/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * Copyright (c) 2005 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.component.image.fontgenerator;

import java.awt.*;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author <a href="mailto:mag@jcaptcha.net">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class RandomFontGenerator extends AbstractFontGenerator {
    /**
     * list of valid fonts.
     */
    protected static java.util.List defaultFonts;

    /**
     * list of fonts given by constructor.
     */
    protected java.util.List fonts = null;

    /**
     * These are the valid font styles.
     */
    protected int[] STYLES = {Font.PLAIN, Font.ITALIC, Font.BOLD, Font.ITALIC | Font.BOLD};


    /**
     * Any font that this class uses must be able to generate all of the characters in this list.
     */
    protected static String requiredCharacters = "abcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * Prefixes of font names that should be avoided.  The default values list fonts that are totally fine in terms of
     * representing characters, of course, but they're too commonly available in OCR programs.
     */
    protected String[] badFontNamePrefixes = {
            "Courier",
            "Times Roman",
    };

    protected Random myRandom = new SecureRandom();

    public RandomFontGenerator(Integer minFontSize, Integer maxFontSize) {
        super(minFontSize, maxFontSize);
    }

    public RandomFontGenerator(Integer minFontSize, Integer maxFontSize, Font[] fontsList) {
        super(minFontSize, maxFontSize);
        fonts = initializeFonts(fontsList);
    }

    /**
     * Method from imageFromWord method to apply font to String. Implementations must take into account the minFontSize
     * and the MaxFontSize.
     *
     * @return a Font
     */
    public Font getFont() {

        //defaultFonts are initialized one time, cause static
        if (defaultFonts == null) {
            // we cache a lot of decisions about fonts -- do this as little as possible
            synchronized (RandomFontGenerator.class) {
                if (defaultFonts == null) {
                    defaultFonts = initializeFonts(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts());
                }
            }
        }

        if (fonts == null) {
            fonts = defaultFonts;
        }

        Font font = (Font) fonts.get(myRandom.nextInt(fonts.size()));

        int plus = 0;
        if (getMaxFontSize() - getMinFontSize() > 0) {
            plus = Math.abs(myRandom.nextInt(getMaxFontSize()
                    - getMinFontSize()));
        }

        Font styled =
                new Font(font.getFontName(),
                        STYLES[myRandom.nextInt(STYLES.length)],
                        getMinFontSize() + plus);
        return styled;

    }


    /**
     * Create an array of fonts that is known to properly represent all the characters in requiredCharacters.
     *
     * @return array of fonts
     * @see #requiredCharacters
     */
    private List initializeFonts(Font[] uncheckFonts) {

        // get a copy of the fonts
        // NB: be careful with this first array! -- the graphics environment obligingly
        // provides a pointer into its internal font array.

        List goodFonts = new ArrayList(uncheckFonts.length);
        // add copy of copy of list of fonts because of asList's special class and also because
        // of the graphics environment's internal point
        goodFonts.addAll(Arrays.asList(uncheckFonts));

        // Iterate through all fonts, remove the bad ones
        for (Iterator iter = goodFonts.iterator(); iter.hasNext();) {
            Font f = (Font) iter.next();

            // a font is removed if it cannot display the characters we need.

            for (int i = 0; i < requiredCharacters.length(); i++) {
                if (!f.canDisplay(requiredCharacters.charAt(i))) {
                    iter.remove();
                    break;
                }
            }

            // a font is also removed if it is prefixed by a known-bad name
            for (int i = 0; i < badFontNamePrefixes.length; i++) {
                if (f.getName().startsWith(badFontNamePrefixes[i])) {
                    iter.remove();
                    break;
                }
            }
        }

        return goodFonts;
    }

    /**
     * @return a list of characters that this class must be able to represent
     */
    public static String getRequiredCharacters() {
        return requiredCharacters;
    }

    /**
     * @param requiredCharacters a list of characters that this class must be able to represent
     */
    public static void setRequiredCharacters(String requiredCharacters) {
        RandomFontGenerator.requiredCharacters = requiredCharacters;
        // force reinitialization of this variable
        RandomFontGenerator.defaultFonts = null;
    }

    /**
     * @return an array of font name prefixes that should be not used in generating captchas
     */
    public String[] getBadFontNamePrefixes() {
        return badFontNamePrefixes;
    }

    /**
     * @param badFontNamePrefixes an array of font name prefixes that should be not used in generating captchas
     */
    public void setBadFontNamePrefixes(String[] badFontNamePrefixes) {
        this.badFontNamePrefixes = badFontNamePrefixes;
        // force reinitialization of this variable
        RandomFontGenerator.defaultFonts = null;
    }

}
