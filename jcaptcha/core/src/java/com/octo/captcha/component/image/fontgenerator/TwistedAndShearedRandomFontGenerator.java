/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * Copyright (c) 2005 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.component.image.fontgenerator;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * <p>Description: </p>
 *
 * @author <a href="mailto:mag@jcaptcha.net">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class TwistedAndShearedRandomFontGenerator
        extends TwistedRandomFontGenerator {
    private static final int GENERATED_FONTS_ARRAY_SIZE = 1000;


    private Font[] generatedFonts = new Font[GENERATED_FONTS_ARRAY_SIZE];

    public TwistedAndShearedRandomFontGenerator(Integer minFontSize,
                                                Integer maxFontSize) {
        super(minFontSize, maxFontSize);
        for (int i = 0; i < GENERATED_FONTS_ARRAY_SIZE; i++) {
            Font font = super.getFont();
            double rx = myRandom.nextDouble() / 3;
            double ry = myRandom.nextDouble() / 3;
            AffineTransform at = AffineTransform.getShearInstance(rx, ry);
            font = font.deriveFont(at);
            generatedFonts[i] = font;
        }

    }

    /**
     * Method from imageFromWord method to apply font to String. Implementations must take into account the minFontSize
     * and the MaxFontSize.
     *
     * @return a Font
     */
    public Font getFont() {
        return generatedFonts[Math.abs(myRandom.nextInt(GENERATED_FONTS_ARRAY_SIZE))];
    }
}
