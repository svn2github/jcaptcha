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
package com.octo.captcha.engine.bufferedengine;


import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Doumas Benoit
 */
public class QuartzBufferedEngineContainerTest extends BufferedEngineContainerTestAbstract {


    private static final int WAIT_TIME = 10000;

	public void testBasic() throws Exception {
        Resource ressource = new ClassPathResource("testQuartzBufferedEngine.xml");
        ConfigurableBeanFactory bf = new XmlBeanFactory(ressource);
        BufferedEngineContainer container = (BufferedEngineContainer) bf.getBean("container");
        Object scheduler = bf.getBean("quartz");
        assertNotNull(scheduler);
        Thread.sleep(WAIT_TIME);
        for (int i = 0; i < 100; i++) {
            assertNotNull(container.getNextCaptcha());
        }
    }

    public void testMockFillingDisk() throws Exception {
        Resource ressource = new ClassPathResource("testFillDiskMockQuartzBufferedEngine.xml");
        ConfigurableBeanFactory bf = new XmlBeanFactory(ressource);
        BufferedEngineContainer container = (BufferedEngineContainer) bf.getBean("container");
        Object scheduler = bf.getBean("quartz");
        assertNotNull(container);
        assertNotNull(scheduler);
        Thread.sleep(100000);
        for (int i = 0; i < 100; i++) {
            assertNotNull(container.getNextCaptcha());
        }
    }

    /**
     * The buffer is never feed during the test (every 24h ...) the swap is every 3 second
     */
    public void testNoBufferdedCaptcha() throws Exception {
        Resource ressource = new ClassPathResource("testNoBufferedCaptchaQuartzBufferedEngine.xml");
        ConfigurableBeanFactory bf = new XmlBeanFactory(ressource);
        BufferedEngineContainer container = (BufferedEngineContainer) bf.getBean("container");
        Object scheduler = bf.getBean("quartz");
        assertNotNull(scheduler);
        assertNotNull(container);
        Thread.sleep(WAIT_TIME);
        for (int i = 0; i < 100; i++) {
            assertNotNull(container.getNextCaptcha());
        }
    }

    /**
     * @see com.octo.captcha.engine.bufferedengine.BufferedEngineContainerTestAbstract#getEngine()
     */
    public BufferedEngineContainer getEngine() {
        Resource ressource = new ClassPathResource("testQuartzBufferedEngine.xml");
        ConfigurableBeanFactory bf = new XmlBeanFactory(ressource);
        BufferedEngineContainer container = (BufferedEngineContainer) bf.getBean("container");
        Object scheduler = bf.getBean("quartz");
        assertNotNull(scheduler);
        return container;
    }

    /**
     * @see com.octo.captcha.engine.bufferedengine.BufferedEngineContainerTestAbstract#releaseEngine()
     */
    public void releaseEngine(BufferedEngineContainer engine) {

    }

}
