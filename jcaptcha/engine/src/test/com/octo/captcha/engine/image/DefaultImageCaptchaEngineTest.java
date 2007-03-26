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

package com.octo.captcha.engine.image;

import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.CaptchaEngineException;
import com.octo.captcha.engine.MockImageCaptchaFactory;
import com.octo.captcha.engine.MockSoundCaptchaFactory;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.sound.SoundCaptchaFactory;
import junit.framework.TestCase;

public class DefaultImageCaptchaEngineTest extends TestCase {

    private static final MockImageCaptchaFactory MOCK_IMAGE_CAPTCHA_FACTORY_1 = new MockImageCaptchaFactory();
    private static final MockImageCaptchaFactory MOCK_IMAGE_CAPTCHA_FACTORY_2 = new MockImageCaptchaFactory();

    DefaultImageCaptchaEngine defaultImageCaptchaEngine;
    private ImageCaptchaFactory[] factories = new ImageCaptchaFactory[]{MOCK_IMAGE_CAPTCHA_FACTORY_1};
    private ImageCaptchaFactory[] otherFactories = new ImageCaptchaFactory[]{MOCK_IMAGE_CAPTCHA_FACTORY_2};

    public void testNullOrEmptyFactoryImageCaptchaEngineConstructor() throws Exception {
        //
        try {
            buildCaptchaEngine(null);
            fail("Cannot build with null factories");
        } catch (CaptchaEngineException e) {

        }

        try {
            buildCaptchaEngine(new ImageCaptchaFactory[]{});
            fail("Cannot build with null factories");
        } catch (CaptchaEngineException e) {

        }

    }


    public void testNullOrEmptySetFactories() throws Exception {
        this.defaultImageCaptchaEngine = (DefaultImageCaptchaEngine) buildCaptchaEngine(new ImageCaptchaFactory[]{MOCK_IMAGE_CAPTCHA_FACTORY_1});

        try {
            defaultImageCaptchaEngine.setFactories(null);
            fail("cannot set null factories");
        } catch (CaptchaEngineException e) {

        }

        try {
            defaultImageCaptchaEngine.setFactories(new ImageCaptchaFactory[]{});
            fail("cannot set null factories");
        } catch (CaptchaEngineException e) {

        }

    }


    public void testWrongTypeSetFactories() throws Exception {
        this.defaultImageCaptchaEngine = (DefaultImageCaptchaEngine) buildCaptchaEngine(factories);

        try {
            defaultImageCaptchaEngine.setFactories(new SoundCaptchaFactory[]{new MockSoundCaptchaFactory()});
            fail("cannot set wrong type factories");
        } catch (CaptchaEngineException e) {

        }

    }


    public void testSetFactories() throws Exception {
        this.defaultImageCaptchaEngine = (DefaultImageCaptchaEngine) buildCaptchaEngine(new MockImageCaptchaFactory[]{MOCK_IMAGE_CAPTCHA_FACTORY_1});
        assertEquals(factories[0], defaultImageCaptchaEngine.getFactories()[0]);


        defaultImageCaptchaEngine.setFactories(otherFactories);
        assertEquals(otherFactories[0], defaultImageCaptchaEngine.getFactories()[0]);


    }


    CaptchaEngine buildCaptchaEngine(Object[] parameter) {
        return new ImplDefaultImageCaptchaEngine((ImageCaptchaFactory[]) parameter);
    }

    private class ImplDefaultImageCaptchaEngine extends DefaultImageCaptchaEngine {
        /**
         * Default constructor : takes an array of ImageCaptchaFactories.
         */
        public ImplDefaultImageCaptchaEngine(final ImageCaptchaFactory[] factories) {
            super(factories);
        }
    }


}