package com.octo.captcha.image.wordtoimage.fontgenerator;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

/**
 * <p>Takes a random font and apply a rotation to it. </p>
 *
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class DeformedRandomFontGenerator extends RandomFontGenerator {

    private Random seed = new Random();
    private float angle;

    public DeformedRandomFontGenerator(Integer minFontSize, Integer maxFontSize) {
        super(minFontSize, maxFontSize);
    }

    public Font getFont() {
        angle = seed.nextFloat() / 5;
        AffineTransform at = new AffineTransform();
        at.rotate(angle, seed.nextDouble(), seed.nextDouble());
        Font font = super.getFont().deriveFont(seed.nextInt(), seed.nextInt(10) + 15);
        font = font.deriveFont(at);
        return font;
    }
}
