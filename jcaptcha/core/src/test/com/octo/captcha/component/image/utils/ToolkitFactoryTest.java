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

    /**
     * Constructor for ToolkitFactoryTest.
     *
     * @param name
     */
    public ToolkitFactoryTest(String name) {
        super(name);
    }

    public void testGetaDefaultToolkit() {
        assertTrue(ToolkitFactory.getToolkit() instanceof Toolkit);
    }

    public void testGetCustomToolkit(){
        System.setProperty(ToolkitFactory.TOOLKIT_IMPL,MockToolkit.class.getName());
        assertTrue(ToolkitFactory.getToolkit() instanceof MockToolkit);
        System.setProperty(ToolkitFactory.TOOLKIT_IMPL,"toto");
        try
        {
            ToolkitFactory.getToolkit();
            fail("should throw an exception");
        } catch (Exception e)
        {
            //assertTrue();
        }

    }
}
