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

import com.octo.captcha.image.gimpy.wordtoimage.TextPaster;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * <p>Base class for Font generators. Sub classes must implement the
 * pasteText(BufferedImage background, AttributedString attributedWord)
 * method that return an image containing the pasted string.</br>
 * use constructor to specify your paster properties.
 * This base class use two Integers, maxAcceptedWordLenght and minAcceptedWordLenghtby wich are the lenght
 * boundaries for the implementation.
 * By default minAcceptedWordLenght = 6 and maxAcceptedWordLenght = 20</p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public abstract class AbstractTextPaster implements TextPaster
{

    public Random myRandom = new Random();

    private int max = 20;
    private int min = 6;
    private Color textColor = Color.BLUE;

    AbstractTextPaster(Integer minAcceptedWordLenght, Integer maxAcceptedWordLenght, Color textColor)
    {
        this.max = maxAcceptedWordLenght != null ? maxAcceptedWordLenght.intValue() : this.max;
        this.min = minAcceptedWordLenght != null && minAcceptedWordLenght.intValue() <= this.max
                ? minAcceptedWordLenght.intValue() : Math.min(this.min, this.max - 1);
        if (textColor != null) this.textColor = textColor;
    }

    /**
     *
     * @return the color that will be used to paste the text
     */
    public Color getTextColor()
    {
        return textColor;
    }

    /**
     * @return the max word lenght accepted by this word2image service
     */
    public int getMaxAcceptedWordLenght()
    {
        return max;
    }

    /**
     *@return the min word lenght accepted by this word2image service
     */
    public int getMinAcceptedWordLenght()
    {
        return min;
    }

    BufferedImage copyBackground(final BufferedImage background)
    {
        BufferedImage out = new BufferedImage(background.getWidth(), background.getHeight(), background.getType());
        return out;
    }

    Graphics2D pasteBackgroundAndSetTextColor(BufferedImage out, final BufferedImage background)
    {
        Graphics2D pie = (Graphics2D) out.getGraphics();
        //paste background
        pie.drawImage(background, 0, 0, out.getWidth(), out.getHeight(), null);
        pie.setColor(getTextColor());
        return pie;
    }

}
