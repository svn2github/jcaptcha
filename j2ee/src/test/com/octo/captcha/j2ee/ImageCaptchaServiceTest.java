/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 *
 */
package com.octo.captcha.j2ee;

import java.util.Locale;
import java.util.Properties;

import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.ImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptchaFactory;

import junit.framework.TestCase;

/**
 * @TODO : DOCUMENT ME !
 *
 * @version $Id$
 *
 * @author <a href="mailto:sbr@octo.com">Sebastien Brunot</a>
 */
public class ImageCaptchaServiceTest extends TestCase
{
    ////////////////////////////
    // Fixtures
    ////////////////////////////

    /**
     * A nested ImageCaptchaEngine class without
     * a default constructor
     */
    public class DoomyImageCaptchaEngine extends ImageCaptchaEngine
    {

        /**
         * @see com.octo.captcha.image.ImageCaptchaEngine#getImageCaptchaFactory()
         */
        public ImageCaptchaFactory getImageCaptchaFactory()
        {
            return null;
        }

        /**
         * @see com.octo.captcha.image.ImageCaptchaEngine#getNextImageCaptcha()
         */
        public ImageCaptcha getNextImageCaptcha()
        {
            return null;
        }

        /**
         * @see com.octo.captcha.image.ImageCaptchaEngine#getNextImageCaptcha(java.util.Locale)
         */
        public ImageCaptcha getNextImageCaptcha(Locale theLocale)
        {
            return null;
        }

    }

    /**
     * Default initialization values
     */
    private Properties initializationValues;

    /**
     * Default value for ENGINE_CLASS_INIT_PARAMETER_PROP
     */
    private static final String ENGINE_CLASS_INIT_PARAMETER_PROP =
        "com.octo.captcha.image.gimpy.SimpleGimpyEngine";

    /**
     * Default value for MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP
     */
    private static final String MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP =
        "1234";

    /**
     * Default value for MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP
     */
    private static final String MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP =
        "120";

    ////////////////////////////
    // Constructors
    ////////////////////////////

    /**
     * Constructor for ImageCaptchaServiceTest.
     * @param theName the name of the test case
     */
    public ImageCaptchaServiceTest(String theName)
    {
        super(theName);
        this.initializationValues = new Properties();
        this.initializationValues.put(
            ImageCaptchaService.MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP,
            MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP);
        this.initializationValues.put(
            ImageCaptchaService.MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP,
            MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP);
        this.initializationValues.put(
            ImageCaptchaService.ENGINE_CLASS_INIT_PARAMETER_PROP,
            ENGINE_CLASS_INIT_PARAMETER_PROP);
    }

    /////////////////////////////
    // setUp / tearDown
    /////////////////////////////

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /////////////////////////////
    // Unit tests
    /////////////////////////////

    /**
     * Test for null ImageCaptchaService(Properties)
     */
    public void testImageCaptchaServicePropertiesNull()
    {
        try
        {
            ImageCaptchaService testedService = new ImageCaptchaService(null);
            fail("A runtime exception should have been thrown !");
        }
        catch (RuntimeException e)
        {
            assertTrue(true);
        }
    }

    /**
     * Test for empty ImageCaptchaService(Properties)
     */
    public void testImageCaptchaServicePropertiesEmpty()
    {
        try
        {
            Properties emptyProperties = new Properties();
            ImageCaptchaService testedService =
                new ImageCaptchaService(emptyProperties);
            fail("A runtime exception should have been thrown !");
        }
        catch (RuntimeException e)
        {
            assertTrue(true);
        }
    }

    /**
     * Test for initialization value MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP
     * missing in the property parameter of ImageCaptchaService(Properties)
     */
    public void testImageCaptchaServicePropertiesMaxNbOfSimCaptchasMissing()
    {
        try
        {
            this.initializationValues.remove(
                ImageCaptchaService.MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP);
            ImageCaptchaService testedService =
                new ImageCaptchaService(this.initializationValues);
            fail("A runtime exception should have been thrown !");
        }
        catch (RuntimeException e)
        {
            assertTrue(true);
        }
    }

    /**
     * Test for initialization value MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP
     * missing in the property parameter of ImageCaptchaService(Properties)
     */
    public void testImageCaptchaServicePropertiesMinStorageDelayMissing()
    {
        try
        {
            this.initializationValues.remove(
                ImageCaptchaService
                    .MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP);
            ImageCaptchaService testedService =
                new ImageCaptchaService(this.initializationValues);
            fail("A runtime exception should have been thrown !");
        }
        catch (RuntimeException e)
        {
            assertTrue(true);
        }
    }

