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

/**
 * Class of the exceptions that can be throwned by the ImageCaptchaService
 * @TODO : verify that the stack trace is correct (includes the detail message)
 * 
 * @version $Id$
 *
 * @author <a href="mailto:sebastien.brunot@club-internet.fr">Sebastien Brunot</a>
 */
public class ImageCaptchaServiceException extends Exception
{
    /////////////////////////////////
    // Constants
    /////////////////////////////////

    /**
     * The error details signaling that there are no
     * details about the error...
     */
    public static final int UNKNOWN_ERROR = 0;

    /**
     * The error details meaning that too many clients are using
     * the service and that it can't respond to the sollicitation.
     */
    public static final int TOO_MANY_USERS_ERROR = 1;

    /**
     * The error details meaning that a no captcha is associated
     * with the ID in the service internal store.
     * No captcha is associated with an ID if :
     * <ul>
     * <li> the generateCaptchaAndRenderChallengeAsJpeg method has
     * never been called with this ID </li>
     * <li> the generateCaptchaAndRenderChallengeAsJpeg method has
     * been previously called with this ID but the 
     * verifyResponseToCaptchaChallenged as been called after that
     * with the same ID  and the captcha was removed from the
     * service internal store </li>
     * <li> the generateCaptchaAndRenderChallengeAsJpeg method has
     * been previously called with this ID but it was more than
     * com.octo.captcha.j2ee.maxWaitInMilliseconds ago (initialization
     * parameter) and the captcha has been removed from the
     * service internal store because it was full and other users
     * were requesting captcha generation. </li>
     * </ul>
     */
    public static final int NO_CAPTCHA_WITH_THIS_ID_ERROR = 2;

    /**
     * The format of the string to use as the registering name for the
     * service does not correspond to a valid javax.management.ObjectName
     * @see javax.management.MalformedObjectNameException
     */
    public static final int MALFORMED_REGISTERING_NAME = 3;

    /**
     * The service is already registered to the MBean Server.
     */
    public static final int INSTANCE_ALREADY_REGISTERED = 4;

    /////////////////////////////////
    // Attributes
    /////////////////////////////////

    /**
     * An array used to store standard message (one message for each
     * error detail).
     */
    private static String[] standardMessages =
        new String[] {
            "Unknown error",
            "Too many client are using the service",
            "No captcha associated with this id in the service internal store",
            "The registering name is not a valid javax.management.ObjectName",
            "The service is already registered to the MBean server"};

    /**
     * a code that details the error cause (see constants)
     */
    private int detail = UNKNOWN_ERROR;

    /////////////////////////////////
    // Constructors
    /////////////////////////////////

    /**
     * Create a new ImageCaptchaServiceException providing a cause
     * and a detail code.
     * @param theCause the Throwable that cause this exception to be created
     * @param theDetail an error code providing details about the error
     */
    public ImageCaptchaServiceException(int theDetail, Throwable theCause)
    {
        super(theCause);
        this.detail = theDetail;
    }

    /**
     * Create a new ImageCaptchaServiceException providing a detail code.
     * @param theDetail an error code providing details about the error
     */
    public ImageCaptchaServiceException(int theDetail)
    {
        super();
        this.detail = theDetail;
    }

    /**
     * @see java.lang.Exception#Exception(java.lang.Throwable)
     */
    public ImageCaptchaServiceException(Throwable theCause)
    {
        super(theCause);
    }

    /////////////////////////////////
    // Public methods
    /////////////////////////////////

    /**
     * Get the error detail code.
     * @return the error detail code.
     */
    public int getDetail()
    {
        return this.detail;
    }

    /**
     * @see java.lang.Exception#getMessage()
     */
    public String getMessage()
    {
        String message = standardMessages[UNKNOWN_ERROR];
        try
        {
            message = standardMessages[this.detail];
        }
        catch (Exception e)
        {
            // nothing to be done : the UNKNOWN ERROR message
            // will be returned
        }
        return message;
    }
}
