package com.octo.captcha.image.utils;

import java.awt.Toolkit;

import junit.framework.TestCase;
import com.octo.captcha.image.utils.ToolkitFactory;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class ToolkitFactoryTest extends TestCase {

    /**
     * Constructor for ToolkitFactoryTest.
     * @param name
     */
    public ToolkitFactoryTest(String name) {
        super(name);
    }

    public void testGetToolkit() {
        assertTrue(ToolkitFactory.getToolkit() instanceof Toolkit);
    }

}
