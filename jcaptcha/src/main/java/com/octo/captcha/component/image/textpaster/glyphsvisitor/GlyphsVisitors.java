package com.octo.captcha.component.image.textpaster.glyphsvisitor;

import java.awt.geom.Rectangle2D;

import com.octo.captcha.component.image.textpaster.Glyphs;

/**
 * @author mag
 * @Date 6 mars 2008
 */
public interface GlyphsVisitors {
    void visit(Glyphs glyphs, Rectangle2D backroundBounds);
}
