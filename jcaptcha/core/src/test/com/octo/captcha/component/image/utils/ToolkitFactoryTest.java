package com.octo.captcha.component.image.utils;

import junit.framework.TestCase;

import java.awt.*;

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
