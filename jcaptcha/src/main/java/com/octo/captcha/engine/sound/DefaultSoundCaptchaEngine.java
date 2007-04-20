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
package com.octo.captcha.engine.sound;

import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.engine.CaptchaEngineException;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.sound.SoundCaptcha;
import com.octo.captcha.sound.SoundCaptchaFactory;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

/**
 * <p/>
 * <p/>
 * </p>
 *
 * @author Benoit Doumas
 */
public class DefaultSoundCaptchaEngine extends SoundCaptchaEngine {

    private SoundCaptchaFactory[] factories;

    private Random myRandom = new SecureRandom();

    /**
     * Default constructor : takes an array of SoundCaptchaFactories.
     */
    public DefaultSoundCaptchaEngine(final SoundCaptchaFactory[] factories) {
        this.factories = factories;
        if (factories == null || factories.length == 0) {
            throw new CaptchaEngineException("DefaultSoundCaptchaEngine cannot be "
                    + "constructed with a null or empty factories array");
        }
    }


    /**
     * This method build a SoundCaptchaFactory.
     *
     * @return a CaptchaFactory
     */
    public final SoundCaptchaFactory getSoundCaptchaFactory() {
        return factories[myRandom.nextInt(factories.length)];
    }

    /**
     * This method use an object parameter to build a CaptchaFactory.
     *
     * @return a SoundCaptcha
     */
    public final SoundCaptcha getNextSoundCaptcha() {
        return getSoundCaptchaFactory().getSoundCaptcha();
    }

    /**
     * This return a new captcha. It may be used directly.
     *
     * @param locale the desired locale
     * @return a new Captcha
     */
    public SoundCaptcha getNextSoundCaptcha(Locale locale) {
        return getSoundCaptchaFactory().getSoundCaptcha(locale);
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
            if (ImageCaptchaFactory.class.isAssignableFrom(factories[i].getClass())) {
                throw new CaptchaEngineException("This factory is not a sound captcha factory " + factories[i].getClass());
            }
            tempFactories.add(factories[i]);
        }

        this.factories = (SoundCaptchaFactory[]) tempFactories.toArray(new SoundCaptchaFactory[factories.length]);
    }

}