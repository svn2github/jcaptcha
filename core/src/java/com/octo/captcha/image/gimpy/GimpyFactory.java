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

package com.octo.captcha.image.gimpy;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.ImageCaptchaFactory;

import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Factories for Gimpies. Built on top of WordGenerator and WordToImage.
 * It uses thoses interfaces to build an ImageCaptha answered by a String and for which the question is :
 * Spell the word.
 */
public class GimpyFactory extends ImageCaptchaFactory
{

    private WordToImage wordToImage;
    private WordGenerator wordGenerator;

    public static final String BUNDLE_NAME = Gimpy.class.getName();
    public static final String BUNDLE_QUESTION_KEY = "question";

    public GimpyFactory(WordGenerator generator, WordToImage word2image)
    {
        if (word2image == null)
        {
            throw new CaptchaException("Invalid configuration for a GimpyFactory : WordToImage can't be null");
        }
        if (generator == null)
        {
            throw new CaptchaException("Invalid configuration for a GimpyFactory : WordGenerator can't be null");
        }
        wordToImage = word2image;
        wordGenerator = generator;

    }

    /**
     * gimpies are ImageCaptcha
     * @return the image captcha with default locale
     */
    public ImageCaptcha getImageCaptcha()
    {
        return getImageCaptcha(Locale.getDefault());
    }

    public WordToImage getWordToImage()
    {
        return wordToImage;
    }

    public WordGenerator getWordGenerator()
    {
        return wordGenerator;
    }

    /**
     * gimpies are ImageCaptcha
     * @return a pixCaptcha with the question :"spell the word"
     */
    public ImageCaptcha getImageCaptcha(Locale locale)
    {

        //lenght
        Integer wordLenght;
        int range = getWordToImage().getMaxAcceptedWordLenght() -
                getWordToImage().getMinAcceptedWordLenght();
        int randomRange = range != 0 ? new Random().nextInt(range) : 0;

        wordLenght = new Integer(
                randomRange + getWordToImage().getMinAcceptedWordLenght());
        String word = getWordGenerator().getWord(wordLenght, locale);

        BufferedImage image = null;
        try
        {
            image = getWordToImage().getImage(word);
        } catch (Throwable e)
        {
            throw new CaptchaException(e);
        }

        ImageCaptcha captcha = new Gimpy(ResourceBundle.getBundle(BUNDLE_NAME, locale).getString(BUNDLE_QUESTION_KEY),
                image, word);
        return captcha;
    }

}