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
package com.octo.captcha.j2ee.servlet;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.octo.captcha.j2ee.ImageCaptchaService;
import com.octo.captcha.j2ee.ImageCaptchaServiceException;
import com.octo.utils.FilterConfigUtils;

/**
 * ImageCaptchaFilter is a J2EE Filter designed to add image captchas to the
 * entry forms of existing MVC web applications. 
 * <br>
 * Current features of ImageCaptchaFilter are:
 * <ul>
 *   <li> Generation and rendering of Captcha images (as JPEG) at runtime for
 *        inclusion in an existing entry form. ImageCaptchaFilter use a
 *        ImageCaptchaEngine to generate captchas : a simple one
 *        (com.octo.captcha.image.gimpy.MultipleGimpyEngine, which displays a
 *         random string composed with characters A,B,C,D and E) is provided
 *         with jcaptcha-j2ee, but you can code your own or use one of those
 *         provided with jcaptcha-sample ;
 *   </li>
 *   <li> Verification of the HTTP client entry in response to the challenge
 *        displayed as an image in the entry form: Redirection of the HTTP
 *        client to an error page if the captcha challenge is not passed ;
 *        Transparent follow up of the request to the web application, without
 *        any information concerning the captcha, if the challenge is
 *        successfully passed ;
 *        Many forms can be protected by captchas in the same web application,
 *        each one having its own error redirection page ;
 *   </li>
 * </ul>
 * The JMX Management interface for ImageCaptchaFilter is PixCaptchaFilterMbean.
 * 
 * @version $Id$
 *
 * @author <a href="mailto:sbr@octo.com">Sebastien Brunot</a>
 */
public class ImageCaptchaFilter implements Filter
{
    ////////////////////////////////////
    // Constants
    ////////////////////////////////////

    /**
     * Name under witch the CaptchaFilter is registered to an MBean server
     */
    public static final String JMX_REGISTERING_NAME =
        "com.octo.captcha.j2ee.servlet:object=ImageCaptchaFilter";

    /**
     * The delimiter used to specify values as CSV in a string in web.xml
     * (used for verification URLs, etc...)
     */
    public static final String CSV_DELIMITER = ";";

    ////////////////////////////////////
    // Private attributes
    ////////////////////////////////////

    /**
     * Logger (commons-logging)
     */
    private static Log log = LogFactory.getLog(ImageCaptchaFilter.class);

    /**
     * The ImageCaptchaService internaly used by the filter
     */
    private ImageCaptchaService captchaService = null;

    /**
     * The URL that commands a new captcha creation and its rendering as
     * a jpg image (filter parameter to define in web.xml)
     */
    private String captchaRenderingURL = null;

    /**
     * The name of the filter parameter in web.xml for captchaRenderingURL
     */
    private static final String CAPTCHA_RENDERING_URL_PARAMETER =
        "CaptchaRenderingURL";

    /**
     * The name of the filter parameter in web.xml for the CSV list of URLs
     * that commands the captcha verification
     */
    private static final String CAPTCHA_VERIFICATION_URLS_PARAMETER =
        "CaptchaVerificationURLs";

    /**
     * The name of the filter parameter in web.xml for the CSV list of URLs
     * (one for each verification URL) to which the request should be forwarded,
     * after cleaning captchaID and the captchaChallengeResponse from it, if the
     * challengeResponse is uncorrect
     * (filter parameter to define in web.xml)
     */
    private static final String CAPTCHA_ERROR_URLS_PARAMETER =
        "CaptchaErrorURLs";

    /**
     * A hashmap that contains information about forwardError URL for a
     * verification URL. The verificationURL is the key, the stored object
     * is a String (the URL to forward to if there is a verification error).
     * (this map is initialized with filter parameters values defined in
     *  web.xml)
     */
    private Hashtable verificationForwards = new Hashtable();

    /**
     * The name of the request parameter that contains the captcha ID
     * (filter parameter to define in web.xml)
     */
    private String captchaIDParameterName = null;

    /**
     * The name of the filter parameter in web.xml for captchaIDParameterName
     */
    private static final String CAPTCHA_ID_PARAMETER_NAME_PARAMETER =
        "CaptchaIDParameterName";

    /**
     * The name of the filter parameter in web.xml for captchaIDMaxLength
     */
    private static final String CAPTCHA_ID_MAX_LENGTH_PARAMETER =
        "CaptchaIDMaxLength";

    /**
     * The name of the request parameter that contains the challenge response
     * to match
     * (filter parameter to define in web.xml)
     */
    private String captchaChallengeResponseParameterName = null;

