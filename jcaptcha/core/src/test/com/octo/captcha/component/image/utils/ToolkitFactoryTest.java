package com.octo.captcha.component.image.utils;

import junit.framework.TestCase;

import java.awt.*;

/**
 * <p>Description: </p>
 *
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class ToolkitFactoryTest extends TestCase {


    protected void setUp() throws Exception {
        super.setUp();

    }

    /**
     * Constructor for ToolkitFactoryTest.
     */
    public ToolkitFactoryTest(String name) {
        super(name);
    }

    public void testGetaDefaultToolkit() {
        assertTrue(ToolkitFactory.getToolkit() instanceof Toolkit);
    }

    public void testGetCustomToolkit() {
        System.setProperty(ToolkitFactory.TOOLKIT_IMPL, MockToolkit.class.getName());
        assertTrue(ToolkitFactory.getToolkit() instanceof MockToolkit);
        System.setProperty(ToolkitFactory.TOOLKIT_IMPL, "toto");
        try {
            ToolkitFactory.getToolkit();
            fail("should throw an exception");
        } catch (Exception e) {
            //assertTrue();
        }

    }

    protected void tearDown() throws Exception {
        super.tearDown();
        System.setProperty(ToolkitFactory.TOOLKIT_IMPL, Toolkit.getDefaultToolkit().getClass().toString());

    }
}
