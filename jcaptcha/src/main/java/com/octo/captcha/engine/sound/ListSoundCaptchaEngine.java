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

import com.octo.captcha.CaptchaException;
import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.engine.CaptchaEngineException;
import com.octo.captcha.sound.SoundCaptcha;
import com.octo.captcha.sound.SoundCaptchaFactory;

import java.security.SecureRandom;
import java.util.*;

/**
 * <p/>
 * This engine is based on a java.util.List of factories. It has a default constructor. Sub class must implements the
 * buildInitialFactories() method that should build an initial set of factories. </p>
 *
 * @author Benoit Doumas
 * @version 1.0
 */
public abstract class ListSoundCaptchaEngine extends SoundCaptchaEngine {

    List factories = new ArrayList();

    private Random myRandom = new SecureRandom();

    public ListSoundCaptchaEngine() {
        buildInitialFactories();
        checkFactoriesSize();
    }

    /**
     * this method should be implemented as folow : <ul> <li>First construct all the factories you want to initialize
     * the gimpy with</li> <li>then call the this.addFactoriy method for each factory</li> </ul>
     */
    protected abstract void buildInitialFactories();

    /**
     * Add a factory to the gimpy list
     *
     * @return true if added false otherwise
     */
    public boolean addFactory(SoundCaptchaFactory factory) {
        return factory != null && this.factories.add(factory);
    }

    /**
     * Add an array of factories to the gimpy list
     */
    public void addFactories(SoundCaptchaFactory[] factories) {
        checkNotNullOrEmpty(factories);
        this.factories.addAll(Arrays.asList(factories));
    }


    /**
     * @return captcha factories used by this engine
     */
    public CaptchaFactory[] getFactories() {
        return (CaptchaFactory[]) this.factories.toArray(new CaptchaFactory[factories.size()]);
    }

    /**
     * @param factories new captcha factories for this engine
     */
    public void setFactories(CaptchaFactory[] factories) throws CaptchaEngineException {
        checkNotNullOrEmpty(factories);
        ArrayList tempFactories = new ArrayList();

        for (int i = 0; i < factories.length; i++) {
            if (!SoundCaptchaFactory.class.isAssignableFrom(factories[i].getClass())) {
                throw new CaptchaEngineException("This factory is not an sound captcha factory " + factories[i].getClass());
            }
            tempFactories.add(factories[i]);
        }

        this.factories = tempFactories;
    }

  private void checkNotNullOrEmpty(CaptchaFactory[] factories) {
        if (factories == null || factories.length == 0) {
            throw new CaptchaEngineException("impossible to set null or empty factories");
        }
    }

    /**
     * This method build a SoundCaptchaFactory.
     *
     * @return a CaptchaFactory
     */
    public SoundCaptchaFactory getSoundCaptchaFactory() {
        return (SoundCaptchaFactory) factories.get(myRandom
                .nextInt(factories.size()));
    }

    /**
     * This method build a SoundCaptchaFactory.
     *
     * @return a SoundCaptcha
     */
    public SoundCaptcha getNextSoundCaptcha() {
        return getSoundCaptchaFactory().getSoundCaptcha();
    }

    /**
     * This method build a SoundCaptchaFactory.
     *
     * @return a SoundCaptcha
     */
    public SoundCaptcha getNextSoundCaptcha(Locale locale) {
        return getSoundCaptchaFactory().getSoundCaptcha(locale);
    }

    private void checkFactoriesSize() {
        if (factories.size() == 0)
            throw new CaptchaException("This soundEngine has no factories. Please initialize it "
                    + "properly with the buildInitialFactory() called by "
                    + "the constructor or the addFactory() mehtod later!");
    }

}