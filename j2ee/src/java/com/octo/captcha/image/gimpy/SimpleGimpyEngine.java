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
 *
 */
package com.octo.captcha.image.gimpy;


import com.octo.captcha.image.ImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.gimpy.wordtoimage.fontgenerator.RandomFontGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.textpaster.DoubleTextPaster;
import com.octo.captcha.image.gimpy.wordgenerator.RandomWordGenerator;
import com.octo.captcha.Captcha;

import java.awt.Color;
import java.util.Locale;

/**
 * A ImageCaptchaEngine which factory creates captcha which challenge is a random
 * string composed with characters A,B,C,D and E.
 *
 * @version $Id$
 *
 * @author <a href="mailto:sbr@octo.com">Sebastien Brunot</a>
 */
public class SimpleGimpyEngine extends ImageCaptchaEngine
{
    ////////////////////////////////////
    // Constants
    ////////////////////////////////////

    /**
     * Width of the captcha image
     */
    private static final Integer IMAGE_WIDTH = new Integer(300);

    /**
     * Height of the captcha image
     */
    private static final Integer IMAGE_HEIGHT = new Integer(100);

    /**
     * Minimum size of the font the word is displayed with in the challenge
     */
    private static final Integer MIN_FONT_SIZE = new Integer(20);

    /**
     * Minimum length of the word displayed by the captcha challenge
     */
    private static final Integer WORD_MIN_LENGTH = new Integer(8);

    /**
     * Maximum length of the word displayed by the captcha challenge
     */
    private static final Integer WORD_MAX_LENGTH = new Integer(10);

    ////////////////////////////////////
    // Attributes
    ////////////////////////////////////

    /**
     * The ImageCaptchaFactory returned by this engine
     */
    private GimpyFactory factory = null;

    ////////////////////////////////////
    // Constructor
    ////////////////////////////////////

    /**
     * Default constructor : creates internal GimpyFactory
     */
    public SimpleGimpyEngine()
    {
        WordGenerator wordGenerator = new RandomWordGenerator("ABCDE");
        WordToImage word2Image =
            new com.octo.captcha.image.gimpy.wordtoimage.ComposedWordToImage(
                new RandomFontGenerator(MIN_FONT_SIZE,null),
                new FunkyBackgroundGenerator(IMAGE_WIDTH, IMAGE_HEIGHT),
                new DoubleTextPaster(WORD_MIN_LENGTH, WORD_MAX_LENGTH, Color.GRAY));
        this.factory = new GimpyFactory(wordGenerator, word2Image);
    }

    ////////////////////////////////////
    // ImageCaptchaFactory concrete class implementation
    ////////////////////////////////////

    /**
     * @see com.octo.captcha.image.ImageCaptchaEngine#getImageCaptchaFactory()
     */
    public ImageCaptchaFactory getImageCaptchaFactory()
    {
        return this.factory;
    }

    /**
     * This method build a ImageCaptchaFactory.
     * @return a CaptchaFactory
     */
    public ImageCaptcha getNextImageCaptcha()
    {
        return this.factory.getImageCaptcha();
    }

    /**
     * This return a new captcha. It may be used directly.
     * @param locale the desired locale
     * @return a new Captcha
     */
    public ImageCaptcha getNextImageCaptcha(Locale locale)
    {
        return this.factory.getImageCaptcha(locale);
    }

}
