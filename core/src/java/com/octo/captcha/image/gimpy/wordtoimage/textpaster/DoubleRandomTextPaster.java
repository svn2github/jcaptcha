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

import com.octo.captcha.CaptchaException;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

/**
 * <p>Randomly pastes two times the attributed string with a random(5) decalage</p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class DoubleRandomTextPaster extends RandomTextPaster {

    public DoubleRandomTextPaster(Integer minAcceptedWordLenght, Integer maxAcceptedWordLenght, Color textColor) {
        super(minAcceptedWordLenght, maxAcceptedWordLenght, textColor);
    }

    /**
     * Pastes the attributed string on the backround image and return the final image.
     * Implementation must take into account the fact that the text must be readable
     * by human and non by programs.
     * Paste the text randomly on the background<
     * @param background
     * @param attributedWord
     * @return the final image
     * @throws com.octo.captcha.CaptchaException if any exception occurs during paste routine.
     */
    public BufferedImage pasteText(final BufferedImage background, final AttributedString attributedWord)
            throws CaptchaException {
        BufferedImage out = copyBackground(background);
        Graphics2D pie = pasteBackgroundAndSetTextColor(out, background);

        //set font to max in order to retrieve the correct boundaries
        Font maxFont = getMaxFont(attributedWord.getIterator());
        pie.setFont(maxFont);
        FontRenderContext frc = pie.getFontRenderContext();
        //get boundaries for the max font
        Rectangle2D bounds = pie.getFont().getStringBounds(attributedWord.getIterator()
                , attributedWord.getIterator().getBeginIndex()
                , attributedWord.getIterator().getEndIndex(), frc);

        //evaluate the max deviation
        Double maxx = new Double(background.getWidth() - bounds.getWidth());
        Double maxy = new Double(background.getHeight() - bounds.getHeight() - 0.4 * maxFont.getSize());
        if (maxx.intValue() < 0 || maxy.intValue() < 0)
            throw new CaptchaException("word is too big, try to use less letters, smaller font or bigger background");

        //evaluate the first to second deviation
        int xdev = new Double((1 + myRandom.nextDouble()) * 0.25 * maxFont.getSize()).intValue();
        int ydev = new Double((1 + myRandom.nextDouble()) * 0.2 * maxFont.getSize()).intValue();

        //evaluate the random deviation
        int x = myRandom.nextInt(maxx.intValue() - xdev);
        //don't forget y goes down!
        int y = maxFont.getSize() + myRandom.nextInt(maxy.intValue() - ydev);
        //draw the string
        pie.drawString(attributedWord.getIterator(), x, y);
        // draw the second one
        pie.drawString(attributedWord.getIterator(), x + xdev, y + ydev);
        pie.dispose();
        return out;

    }


}