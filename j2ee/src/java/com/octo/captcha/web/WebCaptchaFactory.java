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
package com.octo.captcha.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Factory class to get WebCaptcha implementations.
 *
 * @version $Id$
 *
 * @author <a href="mailto:sbr@octo.com">Sebastien Brunot</a>
 */
public class WebCaptchaFactory
{
    ////////////////////////////////////
    // Class attributes
    ////////////////////////////////////

    /**
     * Log class (commons-logging)
     */
    private static Log log = LogFactory.getLog(WebCaptchaFactory.class);

    /**
     * Name of the properties file that can be used by the user to change the
     * default WebCaptcha implementation returned by this factory
     */
    private static final String CONFIG_FILE = "captchaFactory.properties";

    /**
     * key under which the default WebCaptcha implementation class returned
     * by this factory can be specified by the user un CONFIG_FILE
     */
    private static final String CONFIG_FILE_CAPTCHA_IMPL_CLASS_KEY =
        "webCaptchaDefaultImplementation";

    /**
     * Default captcha implementation used by the factory.
     */
    private static String webCaptchaDefaultImplementation =
        "com.octo.captcha.web.impl.TestGimpy";

    ////////////////////////////////////
    // Static initialization
    ////////////////////////////////////

    static {
        // read the implementation class from a config file if one
        // is provided
        ClassLoader cl = WebCaptchaFactory.class.getClassLoader();
        InputStream is = null;
        try
        {
            Properties props = new Properties();
            is = cl.getResourceAsStream(CONFIG_FILE);
            if (is != null)
            {
                // @TODO : FIX THE BUG : there's an exception here if no
                // config file is provided
                props.load(is);
                if (props.containsKey(CONFIG_FILE_CAPTCHA_IMPL_CLASS_KEY))
                {
                    webCaptchaDefaultImplementation =
                        props.getProperty(CONFIG_FILE_CAPTCHA_IMPL_CLASS_KEY);
                }
            }
        }
        catch (Exception e)
        {
            // if there is an exception, log it
            log.warn(
                "An error occured during static initialization of the class : ["
                    + webCaptchaDefaultImplementation
                    + "] Captcha implementation will be used by the factory",
                e);
        }
        finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                }
                catch (IOException e)
                {
                    log.warn(
                        "An exception occured closing an InputStream to the"
                            + "CaptchaFactory configuration file",
                        e);
                }
            }
        }
    }

    ////////////////////////////////////
    // Constructors
    ////////////////////////////////////
    /**
     * A protected constructor that does nothing.
     */
    protected WebCaptchaFactory()
    {
    }

    ////////////////////////////////////
    // Public methods
    ////////////////////////////////////

    /**
     * Get a WebCaptcha implementation.
     *
     * @param theClassName the name of the implementation. If className is null,
     * the default implementation is returned. The default implementation used
     * by this method is a com.octo.captcha.impl.RandomStringCaptcha,
     * but another implementation class can be declared in a file named
     * captchaFactory.properties, with the line
     * "implementationClass=my.captcha.implementation" in it.
     * The captchaFactory.properties file should be in the classpath.
     * @return a WebCaptcha
     * @throws WebCaptchaFactoryException if an error occurend instantiating the
     *         WebCaptcha implementation.
     */
    public static WebCaptcha getWebCaptcha(final String theClassName)
        throws WebCaptchaFactoryException
    {
        try
        {
            if (theClassName == null)
            {
                return (WebCaptcha) Class
                    .forName(webCaptchaDefaultImplementation)
                    .newInstance();
            }
            else
            {
                return (WebCaptcha) Class.forName(theClassName).newInstance();
            }
        }
        catch (Exception e)
        {
            throw new WebCaptchaFactoryException(e);
        }
    }

    /**
     * Get the class name of the default WebCaptcha implementation returned
     * by this factory.
     * @return the class name
     */
    public static String getWebCaptchaDefaultImplementation()
    {
        return webCaptchaDefaultImplementation;
    }
}
