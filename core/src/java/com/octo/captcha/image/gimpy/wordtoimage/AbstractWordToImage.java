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

package com.octo.captcha.image.gimpy.wordtoimage;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.image.gimpy.WordToImage;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

/**
 * <p>Implementation skeletton for the WordToImage interface</p>
 * Basically this class implements the imageFromWord method proceding as folow :
 * <ul>
 * <li>Checks the word lenght</li>
 * <li>Creates an java.text.AttributedString from the word</li>
 * <li>Apply font to the AttributedString using the abstract method getFont</li>
 * <li>Create an image for the background using the abstact method getBackround</li>
 * <li>Put the text on the backround using the abstact method pasteText</li>
 * <li>Return the newly created image</li>
 * </ul>
 *
 * This class implements the Template method pattern from the GOF design patterns.
 *
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public abstract class AbstractWordToImage implements WordToImage
{

    /**
     * Creates an image of the provided String
     * This method is a skeleton for creation algorithm.
     * it proceeds as folows :
     * <ul>
     * <li>Checks the word lenght</li>
     * <li>Creates an java.text.AttributedString from the word</li>
     * <li>Apply font to the AttributedString using the abstract method getFont</li>
     * <li>Create an image for the background using the abstact method getBackround</li>
     * <li>Put the text on the backround using the abstact method pasteText</li>
     * <li>Return the newly created image</li>
     * </ul>
     *
     * @return an image representation of the word
     * @throws CaptchaException if word is invalid or if image generation fails.
     */
    public BufferedImage getImage(String word) throws CaptchaException
    {
        int wordLenght;
        //check word
        wordLenght = checkWordLenght(word);
        //create attribute string from word
        AttributedString attributedWord = getAttributedString(word, wordLenght);

        //create backgound
        BufferedImage background = getBackround();
        //apply text on background
        return pasteText(background, attributedWord);

    }

    AttributedString getAttributedString(String word, int wordLenght)
    {
        AttributedString attributedWord = new AttributedString(word);
        //apply font to string

        for (int i = 0 ; i < wordLenght ; i++)
        {
            Font font = getFont();//get the new font for next character
            //apply font to next character
            attributedWord.addAttribute(TextAttribute.FONT, font, i, i + 1);
        }
        return attributedWord;
    }

    int checkWordLenght(String word) throws CaptchaException
    {
        int wordLenght;
        if (word == null)
        {
            throw new CaptchaException("null word");
        } else
        {
            wordLenght = word.length();
            if (wordLenght > this.getMaxAcceptedWordLenght() || wordLenght < getMinAcceptedWordLenght())
            {
                throw new CaptchaException("invalid lenght word");
            }
        }
        return wordLenght;
    }

    /**
     * Method from imageFromWord method to apply font to String.
     * Implementations must take into account the minFontSize and the MaxFontSize.
     * @return a Font
     */
    abstract Font getFont();

    /**
     * Generates a backround image on wich text will be paste.
     * Implementations must take into account the imageHeigt and imageWidth.
     * @return the background image
     */
    abstract BufferedImage getBackround();

    /**
     * Pastes the attributed string on the backround image and return the final image.
     * Implementation must take into account the fact that the text must be readable
     * by human and non by programs
     * @param background
     * @param attributedWord
     * @return the final image
     * @throws CaptchaException if any exception accurs during paste routine.
     */
    abstract BufferedImage pasteText(final BufferedImage background, final AttributedString attributedWord)
            throws CaptchaException;

}
