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
package com.octo.captcha.web.impl;

import java.awt.image.BufferedImage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.octo.captcha.CaptchaException;
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
 * @TODO : DOCUMENT ME
 *
 * @version $Id$
 *
 * @author <a href="mailto:sbr@octo.com">Sebastien Brunot</a>
 */
public class TestGimpy extends Gimpy
{
    /**
     * logger
     */
    private Log log = LogFactory.getLog(TestGimpy.class);
    
    /**
     * Length of the word the user must spell
     */
    private static final Integer WORD_LENGTH = new Integer(10);
    
    /**
     * Minimum size of the font the word is displayed with in the question
     */
    private static final Integer MIN_FONT_SIZE = new Integer(20);
    
    /**
     * the WordGenerator used to initialize the word the user must spell
     */
    private static WordGenerator wordGenerator =
        new RandomWordGenerator("ABDEFGHJKMNQRTabdefghjkmnqrt123456789#");
    /**
     * The WordToImage used to display the word as an image 
     */
    private static WordToImage wordToImage =
        new ComposedWordToImage(
            new RandomFontGenerator(MIN_FONT_SIZE),
            new FunkyBackgroundGenerator(new Integer(100), new Integer(300)),
            new DoubleTextPaster(new Integer(50), new Integer(1)));

    /**
     * @see com.octo.captcha.web.impl.Gimpy#getWord()
     */
    protected String initializeWord()
    {
        return wordGenerator.getWord(WORD_LENGTH);
    }

    /**
     * @see com.octo.captcha.web.impl.Gimpy#getImageFromWord(java.lang.String)
     */
    protected BufferedImage getImageFromWord()
    {
        BufferedImage returnedValue =
            new BufferedImage(100, 50, BufferedImage.TYPE_INT_RGB);

        try
        {
            returnedValue = wordToImage.getImage(this.word);
        }
        catch (CaptchaException e)
        {
            log.error("Error getting image from word :", e);
        }

        return returnedValue;
    }

}
