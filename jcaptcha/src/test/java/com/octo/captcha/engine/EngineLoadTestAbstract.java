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
import junit.framework.TestCase;
import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import java.lang.reflect.Constructor;

/**
 * Base class for CaptchaEngine load tests...
 */
public abstract class EngineLoadTestAbstract extends TestCase {
    protected CaptchaEngine engine;

    // loader init by default
    protected Class loader = DefaultEngineLoadTestHelper.class;

    public void testGetNextCaptcha() throws Exception {
        Captcha captcha = engine.getNextCaptcha();
        assertNotNull(captcha);
    }

    public void testGetNextCaptchaLongRun1000() throws Exception {
        for (int i = 0; i < 1000; i++) {
            Captcha captcha = engine.getNextCaptcha();
            assertNotNull(captcha.getChallenge());
        }
    }

        public void test_100It_0Del_1Us_2min() throws Throwable {
        int count = 100;
        int delay = 0;
        int users = 1;
        int max_time = 2 * 60 * 1000;
        load(users, count, delay, max_time);

    }

    public void test_1It_0Del_100Us_2min() throws Throwable {
        int count = 1;
        int delay = 0;
        int users = 100;
        int max_time = 2 * 60 * 1000;
        load(users, count, delay, max_time);
    }

    public void test_1000It_0Del_1Us_5min() throws Throwable {
        int count = 1000;
        int delay = 0;
        int users = 1;
        int max_time = 5 * 60 * 1000;
        load(users, count, delay, max_time);

    }

    public void test_200It_0Del_5Us_5min() throws Throwable {
        int count = 200;
        int delay = 0;
        int users = 5;
        int max_time = 5 * 60 * 1000;
        load(users, count, delay, max_time);
    }

    public void test_10It_100Del_10Us_2min() throws Throwable {
        int count = 10;
        int delay = 100;
        int users = 10;
        int max_time = 2 * 60 * 1000;
        load(users, count, delay, max_time);
    }

    public void test_2It_100Del_100Us_5min() throws Throwable {

        int count = 2;
        int delay = 100;
        int users = 100;
        int max_time = 5 * 60 * 1000;
        load(users, count, delay, max_time);
    }

    protected void load(int users, int count, int delay, int max_time) throws Throwable {
        TestRunnable[] tcs = new TestRunnable[users];
        Constructor contructor = loader.getConstructor(new Class[]{CaptchaEngine.class,
                int.class, int.class});

        for (int i = 0; i < users; i++) {
            tcs[i] = (TestRunnable) contructor.newInstance(new Object[]{this.engine,
                    new Integer(count), new Integer(delay)});
        }
        MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(tcs);

        mttr.runTestRunnables(max_time);
    }
}