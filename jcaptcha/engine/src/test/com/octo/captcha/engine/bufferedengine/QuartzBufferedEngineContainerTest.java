/*
 * Created on May 2, 2005
 *
 */
package com.octo.captcha.engine.bufferedengine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.octo.captcha.engine.DefaultEngineLoadTestHelper;

import junit.framework.TestCase;

/**
 * @author Doumas Benoit
 */
public class QuartzBufferedEngineContainerTest extends TestCase
{
    private static final Log log = LogFactory.getLog(QuartzBufferedEngineContainerTest.class
        .getName());

    // loader init by default
    protected Class loader = DefaultEngineLoadTestHelper.class;

    public void testBasic() throws Exception
    {
        Resource ressource = new ClassPathResource("testQuartzBufferedEngine.xml");
        ConfigurableBeanFactory bf = new XmlBeanFactory(ressource);
        BufferedEngineContainer container = (BufferedEngineContainer) bf.getBean("container");
        Object scheduler = bf.getBean("quartz");
        Thread.sleep(10000);
        for (int i = 0; i < 100; i++)
        {
            assertNotNull(container.getNextCaptcha());
        }
    }

    public void testMockFillingDisk() throws Exception
    {
        Resource ressource = new ClassPathResource("testFillDiskMockQuartzBufferedEngine.xml");
        ConfigurableBeanFactory bf = new XmlBeanFactory(ressource);
        BufferedEngineContainer container = (BufferedEngineContainer) bf.getBean("container");
        Object scheduler = bf.getBean("quartz");
        Thread.sleep(100000);
    }

    /**
     * The buffer is never feed during the test (every 24h ...) the swap is every 3 second
     * 
     * @throws Exception
     */
    public void testExecuteNoBufferdedCaptcha() throws Exception
    {
        Resource ressource = new ClassPathResource("testNoBufferedCaptchaQuartzBufferedEngine.xml");
        ConfigurableBeanFactory bf = new XmlBeanFactory(ressource);
        BufferedEngineContainer container = (BufferedEngineContainer) bf.getBean("container");
        Object scheduler = bf.getBean("quartz");
        Thread.sleep(10000);
        for (int i = 0; i < 100; i++)
        {
            assertNotNull(container.getNextCaptcha());
        }
    }

}
