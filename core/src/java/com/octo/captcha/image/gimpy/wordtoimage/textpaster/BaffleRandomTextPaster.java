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
 */

package com.octo.captcha.image.gimpy.wordtoimage.textpaster;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

import com.octo.captcha.CaptchaException;

/**
 * <p>text paster that paint white holes on the string (erase some parts)</p>
 * You may specify the number of holes per glyph : 3 by default.
 * You may specify the color of holes : TextColor by default.
 * @see {http://www.parc.xerox.com/research/istl/projects/captcha/default.html}
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class BaffleRandomTextPaster extends RandomTextPaster
{
    private Integer numberOfHolesPerGlyph = new Integer(3) ;
    private Color holesColor;

//    public BaffleRandomTextPaster(Integer minAcceptedWordLenght, Integer maxAcceptedWordLenght, Color textColor)
//    {
//        super(minAcceptedWordLenght, maxAcceptedWordLenght, textColor);
//    }
//
//    public BaffleRandomTextPaster(Integer minAcceptedWordLenght, Integer maxAcceptedWordLenght, Color textColor,
//                                 Integer numberOfHolesPerGlyph)
//    {
//        super(minAcceptedWordLenght, maxAcceptedWordLenght, textColor);
//        this.numberOfHolesPerGlyph = numberOfHolesPerGlyph!=null?numberOfHolesPerGlyph:this.numberOfHolesPerGlyph;
//    }

    public BaffleRandomTextPaster(Integer minAcceptedWordLenght, Integer maxAcceptedWordLenght, Color textColor,
                                 Integer numberOfHolesPerGlyph, Color holesColor)
    {
        super(minAcceptedWordLenght, maxAcceptedWordLenght, textColor);
        this.numberOfHolesPerGlyph = numberOfHolesPerGlyph!=null?numberOfHolesPerGlyph:this.numberOfHolesPerGlyph;
        this.holesColor = holesColor!=null?holesColor:textColor;
    }
    /**
     * Pastes the attributed string on the backround image and return the final image.
     * Implementation must take into account the fact that the text must be readable
     * by human and non by programs
     * @param background
     * @param attributedWord
     * @return the final image
     * @throws CaptchaException if any exception accurs during paste routine.
     */
    public BufferedImage pasteText(BufferedImage background, AttributedString attributedWord) throws CaptchaException
    {
        BufferedImage out = copyBackground(background);
        Graphics2D pie = pasteBackgroundAndSetTextColor(out, background);
        //set font to max in order to retrieve the correct boundaries
        Font maxFont = getMaxFont(attributedWord.getIterator());
        Rectangle2D bounds = getTextBoundaries(pie, maxFont, attributedWord);
        int[] randomDeviation = getRandomDeviation(background, bounds, maxFont);
        //draw the string
        pie.drawString(attributedWord.getIterator(), randomDeviation[0], randomDeviation[1]);

        //draw the holes
        //Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f);
        //pie.setComposite(c);
        //Color circleColor = ((Graphics2D)background.getGraphics()).getColor();
                //pie.getColor();
        pie.setColor(holesColor);
        int numberOfHoles = numberOfHolesPerGlyph.intValue()*attributedWord.getIterator().getEndIndex();
        int circleMaxSize = maxFont.getSize()/3;
        if(circleMaxSize == 0) {
            throw new CaptchaException("The font is too small");
        }
        for(int i = 0; i<numberOfHoles;i++){
            int circleSize = myRandom.nextInt(circleMaxSize)/2+circleMaxSize/2;
            double circlex = bounds.getMaxX()*myRandom.nextGaussian();
            double circley = bounds.getMaxY()*myRandom.nextGaussian();
            Ellipse2D circle = new Ellipse2D.Double(randomDeviation[0]+circlex,
                    randomDeviation[1]-maxFont.getSize()/2+circley,circleSize,circleSize);
            pie.fill(circle);

        }
        pie.dispose();
        return out;
    }
}
