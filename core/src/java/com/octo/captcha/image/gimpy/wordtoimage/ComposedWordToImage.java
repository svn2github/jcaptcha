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

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

/**
 * <p>Base class for composed WordToImage</p>
 * It extends the AbstractWord to image and uses three abstract subclasses :
 * <ul>
 * <li>a AbstractFontGenerator to implement the getFont() method</li>
 * <li>a AbstractBackgroundGenerator to implement the getBackround() method</li>
 * <li>a AbstractTextParser to implement the pasteText() method</li>
 * </ul>
 *
 * By this design people can implement separatly different parts of the algorithm.
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class ComposedWordToImage extends AbstractWordToImage {

    private FontGenerator fontGenerator;
    private BackgroundGenerator background;
    private TextPaster textPaster;

    /**
     * @param fontGenerator a AbstractFontGenerator to implement the getFont() method
     * @param background a AbstractBackgroundGenerator to implement the getBackround() method
     * @param textPaster a AbstractTextParser to implement the pasteText() method
     */
    public ComposedWordToImage(FontGenerator fontGenerator, BackgroundGenerator background, TextPaster textPaster) {
        this.background = background;
        this.fontGenerator = fontGenerator;
        this.textPaster = textPaster;
    }

    /**
     * @return the max word lenght accepted by this word2image service
     */
    public int getMaxAcceptedWordLenght() {
        return textPaster.getMaxAcceptedWordLenght();
    }

    /**
     *@return the min word lenght accepted by this word2image service
     */
    public int getMinAcceptedWordLenght() {
        return textPaster.getMinAcceptedWordLenght();
    }

    /**
     * @return the generated image height
     */
    public int getImageHeight() {
        return background.getImageHeight();
    }

    /**
     *
     * @return teh generated image width
     */
    public int getImageWidth() {
        return background.getImageWidth();
    }

    /**
     *
     * @return the min font size for the generated image
     */
    public int getMinFontSize() {
        return fontGenerator.getMinFontSize();
    }

    /**
     * Method from imageFromWord method to apply font to String.
     * Implementations must take into account the minFontSize and the MaxFontSize.
     * @return a Font
     */
    Font getFont() {
        return fontGenerator.getFont();
    }

    /**
     * Generates a backround image on wich text will be paste.
     * Implementations must take into account the imageHeigt and imageWidth.
     * @return the background image
     */
    BufferedImage getBackround() {
        return background.getBackround();
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
    BufferedImage pasteText(BufferedImage background, AttributedString attributedWord)
            throws CaptchaException {
        return textPaster.pasteText(background, attributedWord);
    }
}