    /**
     * Test for initialization value ENGINE_CLASS_INIT_PARAMETER_PROP
     * missing in the property parameter of ImageCaptchaService(Properties)
     */
    public void testImageCaptchaServicePropertiesEngineClassMissing()
    {
        try
        {
            this.initializationValues.remove(
                ImageCaptchaService.ENGINE_CLASS_INIT_PARAMETER_PROP);
            ImageCaptchaService testedService =
                new ImageCaptchaService(this.initializationValues);
            fail("A runtime exception should have been thrown !");
        }
        catch (RuntimeException e)
        {
            assertTrue(true);
        }
    }

    /**
     * Test for initialization value MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP
     * beeing not an integer in the property parameter of 
     * ImageCaptchaService(Properties)
     */
    public void testImageCaptchaServicePropertiesMaxNbOfSimCaptchasNotInteger()
    {
        try
        {
            this.initializationValues.put(
                ImageCaptchaService.MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP,
                "foo");
            ImageCaptchaService testedService =
                new ImageCaptchaService(this.initializationValues);
            fail("A runtime exception should have been thrown !");
        }
        catch (RuntimeException e)
        {
            assertTrue(true);
        }
    }

    /**
     * Test for initialization value MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP
     * not beeing an integer in the property parameter of
     * ImageCaptchaService(Properties)
     */
    public void testImageCaptchaServicePropertiesMinStorageDelayNotInteger()
    {
        try
        {
            this.initializationValues.put(
                ImageCaptchaService.MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP,
                "foo");
            ImageCaptchaService testedService =
                new ImageCaptchaService(this.initializationValues);
            fail("A runtime exception should have been thrown !");
        }
        catch (RuntimeException e)
        {
            assertTrue(true);
        }
    }

    /**
     * Test for initialization value MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP
     * beeing the maximum integer in the property parameter of
     * ImageCaptchaService(Properties)
     */
    public void testImageCaptchaServicePropertiesMinStorageDelayMaxInteger()
    {
        this.initializationValues.put(
            ImageCaptchaService.MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP,
            "" + Integer.MAX_VALUE);
        ImageCaptchaService testedService =
            new ImageCaptchaService(this.initializationValues);
        assertEquals(
            "MinGuarantedStorageDelayInSeconds is uncorrect !",
            testedService.getMinGuarantedStorageDelayInSeconds(),
            Integer.MAX_VALUE);
        assertEquals(
            "MaxNumberOfSimultaneousCaptchas is uncorrect !",
            testedService.getMaxNumberOfSimultaneousCaptchas(),
            new Integer(MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP).intValue());
        assertEquals(
            "ImageCaptchaEngineClass is uncorrect !",
            testedService.getImageCaptchaEngineClass(),
            ENGINE_CLASS_INIT_PARAMETER_PROP);
    }

    /**
     * Test for initialization value ENGINE_CLASS_INIT_PARAMETER_PROP
     * unknown in the property parameter of ImageCaptchaService(Properties)
     */
    public void testImageCaptchaServicePropertiesEngineClassUnknown()
    {
        try
        {
            this.initializationValues.put(
                ImageCaptchaService.ENGINE_CLASS_INIT_PARAMETER_PROP,
                "foo");
            ImageCaptchaService testedService =
                new ImageCaptchaService(this.initializationValues);
            fail("A runtime exception should have been thrown !");
        }
        catch (RuntimeException e)
        {
            assertTrue(true);
        }
    }

    /**
     * Test for initialization value ENGINE_CLASS_INIT_PARAMETER_PROP
     * which is a class without no arg constructor
     * in the property parameter of ImageCaptchaService(Properties)
     */
    public void testImageCaptchaServicePropertiesEngineClassWithoutNoArgConst()
    {
        try
        {
            this.initializationValues.put(
                ImageCaptchaService.ENGINE_CLASS_INIT_PARAMETER_PROP,
                DoomyImageCaptchaEngine.class.getName());
            ImageCaptchaService testedService =
                new ImageCaptchaService(this.initializationValues);
            fail("A runtime exception should have been thrown !");
        }
        catch (RuntimeException e)
        {
            assertTrue(true);
        }
    }

}
