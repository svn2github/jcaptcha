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

package com.octo.captcha.engine.image;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.image.ImageCaptcha;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * <p>This gimpy is based on a java.util.List of factories. It has a default constructor.
 * Sub class must implements the buildInitialFactories() method that should build an initial set of factories.</p>
 *
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public abstract class ListImageCaptchaEngine extends com.octo.captcha.engine.image.ImageCaptchaEngine {

    private List factories = new ArrayList();
    private Random myRandom = new Random();

    public ListImageCaptchaEngine() {
        buildInitialFactories();
        checkFactoriesSize();
    }

    /**
     * this method should be implemented as folow :
     * <ul>
     * <li>First construct all the factories you want to initialize the gimpy with</li>
     * <li>then call the this.addFactoriy method for each factory</li>
     * </ul>
     */
    protected abstract void buildInitialFactories();

    /**
     * Add a factory to the gimpy list
     *
     * @param factory
     * @return true if added false otherwise
     */
    public boolean addFactory(com.octo.captcha.image.ImageCaptchaFactory factory) {
        return this.factories.add(factory);
    }

    /**
     * Add an array of factories to the gimpy list
     *
     * @param factories
     */
    public void addFactories(com.octo.captcha.image.ImageCaptchaFactory[] factories) {
        for (int i = 0; i < factories.length; i++) {
            this.factories.add(factories[i]);
        }
    }

    /**
     * remove the factory from the gimpy list
     *
     * @param factory
     * @return true if removed, false otherwise
     */
    public boolean removeFactory(com.octo.captcha.image.ImageCaptchaFactory factory) {
        return this.factories.remove(factory);
    }

    /**
     * This method build a ImageCaptchaFactory.
     *
     * @return a CaptchaFactory
     */
    public final com.octo.captcha.image.ImageCaptchaFactory getImageCaptchaFactory() {
        checkFactoriesSize();
        return (com.octo.captcha.image.ImageCaptchaFactory) factories.get(myRandom.nextInt(factories.size()));
    }

    /**
     * This method build a ImageCaptchaFactory.
     *
     * @return a CaptchaFactory
     */
    public final ImageCaptcha getNextImageCaptcha() {
        checkFactoriesSize();
        return getImageCaptchaFactory().getImageCaptcha();
    }

    /**
     * This method build a ImageCaptchaFactory.
     *
     * @param locale
     * @return a CaptchaFactory
     */
    public final ImageCaptcha getNextImageCaptcha(Locale locale) {
        checkFactoriesSize();
        return getImageCaptchaFactory().getImageCaptcha(locale);
    }

    private void checkFactoriesSize() {
        if (factories.size() == 0)
            throw new CaptchaException("This gimpy has no factories. Please initialize it " +
                    "properly with the buildInitialFactory() called by the constructor or the addFactory() mehtod later!");
    }

}
