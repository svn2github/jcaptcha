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

import com.octo.captcha.CaptchaException;
import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.engine.CaptchaEngineException;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.ImageCaptchaFactory;

import java.security.SecureRandom;
import java.util.*;

/**
 * <p>This engine is based on a java.util.List of factories. It has a default constructor. Sub class must implements the
 * buildInitialFactories() method that should build an initial set of factories.</p>
 *
 * @author <a href="mailto:mag@jcaptcha.net">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public abstract class ListImageCaptchaEngine
        extends com.octo.captcha.engine.image.ImageCaptchaEngine {

    List factories = new ArrayList();
    private Random myRandom = new SecureRandom();

    public ListImageCaptchaEngine() {
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
    public boolean addFactory(
            com.octo.captcha.image.ImageCaptchaFactory factory) {
        return factory != null && this.factories.add(factory);
    }

    /**
     * Add an array of factories to the gimpy list
     */
    public void addFactories(
            com.octo.captcha.image.ImageCaptchaFactory[] factories) {
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
            if (!ImageCaptchaFactory.class.isAssignableFrom(factories[i].getClass())) {
                throw new CaptchaEngineException("This factory is not an image captcha factory " + factories[i].getClass());
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
     * This method build a ImageCaptchaFactory.
     *
     * @return a CaptchaFactory
     */
    public com.octo.captcha.image.ImageCaptchaFactory getImageCaptchaFactory() {
        return (com.octo.captcha.image.ImageCaptchaFactory) factories.get(
                myRandom.nextInt(factories.size()));
    }

    /**
     * This method build a ImageCaptchaFactory.
     *
     * @return a CaptchaFactory
     */
    public ImageCaptcha getNextImageCaptcha() {
        return getImageCaptchaFactory().getImageCaptcha();
    }

    /**
     * This method build a ImageCaptchaFactory.
     *
     * @return a CaptchaFactory
     */
    public ImageCaptcha getNextImageCaptcha(Locale locale) {
        return getImageCaptchaFactory().getImageCaptcha(locale);
    }

    private void checkFactoriesSize() {
        if (factories.size() == 0)
            throw new CaptchaException(
                    "This gimpy has no factories. Please initialize it "
                            + "properly with the buildInitialFactory() called by "
                            + "the constructor or the addFactory() mehtod later!");
    }

}
