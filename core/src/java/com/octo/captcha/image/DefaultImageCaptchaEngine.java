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

package com.octo.captcha.image;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.Captcha;

import java.util.Random;
import java.util.Locale;

/**
 * <p>This is a very simple engine, which is constructed from an array of Factory and
 * randomly return one when the getCaptchaFactory is called</p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class DefaultImageCaptchaEngine extends ImageCaptchaEngine {

    private ImageCaptchaFactory[] factories;
    private Random myRandom = new Random();

/**
 * Default constructor : takes an array of ImageCaptchaFactories.
 * @param factories
 */
    public DefaultImageCaptchaEngine(final ImageCaptchaFactory[] factories) {
        this.factories = factories;
        if (factories == null || factories.length == 0) {
            throw new CaptchaException("DefaultImageCaptchaEngine cannot be " +
                    "constructed with a null or empty factories array");
        }
    };

    /**
     * This method build a ImageCaptchaFactory.
     * @return a CaptchaFactory
     */
    public final ImageCaptchaFactory getImageCaptchaFactory() {
        return factories[myRandom.nextInt(factories.length)];
    }

    /**
     * This method use an object parameter to build a CaptchaFactory.
     * @return a CaptchaFactory
     */
    public final ImageCaptcha getNextImageCaptcha() {
        return getImageCaptchaFactory().getImageCaptcha();
    }

    /**
     * This return a new captcha. It may be used directly.
     * @param locale the desired locale
     * @return a new Captcha
     */
    public ImageCaptcha getNextImageCaptcha(Locale locale)
    {
        return getImageCaptchaFactory().getImageCaptcha(locale);
    }

}