    /**
     * The name of the filter parameter in web.xml for
     * captchaChallengeResponseParameterName
     */
    private static final String CAPTCHA_RESPONSE_PARAMETER_NAME_PARAMETER =
        "CaptchaChallengeResponseParameterName";

    /**
     * The name of the filter parameter in web.xml for the
     * internal ImageCaptcha Service 
     * ImageCaptchaService.MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP
     * initialization parameter.
     * @see com.octo.captcha.j2ee.ImageCaptchaService#MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP
     */
    private static final String MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP =
        "MaxNumberOfSimultaneousCaptchas";

    /**
     * The name of the filter parameter in web.xml for the
     * internal ImageCaptchaService
     * ImageCaptchaService.MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP
     * initialization parameter.
     * @see com.octo.captcha.j2ee.ImageCaptchaService#MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP
     */
    private static final String MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP =
        "MinGuarantedStorageDelayInSeconds";

    /**
     * A boolean that signal if the CaptchaFilter should be registered to
     * the MBean Server in the Application Server (default value is false)
     * (filter parameter to define in web.xml)
     */
    private boolean captchaRegisterToMBeanServer = false;

    /**
     * The name of the filter parameter in web.xml for
     * captchaRegisterToMBeanServer
     */
    private static final String CAPTCHA_REGISTER_TO_MBEAN_SERVER_PARAMETER =
        "RegisterToMBeanServer";

    /**
     * The servlet context asociated with the filter
     */
    private ServletContext servletContext = null;

    /**
     * The name of the filter parameter in web.xml for the
     * internal ImageCaptchaService
     * ImageCaptchaService.ENGINE_CLASS_INIT_PARAMETER_PROP
     * initialization parameter.
     * @see com.octo.captcha.j2ee.ImageCaptchaService#ENGINE_CLASS_INIT_PARAMETER_PROP
     */
    private static final String CAPTCHA_ENGINE_CLASS_PARAMETER =
        "ImageCaptchaEngineClass";

