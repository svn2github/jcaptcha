/*
 * JCaptcha, the open source java framework for captcha definition and integration
 * Copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.engine.image;

import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.engine.CaptchaEngineException;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.ImageCaptchaFactory;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * <p>This is a very simple gimpy, which is constructed from an array of Factory and randomly return one when the
 * getCaptchaFactory is called</p>
 *
 * @author <a href="mailto:mag@jcaptcha.net">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public abstract class DefaultImageCaptchaEngine extends ImageCaptchaEngine {

    private ImageCaptchaFactory[] factories;
    private Random myRandom = new SecureRandom();

    /**
     * Default constructor : takes an array of ImageCaptchaFactories.
     */
    public DefaultImageCaptchaEngine(final ImageCaptchaFactory[] factories) {
        this.factories = factories;
        if (factories == null || factories.length == 0) {
            throw new CaptchaEngineException("DefaultImageCaptchaEngine cannot be " +
                    "constructed with a null or empty factories array");
        }
    }


    /**
     * This method build a ImageCaptchaFactory.
     *
     * @return a CaptchaFactory
     */
    public final ImageCaptchaFactory getImageCaptchaFactory() {
        return factories[myRandom.nextInt(factories.length)];
    }

    /**
     * This method use an object parameter to build a CaptchaFactory.
     *
     * @return a CaptchaFactory
     */
    public final ImageCaptcha getNextImageCaptcha() {
        return getImageCaptchaFactory().getImageCaptcha();
    }

    /**
     * This return a new captcha. It may be used directly.
     *
     * @param locale the desired locale
     * @return a new Captcha
     */
    public ImageCaptcha getNextImageCaptcha(Locale locale) {
        return getImageCaptchaFactory().getImageCaptcha(locale);
    }


    /**
     * @return captcha factories used by this engine
     */
    public CaptchaFactory[] getFactories() {
        return factories;
    }

    /**
     * @param factories new captcha factories for this engine
     */
    public void setFactories(CaptchaFactory[] factories) throws CaptchaEngineException {
        if (factories == null || factories.length == 0) {
            throw new CaptchaEngineException("impossible to set null or empty factories");
        }
        ArrayList tempFactories = new ArrayList();

        for (int i = 0; i < factories.length; i++) {
            if (!ImageCaptchaFactory.class.isAssignableFrom(factories[i].getClass())) {
                throw new CaptchaEngineException("This factory is not an image captcha factory " + factories[i].getClass());
            }
            tempFactories.add(factories[i]);

        }

        this.factories = (ImageCaptchaFactory[]) tempFactories.toArray(new ImageCaptchaFactory[factories.length]);
    }

}
