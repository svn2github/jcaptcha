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
package com.octo.captcha.pix.gimpy.impl;

import com.octo.captcha.pix.PixCaptchaEngine;
import com.octo.captcha.pix.PixCaptchaFactory;
import com.octo.captcha.pix.gimpy.GimpyFactory;
import com.octo.captcha.pix.gimpy.WordGenerator;
import com.octo.captcha.pix.gimpy.WordToImage;
import com.octo.captcha.pix.gimpy.wordgenerators.RandomWordGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.ComposedWordToImage;
import com
    .octo
    .captcha
    .pix
    .gimpy
    .wordtoimages
    .backgroundgenerators
    .FunkyBackgroundGenerator;
import com
    .octo
    .captcha
    .pix
    .gimpy
    .wordtoimages
    .fontgenerator
    .RandomFontGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.textpasters.DoubleTextPaster;

/**
 * A PixCaptchaEngine which factory creates captcha which challenge is a random
 * string composed with characters A,B,C,D and E.
 *
 * @version $Id$
 *
 * @author <a href="mailto:sbr@octo.com">Sebastien Brunot</a>
 */
public class SimpleGimpyEngine extends PixCaptchaEngine
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
     * The PixCaptchaFactory returned by this engine
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
            new ComposedWordToImage(
                new RandomFontGenerator(MIN_FONT_SIZE),
                new FunkyBackgroundGenerator(IMAGE_HEIGHT, IMAGE_WIDTH),
                new DoubleTextPaster(WORD_MAX_LENGTH, WORD_MIN_LENGTH));
        this.factory = new GimpyFactory(wordGenerator, word2Image);
    }

    ////////////////////////////////////
    // PixCaptchaFactory concrete class implementation
    ////////////////////////////////////

    /**
     * @see com.octo.captcha.pix.PixCaptchaEngine#getPixCaptchaFactory()
     */
    public PixCaptchaFactory getPixCaptchaFactory()
    {
        return this.factory;
    }
    
}