    ////////////////////////////////////
    // Filter implementation
    ////////////////////////////////////

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     * @TODO : verify that URL begins with a "\" (or add it and trace
     * a warning) ?
     */
    public void init(final FilterConfig theFilterConfig)
        throws ServletException
    {

        // get associated servlet context
        this.servletContext = theFilterConfig.getServletContext();

        // get rendering URL from web.xml
        this.captchaRenderingURL =
            FilterConfigUtils.getStringInitParameter(
                theFilterConfig,
                CAPTCHA_RENDERING_URL_PARAMETER,
                true);

        // get verification URLs from web.xml (CSV list of URLs)
        String captchaVerificationURLs =
            FilterConfigUtils.getStringInitParameter(
                theFilterConfig,
                CAPTCHA_VERIFICATION_URLS_PARAMETER,
                true);

        // get forward error URLs from web.xml (CSV list of URLs)
        String captchaForwardErrorURLs =
            FilterConfigUtils.getStringInitParameter(
                theFilterConfig,
                CAPTCHA_ERROR_URLS_PARAMETER,
                true);

        // initialize the verificationForwards hashtable
        StringTokenizer verificationURLs =
            new StringTokenizer(captchaVerificationURLs, CSV_DELIMITER, false);
        StringTokenizer forwardErrorURLs =
            new StringTokenizer(captchaForwardErrorURLs, CSV_DELIMITER, false);
        if (verificationURLs.countTokens() != forwardErrorURLs.countTokens())
        {
            // The URL lists are not consistant (there should be a forward and
            // a success for each verification URL)
            throw new ServletException(
                CAPTCHA_VERIFICATION_URLS_PARAMETER
                    + " and "
                    + CAPTCHA_ERROR_URLS_PARAMETER
                    + " values are not consistant in web.xml : there should be"
                    + " exactly one forward error for each verification URL !");
        }
        while (verificationURLs.hasMoreTokens())
        {
            // Create a ForwardInfo for each verification URL and store it in
            // the verificationForward hashtable
            this.verificationForwards.put(
                verificationURLs.nextToken(),
                forwardErrorURLs.nextToken());
        }

        // get captcha ID parameter name from web.xml
        this.captchaIDParameterName =
            FilterConfigUtils.getStringInitParameter(
                theFilterConfig,
                CAPTCHA_ID_PARAMETER_NAME_PARAMETER,
                true);

        // get challenge response parameter name from web.xml
        this.captchaChallengeResponseParameterName =
            FilterConfigUtils.getStringInitParameter(
                theFilterConfig,
                CAPTCHA_RESPONSE_PARAMETER_NAME_PARAMETER,
                true);

        // get from web.xml the indicator signaling if the CaptchaFilter
        // should be registered to an MBean Server
        this.captchaRegisterToMBeanServer =
            FilterConfigUtils.getBooleanInitParameter(
                theFilterConfig,
                CAPTCHA_REGISTER_TO_MBEAN_SERVER_PARAMETER,
                false);

        // Extract the ImageCaptchaService initialization parameters
        // from web.xml
        Properties captchaServiceInitParameters = new Properties();
        {
            // get max number of simultaneous captchas from web.xml
            String captchaInternalStoreSize =
                FilterConfigUtils.getStringInitParameter(
                    theFilterConfig,
                    MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP,
                    true);
            captchaServiceInitParameters.setProperty(
                ImageCaptchaService.MAX_NUMBER_OF_SIMULTANEOUS_CAPTCHAS_PROP,
                captchaInternalStoreSize);
            // get minimum guaranted storage delay in seconds from web.xml
            String captchaTimeToLive =
                FilterConfigUtils.getStringInitParameter(
                    theFilterConfig,
                    MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP,
                    true);
            captchaServiceInitParameters.setProperty(
                ImageCaptchaService.MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS_PROP,
                captchaTimeToLive);
            // get from web.xml the ImageCaptchaEngine implementation class name
            String engineClass =
                FilterConfigUtils.getStringInitParameter(
                    theFilterConfig,
                    CAPTCHA_ENGINE_CLASS_PARAMETER,
                    true);
            captchaServiceInitParameters.setProperty(
                ImageCaptchaService.ENGINE_CLASS_INIT_PARAMETER_PROP,
                engineClass);
        }

        // create the ImageCaptchaService
        this.captchaService =
            new ImageCaptchaService(captchaServiceInitParameters);

        // get captcha ID max length from web.xml and set the
        // ImageCaptchaService captcha ID max length value with it
        Integer captchaIDMaxMLengthAsInteger =
            FilterConfigUtils.getIntegerInitParameter(
                theFilterConfig,
                CAPTCHA_ID_MAX_LENGTH_PARAMETER,
                true,
                0,
                Integer.MAX_VALUE);
        this.captchaService.setCaptchaIDMaxLength(
            captchaIDMaxMLengthAsInteger.intValue());

        // register the ImageCaptchaService to an MBean server if specified
        if (this.captchaRegisterToMBeanServer)
        {
            registerToMBeanServer();
        }
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *                                    javax.servlet.ServletResponse,
     *                                    javax.servlet.FilterChain)
     */
    public void doFilter(
        final ServletRequest theRequest,
        final ServletResponse theResponse,
        final FilterChain theFilterChain)
        throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) theRequest;
        HttpServletResponse response = (HttpServletResponse) theResponse;

        // Get the URL the user asked for
        String servletPathInfo = request.getServletPath();

        if (servletPathInfo.equals(this.captchaRenderingURL))
        {
            // This is the URL used to ask for captcha generation : do it !
            this.generateAndRenderCaptcha(request, response);
        }
        else if (this.verificationForwards.containsKey(servletPathInfo))
        {
            // This is the URL used to ask for captcha challenge verification :
            // do it !
            this.verifyAnswerToACaptchaChallenge(
                request,
                response,
                servletPathInfo,
                theFilterChain);
        }
        else
        {
            // follow to the filter chain...
            theFilterChain.doFilter(theRequest, theResponse);
        }
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    public void destroy()
    {
        // Unregister ImageCaptchaService from the JMX MBean server
        unregisterFromMBeanServer();
    }

    ////////////////////////////////////
    // Private methods
    ////////////////////////////////////

    /**
     * Register the internal ImageCaptchaService to a JMX MBean Server, if any
     */
    private void registerToMBeanServer()
    {
        try
        {
            this.captchaService.registerToMBeanServer(JMX_REGISTERING_NAME);
        }
        catch (ImageCaptchaServiceException e)
        {
            log.error(
                "Exception trying to create the object name to"
                    + " register the ImageCaptchaFilter under",
                e);
        }
    }

    /**
     * Unregister the internal ImageCaptchaService from an MBean server
     */
    private void unregisterFromMBeanServer()
    {
        this.captchaService.unregisterFromMBeanServer();
    }

    /**
     * Generate a new ImageCaptcha, store it in the internal store and
     * render it as JPEG to the client.
     * This method returns a 404 to the client instead of the image if
     * the request isn't correct (missing parameters, etc...).
     *
     * @param theRequest the request
     * @param theResponse the response
     * @throws IOException @TODO : DOCUMENT ME !
     * @throws ServletException @TODO : DOCUMENT ME  !
     */
    private void generateAndRenderCaptcha(
        HttpServletRequest theRequest,
        HttpServletResponse theResponse)
        throws IOException, ServletException
    {
        // get captcha ID from the request
        String captchaID = theRequest.getParameter(captchaIDParameterName);

        // call the ImageCaptchaService methods
        byte[] captchaChallengeAsJpeg = null;
        try
        {
            captchaChallengeAsJpeg =
                this.captchaService.generateCaptchaAndRenderChallengeAsJpeg(
                    captchaID);
        }
        catch (IllegalArgumentException e)
        {
            // log a security warning and return a 404...
            if (log.isWarnEnabled())
            {
                log.warn(
                    "There was a try from "
                        + theRequest.getRemoteAddr()
                        + " to render an URL without ID"
                        + " or with a too long one");
                theResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }
        catch (ImageCaptchaServiceException e)
        {
            // log and return a 404 instead of an image...
            log.warn(
                "Error trying to generate a captcha and "
                    + "render its challenge as JPEG",
                e);
            theResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // render the captcha challenge as a JPEG image in the response
        theResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
            theResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
    }

    /**
     * Verify client answer to a captcha challenge, and forward to the
     * correponding success or error URL.
     * This method returns a 404 to the client instead of forwarding if
     * the request isn't correct (missing parameters, etc...).
     * @param theRequest the request
     * @param theResponse the response
     * @param theVerificationURL the verification URL that was called
     * @param theFilterChain DOCUMENT ME !
     * @throws IOException @TODO : DOCUMENT ME !
     * @throws ServletException @TODO : DOCUMENT ME !
     */
    private void verifyAnswerToACaptchaChallenge(
        HttpServletRequest theRequest,
        HttpServletResponse theResponse,
        String theVerificationURL,
        FilterChain theFilterChain)
        throws IOException, ServletException
    {
        // get captcha ID from the request
        String captchaID = theRequest.getParameter(captchaIDParameterName);

        // get challenge response from the request
        String challengeResponse =
            theRequest.getParameter(captchaChallengeResponseParameterName);
        if (challengeResponse == null)
        {
            // If the challenge response is not specified, forward error
            this.redirectError(theVerificationURL, theRequest, theResponse);
            return;
        }

        // Call the ImageCaptchaService method
        boolean isResponseCorrect = false;
        try
        {
            isResponseCorrect =
                this.captchaService.verifyResponseToACaptchaChallenge(
                    captchaID,
                    challengeResponse);
        }
        catch (ImageCaptchaServiceException e)
        {
            // nothing to do : isResponseCorrect is false
            // so the user will be redirected to the error page
        }

        // forward user to the success URL or redirect it to the error URL
        if (isResponseCorrect)
        {
            // clean the request and call the next element in filter chain
            // (forward success)
            this.forwardSuccess(theFilterChain, theRequest, theResponse);
        }
        else
        {
            // forward
            this.redirectError(theVerificationURL, theRequest, theResponse);
        }
    }

    /**
     * Redirect request to the Error URL
     * @param theVerificationURL the verification URL for which there is an
     * error redirect
     * @param theRequest the request
     * @param theResponse the response
     * @throws ServletException @TODO : DOCUMENT ME !
     * @see javax.servlet.RequestDispatcher#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    private void redirectError(
        String theVerificationURL,
        HttpServletRequest theRequest,
        HttpServletResponse theResponse)
        throws ServletException
    {
        this.removeParametersFromRequest(theRequest);
        try
        {
            // Redirect to the error URL
            String forwardErrorURL =
                theRequest.getContextPath()
                + (String) this.verificationForwards.get(theVerificationURL);
            theResponse.sendRedirect(forwardErrorURL);
        }
        catch (IOException e)
        {
            throw new ServletException(e);
        }
    }

    /**
     * Clean the request and call the next element in filter chain 
     * @param theFilterChain the filter chain
     * @param theRequest the request
     * @param theResponse the response
     * @throws ServletException @TODO : DOCUMENT ME !
     * @TODO : the see is uncorrect !!!
     * @see javax.servlet.RequestDispatcher#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    private void forwardSuccess(
        FilterChain theFilterChain,
        HttpServletRequest theRequest,
        HttpServletResponse theResponse)
        throws ServletException
    {
        this.removeParametersFromRequest(theRequest);
        try
        {
            theFilterChain.doFilter(theRequest, theResponse);
        }
        catch (IOException e)
        {
            throw new ServletException(e);
        }
    }

    /**
     * Remove captcha filter specific parameters from a request
     * @param theRequest the request
     */
    private void removeParametersFromRequest(HttpServletRequest theRequest)
    {
        theRequest.removeAttribute(CAPTCHA_RESPONSE_PARAMETER_NAME_PARAMETER);
        theRequest.removeAttribute(CAPTCHA_ID_PARAMETER_NAME_PARAMETER);
    }
}
