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
import java.io.IOException;
import java.io.OutputStream;

import com.octo.captcha.web.WebCaptcha;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * A Gimpy WebCaptcha.
 *
 * @version $Id$
 *
 * @author <a href="mailto:sbr@octo.com">Sebastien Brunot</a>
 */
public abstract class Gimpy implements WebCaptcha
{
    /**
     * The word the user must spell to success the challenge
     */
    protected String word = null;

    /**
     * Constructor : initialize the word the user must spell
     * by calling internat initializeWord() method
     *
     */
    public Gimpy()
    {
        this.word = this.initializeWord();
    }

    /** 
     * Initialize the word the user must spell (called by the Constructor)
     * @return String the word the user must spell
     */
    protected abstract String initializeWord();

    /** 
     * Display the word (as a gimpy captcha) as a BufferedImage
     * @return an image displaying the word the user must guess,
     * in such a way that it is very difficult for a computer to guess
     * it using OCR.
     */
    protected abstract BufferedImage getImageFromWord();

    /**
     * @see com.octo.captcha.web.WebCaptcha#testChallengeResponse(java.lang.String)
     */
    public boolean validateResponse(String theChallengeResponse)
    {
        if (theChallengeResponse == null)
        {
            return (this.word == null);
        }
        else
        {
            return (theChallengeResponse.equals(this.word));
        }
    }

    /**
     * @see com.octo.captcha.web.WebCaptcha#renderAsJPEG(java.io.OutputStream)
     */
    public void renderChallengeAsJPEG(OutputStream theOutputStream)
        throws IOException
    {
        BufferedImage image = renderChallengeAsBufferedImage();
        JPEGImageEncoder jpegEncoder =
            JPEGCodec.createJPEGEncoder(theOutputStream);
        jpegEncoder.encode(image);
    }

    /**
     * @see com.octo.captcha.web.WebCaptcha#renderAsBufferedImage()
     */
    public BufferedImage renderChallengeAsBufferedImage()
    {
        return getImageFromWord();
    }

}
