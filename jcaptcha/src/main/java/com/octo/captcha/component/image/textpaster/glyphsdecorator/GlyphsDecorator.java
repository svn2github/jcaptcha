package com.octo.captcha.component.image.textpaster.glyphsdecorator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.octo.captcha.component.image.textpaster.Glyphs;

/**
 * @author mag
 * @Date 11 mars 2008
 */
public interface GlyphsDecorator {
    /*static int ALL_GLYPHS_TYPE=1;
    static int SINGLE_GLYPHS_TYPE=2;
    int getType();*/
    void decorate(Graphics2D g2, Glyphs glyphs, BufferedImage backround);
}
