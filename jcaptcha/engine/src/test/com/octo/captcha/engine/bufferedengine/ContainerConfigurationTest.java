
package com.octo.captcha.engine.bufferedengine;

import java.util.HashMap;
import java.util.Locale;

import junit.framework.TestCase;

/**
 * Unit test the ContainerConfiguration
 * 
 * @author Benoit Doumas
 */
public class ContainerConfigurationTest extends TestCase
{
    ContainerConfiguration config  = null;
    
    HashMap localRatio = null;
    
    int maxMemSize = 10;
    
    int maxPersistentSize = 100;
    
    int swapSize = 10;
    
    int feedSize = 100;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        localRatio = new HashMap(2);
        
        localRatio.put(Locale.FRANCE, new Double(0.2));
        localRatio.put(Locale.US, new Double(0.8));
        config = new ContainerConfiguration(localRatio, 0, 0);
    }

    public void testSetFeedSize()
    {
        config.setFeedSize(new Integer(feedSize));
        
        assertEquals(feedSize, config.getFeedSize().intValue());
    }

    public void testSetLocaleRatio()
    {
        HashMap localRatio2 = new HashMap(3);
        localRatio2.put(Locale.GERMAN, new Double(0.1));
        localRatio2.put(Locale.CHINA, new Double(0.8));
        localRatio2.put(Locale.ITALY, new Double(0.1));
        
        config.setLocaleRatio(localRatio2);
        
        assertEquals(3, config.getLocaleRatio().size());
    }

    public void testSetMaxPersistentMemorySize()
    {
        config.setMaxPersistentMemorySize(new Integer(maxPersistentSize));
        
        assertEquals(maxPersistentSize, config.getMaxPersistentMemorySize().intValue());
    }

    public void testSetMaxVolatileMemorySize()
    {
        config.setMaxVolatileMemorySize(new Integer(maxMemSize));
        
        assertEquals(maxMemSize, config.getMaxVolatileMemorySize().intValue());
    }

    public void testSetSwapSize()
    {
        config.setSwapSize(new Integer(swapSize));
        
        assertEquals(swapSize, config.getSwapSize().intValue());
    }

}
