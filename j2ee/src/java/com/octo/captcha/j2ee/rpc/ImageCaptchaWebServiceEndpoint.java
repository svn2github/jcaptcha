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
package com.octo.captcha.j2ee.rpc;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.octo.captcha.j2ee.ImageCaptchaService;
import com.octo.captcha.j2ee.ImageCaptchaServiceException;

/**
 * @TODO : DOCUMENT ME !
 *
 * @version $Id$
 *
 * @author <a href="mailto:sebastien.brunot@club-internet.fr">Sebastien Brunot</a>
 */
public class ImageCaptchaWebServiceEndpoint implements Remote
{
    /////////////////////////////
    // Private attributes
    /////////////////////////////

    /**
     * The ImageCaptchaService encapsulated by this
     * Web Service
     */
    private ImageCaptchaService imageCaptchaService = new ImageCaptchaService();

    /**
     * The logger (commons-logging)
     */
    private static Log log =
        LogFactory.getLog(ImageCaptchaWebServiceEndpoint.class);

    /////////////////////////////
    // Constructor
    /////////////////////////////

    /**
     * @TODO : MAKE REGISTERING TO MBEAN SERVER AN OPTION !
     */
    public ImageCaptchaWebServiceEndpoint()
    {
        try
        {
            this.imageCaptchaService.registerToMBeanServer(
                "com.octo.captcha.j2ee:object=ImageCaptchaService");
        }
        catch (ImageCaptchaServiceException e)
        {
            log.warn(
                "Unable to register the internal service to an MBean server : ",
                e);
        }
    }

    /////////////////////////////
    // Finalizer
    /////////////////////////////

    /**
     * @TODO : WHAT IS THE LIFECYCLE OF THE SERVICE ? WHEN TO UNREGISTER ?
     */
    public void finalize()
    {
        this.imageCaptchaService.unregisterFromMBeanServer();
    }

    /////////////////////////////
    // Public methods
    /////////////////////////////

    /**
     * @see com.octo.captcha.j2ee.ImageCaptchaService#generateCaptchaAndRenderChallengeAsJpeg(java.lang.String)
     *
     * @param theCaptchaID a unique ID for the requested captcha
     * @return an Jpeg image
     * @throws RemoteException with a generic message if the
     * an exception occured during method execution. An error log
     * is also provided for the service endpoint operator.
     */
    public byte[] generateCaptchaAndRenderChallengeAsJpeg(String theCaptchaID)
        throws RemoteException
    {
        byte[] captchaJpeg = null;
        try
        {
            captchaJpeg =
                this
                    .imageCaptchaService
                    .generateCaptchaAndRenderChallengeAsJpeg(
                    theCaptchaID);
        }
        catch (ImageCaptchaServiceException e)
        {
            // log a message to the service endpoint operator
            log.error(e.getMessage());
            // returns a generic exception to the end user
            throw new RemoteException(
                "Service is temporarily unavailable."
                    + " Please try again later.");
        }
        catch (IllegalArgumentException e)
        {
            // route the exception to the end user
            throw new RemoteException(e.getMessage());
        }

        return captchaJpeg;
    }

    /**
     * @see com.octo.captcha.j2ee.ImageCaptchaService#verifyResponseToACaptchaChallenge(java.lang.String, java.lang.String)
     *
     * @param theCaptchaID the unique ID associated with the captcha
     * @param theResponse the client response to the captcha challenge
     * @return true is the response is correct, false otherwise
     * @throws RemoteException @TODO DOCUMENT ME !
     */
    public boolean verifyResponseToACaptchaChallenge(
        String theCaptchaID,
        String theResponse)
        throws RemoteException
    {
        boolean returnValue = false;
        try
        {
            returnValue =
                this.imageCaptchaService.verifyResponseToACaptchaChallenge(
                    theCaptchaID,
                    theResponse);
        }
        catch (ImageCaptchaServiceException e)
        {
            if (e.getDetail()
                == ImageCaptchaServiceException.NO_CAPTCHA_WITH_THIS_ID_ERROR)
            {
                // route the exception to the end user
                throw new RemoteException(e.getMessage());
            }
            else
            {
                // log a message to the service endpoint operator
                log.error(e.getMessage());
                // returns a generic exception to the end user
                throw new RemoteException(
                    "Service is temporarily unavailable."
                        + " Please try again later.");
            }
        }
        return returnValue;
    }
}
