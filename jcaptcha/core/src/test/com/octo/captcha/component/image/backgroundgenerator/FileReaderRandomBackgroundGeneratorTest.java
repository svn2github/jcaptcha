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

/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.component.image.backgroundgenerator;

import junit.framework.TestCase;

import java.io.File;

public class FileReaderRandomBackgroundGeneratorTest extends TestCase {
    FileReaderRandomBackgroundGenerator fileReaderRandomBackgroundGenerator;

    protected void setUp() throws Exception {
        super.setUp();
        fileReaderRandomBackgroundGenerator =
                new FileReaderRandomBackgroundGenerator(new Integer(2), new Integer(2), "imagedir");
    }

    public void testFindDirectory() throws Exception {

        File dir = fileReaderRandomBackgroundGenerator.findDirectory("com/octo");
        assertValidDir(dir, "octo");
        try {
            dir = fileReaderRandomBackgroundGenerator.findDirectory("does not exists");
            fail("should never pass");
        } catch (Exception e) {
            // should throw exception
        }
        dir = fileReaderRandomBackgroundGenerator.findDirectory("imagedir");
        assertValidDir(dir, "imagedir");
        dir = fileReaderRandomBackgroundGenerator.findDirectory("emptyimagedir");
        assertValidDir(dir, "emptyimagedir");
        try {
            new FileReaderRandomBackgroundGenerator(new Integer(2), new Integer(2), "emptyimagedir");
            fail("should never pass");
        } catch (Exception e) {
            // should throw exception
        }
    }


    /**
     * Requires that directory be a directory, be readable, and have the right name.
     */
    private void assertValidDir(File dir, String expectedName) {
        assertTrue("should be readable", dir.canRead());
        assertTrue("should be a directory", dir.canRead());
        assertEquals("Name of root path should match name of directory", expectedName, dir.getName());
    }

}