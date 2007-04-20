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
package com.octo.captcha.engine;

import com.octo.captcha.Captcha;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

/**
 * @author Benoit
 */
public class DefaultEngineLoadTestHelper extends TestRunnable {

    protected CaptchaEngine engine;

    protected int count;

    protected int sleepTime;

    public DefaultEngineLoadTestHelper(CaptchaEngine engine, int count, int delay) {
        this.engine = engine;
        this.count = count;
        this.sleepTime = delay;
    }

    public void runTest() throws Throwable {
        for (int i = 0; i < this.count; ++i) {
            Captcha captcha = this.engine.getNextCaptcha();
            assertNotNull(captcha);
            Thread.sleep(this.sleepTime);
        }
    }
}