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

    public TwistedAndShearedRandomFontGenerator(Integer minFontSize,
                                                Integer maxFontSize) {
        super(minFontSize, maxFontSize);
    }

    /**
     * Method from imageFromWord method to apply font to String. Implementations must take into account the minFontSize
     * and the MaxFontSize.
     *
     * @return a Font
     */
    public Font getFont() {
        Font font = super.getFont();
        double rx = myRandom.nextDouble() / 3;
        double ry = myRandom.nextDouble() / 3;
        AffineTransform at = AffineTransform.getShearInstance(rx, ry);
        font = font.deriveFont(at);
        return font;
    }
}
