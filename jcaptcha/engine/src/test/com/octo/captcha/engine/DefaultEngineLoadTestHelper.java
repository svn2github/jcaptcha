/*
 * Created on 15 mars 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.octo.captcha.engine;

import net.sourceforge.groboutils.junit.v1.TestRunnable;

import com.octo.captcha.Captcha;

/**
 * @author Benoit
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DefaultEngineLoadTestHelper extends TestRunnable
{

    protected CaptchaEngine engine;

    protected int count;

    protected int sleepTime;

    public DefaultEngineLoadTestHelper(CaptchaEngine engine, int count, int delay)
    {
        this.engine = engine;
        this.count = count;
        this.sleepTime = delay;
    }

    public void runTest() throws Throwable
    {
        for (int i = 0; i < this.count; ++i)
        {
            Captcha captcha = this.engine.getNextCaptcha();
            assertNotNull(captcha);
            Thread.sleep(this.sleepTime);
        }
    }
}