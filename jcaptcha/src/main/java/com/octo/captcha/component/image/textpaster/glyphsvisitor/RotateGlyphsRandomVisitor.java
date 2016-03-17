package com.octo.captcha.component.image.textpaster.glyphsvisitor;

import java.awt.geom.Rectangle2D;
import java.security.SecureRandom;
import java.util.Random;

import com.octo.captcha.component.image.textpaster.Glyphs;

/**
 * @author mag
 * @Date 6 mars 2008
 */
public class RotateGlyphsRandomVisitor implements GlyphsVisitors {

    private double maxAngle =Math.PI/8;
    private Random myRandom = new SecureRandom();

    public RotateGlyphsRandomVisitor() {
    }

    public RotateGlyphsRandomVisitor(double maxAngle) {
        this.maxAngle = maxAngle;
    }

    public void visit(Glyphs gv, Rectangle2D backroundBounds) {

        for(int i=0;i<gv.size();i++){
            gv.rotate(i,maxAngle*myRandom.nextGaussian());
        }
    }
}