/*
 * Created on Jul 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.octo.captcha.engine.bufferedengine;

import java.util.Locale;

import com.octo.captcha.Captcha;
import com.octo.captcha.engine.CaptchaEngine;

import junit.framework.TestCase;

/**
 * @author Benoit
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class BufferedEngineContainerTestAbstract extends TestCase
{

    /*
     * Class under test for Captcha getNextCaptcha()
     */
    public void testGetNextCaptcha()
    {
        BufferedEngineContainer engine = getEngine();
        assertTrue(Captcha.class.isInstance(engine.getNextCaptcha()));
        releaseEngine(engine);
    }

    /*
     * Class under test for Captcha getNextCaptcha(Locale)
     */
    public void testGetNextCaptchaLocale()
    {
        BufferedEngineContainer engine = getEngine();
        assertTrue(Captcha.class.isInstance(engine.getNextCaptcha(Locale.FRANCE)));
        releaseEngine( engine);
    }

    public void testSwapCaptchasFromPersistentToVolatileMemory()
    {
        BufferedEngineContainer engine = getEngine();
        int size =engine.getVolatileBuffer().size();
        
        engine.feedPersistentBuffer();
        
        engine.swapCaptchasFromPersistentToVolatileMemory();
        
        assertTrue(size < engine.getVolatileBuffer().size());
        
        releaseEngine( engine);
    }

    public void testFeedPersistentBuffer()
    {
        BufferedEngineContainer engine = getEngine();
        int size =engine.getPersistentBuffer().size();
        
        engine.feedPersistentBuffer();
        
        assertTrue(size < engine.getPersistentBuffer().size());
        releaseEngine( engine);
    }
    
    public abstract BufferedEngineContainer getEngine();
    
    public abstract void releaseEngine(BufferedEngineContainer engine);

}
