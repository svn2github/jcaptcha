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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
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

import com.octo.captcha.pix.PixCaptcha;
import com.octo.captcha.pix.PixCaptchaEngine;
import com.octo.utils.ConstantCapacityHashtable;
import com.octo.utils.ConstantCapacityHashtableFullException;
import com.octo.utils.FilterConfigUtils;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * PixCaptchaFilter is a J2EE Filter designed to add pix captchas to the entry
 * forms of existing MVC web applications. 
 * <br>
 * Current features of PixCaptchaFilter are:
 * <ul>
 *   <li> Generation and rendering of Captcha images (as JPEG) at runtime for
 *        inclusion in an existing entry form. PixCaptchaFilter use a
 *        PixCaptchaEngine to generate captchas : a simple one 
 *        (com.octo.captcha.pix.gimpy.impl.SimpleGimpyEngine, which displays a
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
 * The JMX Management interface for PixCaptchaFilter is PixCaptchaFilterMbean.
 * 
 * @version $Id$
 *
 * @author <a href="mailto:sbr@octo.com">Sebastien Brunot</a>
 */
public class PixCaptchaFilter implements Filter, PixCaptchaFilterMBean
{
    ////////////////////////////////////
    // Constants
    ////////////////////////////////////

    /**
     * Name under witch the CaptchaFilter is registered to an MBean server
     */
    public static final String JMX_REGISTERING_NAME =
        "com.octo.captcha.j2ee.servlet:object=PixCaptchaFilter";

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
    private static Log log = LogFactory.getLog(PixCaptchaFilter.class);

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
     * The maximum length for the captcha ID, so that one can't try to attack
     * the system by requesting captcha generation providing heavy keys
     * (filter parameter to define in web.xml)
     */
    private int captchaIDMaxLength = 0;

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
     * The size of the internal store
     * @TODO : add more documentation about this !!!
     * (filter parameter to define in web.xml)
     */
    private int captchaInternalStoreSize = 0;

    /**
     * The name of the filter parameter in web.xml for captchaInternalStoreSize
     */
    private static final String CAPTCHA_INTERNAL_STORE_SIZE_PARAMETER =
        "CaptchaInternalStoreSize";

    /**
     * The time to live of a captcha in the internal store
     * before it is garbage collected
     * (filter parameter to define in web.xml)
     */
    private int captchaTimeToLive = 0;

    /**
     * The name of the filter parameter in web.xml for captchaTimeToLive
     */
    private static final String CAPTCHA_TIME_TO_LIVE_PARAMETER =
        "CaptchaTimeToLive";

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
     * A ConstantCapacityHashtable used to store and check generated captcha
     */
    private ConstantCapacityHashtable internalStore = null;

    /**
     * Engine used by the filter to generate captchas (the concrete
     * implementation class must be specified in web.xml)
     */
    private PixCaptchaEngine engine = null;

    /**
     * The name of the filter parameter in web.xml for
     * the class name of the engine used by the filter
     * to generate captchas
     */
    private static final String CAPTCHA_ENGINE_CLASS_PARAMETER =
        "PixCaptchaEngineClass";

    /**
     * Was the filter successfully registered to a JMX MBean server ?
     */
    private boolean registeredToMBeanServer = false;

    /**
     * Number of captcha generated since the filter is up
     */
    private long totalNumberOfGeneratedCaptcha = 0;

    /**
     * Number of captcha correctly answered since the filter is up
     */
    private long totalNumberOfCaptchaCorrectlyAnswered = 0;

    /**
     * Number of captcha badly answered since the filter is up
     */
    private long totalNumberOfCaptchaBadlyAnswered = 0;

    /**
     * Number of captcha garbage collected since the filter is up
     */
    private long totalNumberOfGarbageCollectedCaptcha = 0;

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
                    + " exactly one forward success and one forward error for"
                    + " each verification URL !");
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

        // get captcha ID max length from web.xml
        Integer captchaIDMaxMLengthAsInteger =
            FilterConfigUtils.getIntegerInitParameter(
                theFilterConfig,
                CAPTCHA_ID_MAX_LENGTH_PARAMETER,
                true,
                0,
                Integer.MAX_VALUE);
        if (captchaIDMaxMLengthAsInteger != null)
        {
            this.captchaIDMaxLength = captchaIDMaxMLengthAsInteger.intValue();
        }

        // get challenge response parameter name from web.xml
        this.captchaChallengeResponseParameterName =
            FilterConfigUtils.getStringInitParameter(
                theFilterConfig,
                CAPTCHA_RESPONSE_PARAMETER_NAME_PARAMETER,
                true);

        // get internal store size from web.xml
        Integer captchaInternalStoreSizeAsInteger =
            FilterConfigUtils.getIntegerInitParameter(
                theFilterConfig,
                CAPTCHA_INTERNAL_STORE_SIZE_PARAMETER,
                true,
                0,
                Integer.MAX_VALUE);
        if (captchaInternalStoreSizeAsInteger != null)
        {
            this.captchaInternalStoreSize =
                captchaInternalStoreSizeAsInteger.intValue();
        }

        // get captcha time to live from web.xml
        Integer captchaTimeToLiveAsInteger =
            FilterConfigUtils.getIntegerInitParameter(
                theFilterConfig,
                CAPTCHA_TIME_TO_LIVE_PARAMETER,
                true,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE);
        if (captchaTimeToLiveAsInteger != null)
        {
            this.captchaTimeToLive = captchaTimeToLiveAsInteger.intValue();
        }

        // get from web.xml the indicator signaling if the CaptchaFilter
        // should be registered to an MBean Server
        this.captchaRegisterToMBeanServer =
            FilterConfigUtils.getBooleanInitParameter(
                theFilterConfig,
                CAPTCHA_REGISTER_TO_MBEAN_SERVER_PARAMETER,
                false);

        // get from web.xml the PixCaptchaEngine implementation class name
        String engineClass =
            FilterConfigUtils.getStringInitParameter(
                theFilterConfig,
                CAPTCHA_ENGINE_CLASS_PARAMETER,
                true);

        // create the engine
        try
        {
            this.engine =
                (PixCaptchaEngine) Class.forName(engineClass).newInstance();
        }
        catch (Exception e)
        {
            throw new ServletException(
                "Error trying to instanciate the engine (class "
                    + engineClass
                    + ")",
                e);
        }

        // get associated servlet context
        this.servletContext = theFilterConfig.getServletContext();

        // init internal store
        this.internalStore =
            new ConstantCapacityHashtable(
                this.captchaInternalStoreSize,
                this.captchaTimeToLive);

        if (this.captchaRegisterToMBeanServer)
        {
            // try to register to the current JMX MBeanServer, if any
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

        if (log.isDebugEnabled())
        {
            log.debug("requestedURL = " + servletPathInfo);
            log.debug("captchaRenderingURL = " + this.captchaRenderingURL);
            // @TODO : there's an error here ?
            log.debug("captchaVerificationURL = " + this.captchaRenderingURL);
        }

        if (servletPathInfo.equals(this.captchaRenderingURL))
        {
            // This is the URL used to ask for captcha generation : do it !
            this.generateAndRenderCaptcha(request, response);
        }
        else if (this.verificationForwards.containsKey(servletPathInfo))
        {
            // This is an URL used to ask for captcha challenge verification :
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
        if (this.registeredToMBeanServer)
        {
            // Unregister from the JMX MBean server
            unregisterFromMBeanServer();
        }
    }

    ////////////////////////////////////
    // Private methods
    ////////////////////////////////////

    /**
     * Register this to a JMX MBean Server, if any
     */
    private void registerToMBeanServer()
    {
        ArrayList mbeanServers = MBeanServerFactory.findMBeanServer(null);
        if (mbeanServers.size() == 0)
        {
            log.warn(
                "No current MBean Server, skiping the registering process");
        }
        else
        {
            MBeanServer mbeanServer = (MBeanServer) mbeanServers.get(0);
            try
            {
                ObjectName name = new ObjectName(JMX_REGISTERING_NAME);
                mbeanServer.registerMBean(this, name);
                this.registeredToMBeanServer = true;
            }
            catch (MalformedObjectNameException e)
            {
                log.error(
                    "Exception trying to create the object name to"
                        + " register the CaptchaFilter under",
                    e);
            }
            catch (InstanceAlreadyExistsException e)
            {
                log.error(
                    "Exception trying to register the CaptchaFilter to"
                        + " the MBean server",
                    e);
            }
            catch (MBeanRegistrationException e)
            {
                log.error(
                    "Exception trying to register the CaptchaFilter to"
                        + " the MBean server",
                    e);
            }
            catch (NotCompliantMBeanException e)
            {
                log.error(
                    "Exception trying to register the CaptchaFilter to"
                        + " the MBean server",
                    e);
            }
        }
    }

    /**
     * Unregister from an MBean server
     */
    private void unregisterFromMBeanServer()
    {
        ArrayList mbeanServers = MBeanServerFactory.findMBeanServer(null);
        MBeanServer mbeanServer = (MBeanServer) mbeanServers.get(0);
        try
        {
            ObjectName name = new ObjectName(JMX_REGISTERING_NAME);
            mbeanServer.unregisterMBean(name);
        }
        catch (MalformedObjectNameException e)
        {
            log.error(
                "Exception trying to create the object name under witch"
                    + " the internal store is registered",
                e);
        }
        catch (InstanceNotFoundException e)
        {
            log.error(
                "Exception trying to unregister the CaptchaFilter from"
                    + " the MBean server",
                e);
        }
        catch (MBeanRegistrationException e)
        {
            log.error(
                "Exception trying to unregister the CaptchaFilter from"
                    + "the MBean server",
                e);
        }
    }

    /**
     * Generate a new PixCaptcha and render it as JPEG to the client.
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
        if ((captchaID == null)
            || (captchaID.length() > this.captchaIDMaxLength))
        {
            // If the captcha ID is not specified or is too long, return
            // a 404 error
            if (log.isWarnEnabled())
            {
                log.warn(
                    "There was a try from "
                        + theRequest.getRemoteAddr()
                        + " to render an URL without ID"
                        + " or with a too long one");
            }
            theResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // create a new captcha and associate it with the captcha ID
        PixCaptcha captcha = null;
        captcha =
            this.engine.getPixCaptchaFactory().getPixCaptcha(
                theRequest.getLocale());
        try
        {
            this.internalStore.put(captchaID, captcha);
        }
        catch (ConstantCapacityHashtableFullException e)
        {
            // log and return a 404 instead of an image...
            log.warn(
                "Get an error trying to store a captcha in"
                    + " the internal store : ",
                e);
            theResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // render the captcha in the response as a JPEG image
        theResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
            theResponse.getOutputStream();
        BufferedImage image = captcha.getPixChallenge();
        JPEGImageEncoder jpegEncoder =
            JPEGCodec.createJPEGEncoder(responseOutputStream);
        jpegEncoder.encode(image);
        // dispose of the captcha image
        captcha.disposeChallenge();

        // update the total number of generated captcha
        totalNumberOfGeneratedCaptcha += 1;
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
        if ((captchaID == null)
            || (captchaID.length() > this.captchaIDMaxLength))
        {
            // If the captcha ID is not specified or is too long,
            // return a 404 error
            if (log.isWarnEnabled())
            {
                log.warn(
                    "There was a try from "
                        + theRequest.getRemoteAddr()
                        + " to render an URL without ID"
                        + " or with a too long one");
            }
            theResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // get challenge response from the request
        String challengeResponse =
            theRequest.getParameter(captchaChallengeResponseParameterName);
        if (challengeResponse == null)
        {
            // If the challenge response is not specified, forward error
            this.redirectError(theVerificationURL, theRequest, theResponse);
            return;
        }

        // get the captcha from internal store and remove it from there
        PixCaptcha captcha = (PixCaptcha) this.internalStore.get(captchaID);
        this.internalStore.remove(captchaID);

        // if captcha is null, it means that captcha ID is a fake one or that
        // the timeout is expired and there was a run of the garbage collector
        // -> forward error
        if (captcha == null)
        {
            this.redirectError(theVerificationURL, theRequest, theResponse);
            return;
        }

        // verify challenge response and forward to the corresponding URL
        if (captcha.validateResponse(challengeResponse).booleanValue())
        {
            // update the total number of captcha correctly answered
            totalNumberOfCaptchaCorrectlyAnswered += 1;
            // clean the request and call the next element in filter chain
            // (forward success)
            this.forwardSuccess(theFilterChain, theRequest, theResponse);
        }
        else
        {
            // update the total number of captcha badly answered
            totalNumberOfCaptchaBadlyAnswered += 1;
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
     * @see javax.servlet.RequestDispatcher#forward(javax.servlet.http.HttpServletRequest,
     *                                  javax.servlet.http.HttpServletResponse)
     *      for details on params
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
     * @see javax.servlet.RequestDispatcher#forward(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     *      for details on params
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

    ////////////////////////////////////
    // Management interface implementation
    ////////////////////////////////////

    /**
     * @see com.octo.captcha.j2ee.servlet.PixCaptchaFilterMBean#getPixCaptchaEngineClass()
     */
    public String getPixCaptchaEngineClass()
    {
        return this.engine.getClass().getName();
    }

    /**
     * @see com.octo.captcha.j2ee.servlet.PixCaptchaFilterMBean#setPixCaptchaEngineClass()
     */
    public void setPixCaptchaEngineClass(String theClassName)
        throws IllegalArgumentException
    {
        // try to use this concrete PixCaptchaEngine class as the
        // engine
        try
        {
            this.engine =
                (PixCaptchaEngine) Class.forName(theClassName).newInstance();
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * @see com.octo.captcha.j2ee.servlet.PixCaptchaFilterMBean#getInternalStoreCapacity()
     */
    public int getInternalStoreCapacity()
    {
        return this.captchaInternalStoreSize;
    }

    /**
     * @see com.octo.captcha.j2ee.servlet.PixCaptchaFilterMBean#getTimeToLive()
     */
    public int getTimeToLiveInMilliseconds()
    {
        return this.internalStore.getTimeToLive();
    }

    /**
     * @see com.octo.captcha.j2ee.servlet.PixCaptchaFilterMBean#setTimeToLive(int)
     */
    public void setTimeToLiveInMilliseconds(int theTimeToLive)
    {
        // store the new time to live
        this.captchaTimeToLive = theTimeToLive;
        this.internalStore.setTimeToLive(theTimeToLive);
    }

    /**
     * @see com.octo.captcha.j2ee.servlet.PixCaptchaFilterMBean#getInternalStoreLoad()
     */
    public double getInternalStoreLoad()
    {
        double internalStoreSizeAsDouble = (double) this.internalStore.size();
        double capacityAsDouble = (double) this.captchaInternalStoreSize;
        return ((internalStoreSizeAsDouble / capacityAsDouble) * 100.0);
    }

    /**
     * @see com.octo.captcha.j2ee.servlet.PixCaptchaFilterMBean#getNumberOfTimeoutedEntriesInInternalStore()
     */
    public int getNumberOfTimeoutedEntriesInInternalStore()
    {
        return this.internalStore.getNumberOfGarbageCollectableEntries();
    }

    /**
     * @see com.octo.captcha.j2ee.servlet.PixCaptchaFilterMBean#getTotalNumberOfGeneratedCaptcha()
     */
    public long getTotalNumberOfGeneratedCaptcha()
    {
        return this.totalNumberOfGeneratedCaptcha;
    }

    /**
     * @see com.octo.captcha.j2ee.servlet.PixCaptchaFilterMBean#getTotalNumberOfCaptchaCorrectlyAnswered()
     */
    public long getTotalNumberOfCaptchaCorrectlyAnswered()
    {
        return this.totalNumberOfCaptchaCorrectlyAnswered;
    }

    /**
     * @see com.octo.captcha.j2ee.servlet.PixCaptchaFilterMBean#getTotalNumberOfCaptchaBadlyAnswered()
     */
    public long getTotalNumberOfCaptchaBadlyAnswered()
    {
        return this.totalNumberOfCaptchaBadlyAnswered;
    }

    /**
     * @see com.octo.captcha.j2ee.servlet.PixCaptchaFilterMBean#getTotalNumberOfCaptchaGarbageCollected()
     */
    public long getTotalNumberOfGarbageCollectedCaptcha()
    {
        return this.totalNumberOfGarbageCollectedCaptcha;
    }

    /**
     * @see com.octo.captcha.j2ee.servlet.PixCaptchaFilterMBean#garbageCollectInternalStore()
     */
    public void garbageCollectInternalStore()
    {
        totalNumberOfGarbageCollectedCaptcha
            += this.internalStore.garbageCollect();
    }
}
