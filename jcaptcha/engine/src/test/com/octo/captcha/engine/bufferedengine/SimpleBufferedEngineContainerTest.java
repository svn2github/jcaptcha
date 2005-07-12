/*
 * Created on May 2, 2005
 *
 */
package com.octo.captcha.engine.bufferedengine;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import junit.framework.TestCase;

import java.util.Locale;

/**
 * @author NIDWMAG
 */
public class SimpleBufferedEngineContainerTest extends TestCase
{

    public void testExecute() throws Exception
    {
        Resource ressource = new ClassPathResource("testSimpleBufferedEngine.xml");
        ConfigurableBeanFactory bf = new XmlBeanFactory(ressource);
        BufferedEngineContainer container = (BufferedEngineContainer) bf.getBean("container");

        Thread.sleep(8000);
        for (int i = 0; i < 30; i++)
        {
            assertNotNull(container.getNextCaptcha(Locale.US));

        }
        
        Thread.sleep(4000);
        
        ((SimpleBufferedEngineContainer)container).stopDaemon();
    }
}
