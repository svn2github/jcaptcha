/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * Copyright (c) 2005 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.component.image.fontgenerator;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.security.SecureRandom;
import java.util.Random;

/**
 * <p>Takes a random font and apply a rotation to it. </p>
 *
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class DeformedRandomFontGenerator extends RandomFontGenerator {

    private Random rng = new SecureRandom();

    public DeformedRandomFontGenerator(Integer minFontSize,
                                       Integer maxFontSize) {
        super(minFontSize, maxFontSize);
    }

    public Font getFont() {
        // obtain a font, pick a random size & font
        Font font = super.getFont();

        // rotate each letter by -0.33, +0.33 or about 20 degrees
        float theta = (rng.nextBoolean() ? 1 : -1) * rng.nextFloat() / 3;

        // private DecimalFormat debug_fmt = new DecimalFormat();
        // System.out.println("Creating " + font + " rotated angle = " + debug_fmt.format(theta * 57.2957));

        // rotate each letter by this angle
        AffineTransform at = new AffineTransform();
        at.rotate(theta, rng.nextDouble()/*x*/, rng.nextDouble()/*y*/);
        font = font.deriveFont(at);
        return font;
    }
}
