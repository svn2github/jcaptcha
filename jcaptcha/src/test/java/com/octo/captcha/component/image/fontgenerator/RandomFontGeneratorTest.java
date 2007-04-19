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


package com.octo.captcha.component.image.fontgenerator;

import junit.framework.TestCase;

import java.awt.*;

/**
 * <p>Description: </p>
 *
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class RandomFontGeneratorTest extends TestCase {

    private RandomFontGenerator randomFontGenerator;

    private RandomFontGenerator randomFontGeneratorWithList;

    /**
     * Constructor for RandomFontGeneratorTest.
     */
    public RandomFontGeneratorTest(String name) {
        super(name);
    }

    public void setUp() {
        this.randomFontGenerator =
                new RandomFontGenerator(new Integer(10), new Integer(10));

        Font[] fontsList = new Font[2];
        fontsList[0] = new Font("Courier", Font.BOLD, 10);
        fontsList[1] = new Font("Arial", Font.BOLD, 10);

        this.randomFontGeneratorWithList =
                new RandomFontGenerator(new Integer(10), new Integer(10), fontsList);
    }

    public void testGetFont() {
        Font test = this.randomFontGenerator.getFont();
        assertNotNull(test);
    }

    public void testGetFontWithList() {
        Font test = this.randomFontGeneratorWithList.getFont();
        assertNotNull(test);
        assertTrue(test.getName().startsWith("Arial"));
    }


    public void testGetFontWithEmptyList() {
        Font[] fontsList = new Font[0];
        try {
            new RandomFontGenerator(new Integer(10), new Integer(10), fontsList);

            fail("should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            new RandomFontGenerator(new Integer(10), new Integer(10), null);
            fail("should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }


    public void testGetFontWithBadFontList() {
        Font[] fontsList = new Font[1];
        fontsList[0] = new Font("Courier", Font.BOLD, 10);

        try {
            new RandomFontGenerator(new Integer(10), new Integer(10), fontsList);
            fail("should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {

        }


    }

}
