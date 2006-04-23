/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * Copyright (c) 2005 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */
package com.octo.captcha.engine.sound.gimpy;

import com.octo.captcha.Captcha;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.DefaultEngineLoadTestHelper;

/**
 * @author Benoit
 *         <p/>
 *         TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style -
 *         Code Templates
 */
public class SoundEngineLoadTestHelper extends DefaultEngineLoadTestHelper {

    /**
     * @param engine
     * @param count
     * @param delay
     */
    public SoundEngineLoadTestHelper(CaptchaEngine engine, int count, int delay) {
        super(engine, count, delay);
    }

    public void runTest() throws Throwable {
        for (int i = 0; i < this.count; ++i) {
            Captcha captcha = this.engine.getNextCaptcha();
            assertNotNull(captcha);
            captcha.disposeChallenge();
            Thread.sleep(this.sleepTime);
        }
    }
}
