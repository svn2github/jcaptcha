/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * Portions of this software are based upon public domain software
 * originally written at the National Center for Supercomputing Applications,
 * University of Illinois, Urbana-Champaign.
 */

package com.octo.captcha.image.gimpy.wordtoimage.backgroundgenerator;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * <p>Draw mutliple different shape with different colors. see attributes to construct it in a proper way.</p>
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class MultipleShapeBackgroundGenerator
        extends AbstractBackgroundGenerator
{

    public MultipleShapeBackgroundGenerator(Integer width, Integer height)
    {
        super(width, height);
    }

    /**
     * Default value for the first color (black) of the gradient paint of ellipses.
     */
    private Color firstEllipseColor = new Color(210, 210, 210);

    /**
     * Default value for the first color (White) of the gradient paint of ellipses.
     */
    private Color secondEllipseColor = new Color(0, 0, 0);

    /**
     * Default value for the first color (black) of the gradient paint of rectangles.
     */
    private Color firstRectangleColor = new Color(210, 210, 210);

    /**
     * Default value for the first color (White) of the gradient paint of rectangles.
     */
    private Color secondRectangleColor = new Color(0, 0, 0);

    /**
     * Default space between lines: 10 pixels.
     */
    private Integer spaceBetweenLine = new Integer(10);

    /**
     * Default space between circles: 10 pixels.
     */
    private Integer spaceBetweenCircle = new Integer(10);

    /**
     * Default height for the ellipse: 8 pixels.
     */
    private Integer ellipseHeight = new Integer(8);

    /**
     * Default width for the ellipse: 8 pixels.
     */
    private Integer ellipseWidth = new Integer(8);

    /**
     * Default width for the rectangle: 3 pixels.
     */
    private Integer rectangleWidth = new Integer(3);

    /**
     * Constructor with full parameters
     * @param width
     * @param height
     * @param firstEllipseColor
     * @param secondEllipseColor
     * @param spaceBetweenLine
     * @param spaceBetweenCircle
     * @param ellipseHeight
     * @param ellipseWidth
     * @param firstRectangleColor
     * @param secondRectangleColor
     * @param rectangleWidth
     */
    public MultipleShapeBackgroundGenerator(
            Integer width, Integer height, Color firstEllipseColor, Color secondEllipseColor,
            Integer spaceBetweenLine, Integer spaceBetweenCircle,
            Integer ellipseHeight, Integer ellipseWidth,
            Color firstRectangleColor, Color secondRectangleColor,
            Integer rectangleWidth)
    {

        super(width, height);

        if (firstEllipseColor != null)
            this.firstEllipseColor = firstEllipseColor;
        if (secondEllipseColor != null)
            this.secondEllipseColor = secondEllipseColor;
        if (spaceBetweenLine != null)
            this.spaceBetweenLine = spaceBetweenCircle;
        if (spaceBetweenCircle != null)
            this.spaceBetweenCircle = spaceBetweenCircle;
        if (ellipseHeight != null)
            this.ellipseHeight = ellipseHeight;
        if (ellipseWidth != null)
            this.ellipseWidth = ellipseWidth;
        if (firstRectangleColor != null)
            this.firstRectangleColor = firstRectangleColor;
        if (secondRectangleColor != null)
            this.secondRectangleColor = secondRectangleColor;
        if (rectangleWidth != null)
            this.rectangleWidth = rectangleWidth;
    }

    /**
     * Main method. It generates a background of the captcha with
     * a large number of lines, ellipse, and gradient paint.
     * @return the background full of shapes
     */
    public BufferedImage getBackround()
    {
        BufferedImage bi = new BufferedImage(getImageWidth(), getImageHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0 ; i < getImageWidth() ;
             i = i + this.getSpaceBetweenLine())
        {
            for (int j = 0 ; j < getImageHeight() ;
                 j = j + this.getSpaceBetweenCircle())
            {
                Ellipse2D e2 = new Ellipse2D.Double(i, j, this.getEllipseHeight(), this.getEllipseWidth());
                GradientPaint gp = new GradientPaint(0, this.getEllipseHeight(),
                        firstEllipseColor, this.getEllipseWidth(), 0, secondEllipseColor, true);

                g2.setPaint(gp);
                g2.fill(e2);
            }
            GradientPaint gp2 = new GradientPaint(0, getImageHeight(), this.firstRectangleColor,
                    this.getRectangleWidth(), 0, this.secondRectangleColor, true);
            g2.setPaint(gp2);
            Rectangle2D r2 = new Rectangle2D.Double(i, 0, this.getRectangleWidth(),
                    getImageHeight());
            g2.fill(r2);
        }
        g2.dispose();
        return bi;
    }

    /**
     * Helper method to get the int value of the number of pixels between lines.
     * @return number of pixels between lines.
     */
    protected int getSpaceBetweenLine()
    {
        return this.spaceBetweenLine.intValue();
    }

    /**
     * Helper method to get the int value of the number of pixels between circles.
     * @return number of pixels between circles.
     */
    protected int getSpaceBetweenCircle()
    {
        return this.spaceBetweenCircle.intValue();
    }

    /**
     * Helper method to get the height of drawn ellipses.
     * @return height of ellipses.
     */
    protected int getEllipseHeight()
    {
        return this.ellipseHeight.intValue();
    }

    /**
     * Helper method to get the width of drawn ellipses.
     * @return width of ellipses.
     */
    protected int getEllipseWidth()
    {
        return this.ellipseWidth.intValue();
    }

    /**
     * Helper method to get the width of drawn rectangles.
     * @return width of rectangles.
     */
    protected int getRectangleWidth()
    {
        return this.rectangleWidth.intValue();
    }
}
