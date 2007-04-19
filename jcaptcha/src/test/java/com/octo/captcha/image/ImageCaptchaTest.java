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
package com.octo.captcha.image;


import com.octo.captcha.component.image.wordtoimage.SimpleWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.DummyWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.image.gimpy.GimpyFactory;
import junit.framework.TestCase;

import java.awt.image.BufferedImage;

/**
 * <p>Description: </p>
 *
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class ImageCaptchaTest extends TestCase {
    private ImageCaptcha pixCaptcha;


    /**
     * Constructor for ImageCaptchaTest.
     */
    public ImageCaptchaTest(String name) {
        super(name);
    }

    /**
     * this method is for initialisation for all the test cases
     */
    public void setUp() throws Exception {
        super.setUp();
        WordGenerator words = new DummyWordGenerator("TESTING");
        WordToImage word2image = new SimpleWordToImage();
        ImageCaptchaFactory factory = new GimpyFactory(words, word2image);
        pixCaptcha = factory.getImageCaptcha();
    }

    /**
     * This test is for verifying if the question of the captcha is correctly instantiated.
     */
    public void testGetQuestion() {
        assertNotNull(pixCaptcha.getQuestion());
    }

    /**
     * This test is for verifying if the challenge of the captcha is correctly instantiated.
     */
    public void testGetChallenge() {
        assertNotNull(pixCaptcha.getChallenge());
        assertTrue("Captcha challenge is not a BufferedImage", pixCaptcha.getImageChallenge() instanceof BufferedImage);
    }

    /**
     * This test is for verifying if the response of the captcha is valid.
     */
//    public void testValidateResponse() {
//        assertTrue(pixCaptcha.validateResponse(pixCaptcha.getResponse()).booleanValue());
//    }
    public void testGetImageChallenge() throws Exception {
        assertEquals(pixCaptcha.getImageChallenge(), pixCaptcha.getChallenge());
    }

    /**
     * This test is for verifying if the question of the captcha
     * is correctly instantiated.
     */
//    public void testGetResponse() {
//        assertNotNull(pixCaptcha.getResponse());
//    }
}
