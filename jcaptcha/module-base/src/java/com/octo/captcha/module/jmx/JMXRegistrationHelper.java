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
 */
package com.octo.captcha.module.jmx;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.AbstractManageableCaptchaServiceMBean;

import javax.management.*;
import java.util.ArrayList;

/**
 * Helper that providdes methods to register and unregister a ManageableCaptchaService to a MBean Server.
 *
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class JMXRegistrationHelper {
   /**
     * Register self to the first MBean server available in the JVM, if
     * any.
     *
     * @param name the name the service will be registered
     *                           to the MBean server.
     * @throws com.octo.captcha.service.CaptchaServiceException in case of error. Possible
     *                                 error details are :
     *                                 <ul>
     *                                 <li> CaptchaServiceException</li>
     *                                 </ul>
     * @see com.octo.captcha.service.CaptchaServiceException
     */
    public static void registerToMBeanServer(AbstractManageableCaptchaServiceMBean service, String name)
            throws CaptchaServiceException {
        if(name==null) throw new CaptchaServiceException("Service registration name can't be null");
        ArrayList mbeanServers = MBeanServerFactory.findMBeanServer(null);
        if (mbeanServers.size() == 0) {
            throw new CaptchaServiceException("No current MBean Server, skiping the registering process");
        } else {
            MBeanServer mbeanServer = (MBeanServer) mbeanServers.get(0);
            try {
                ObjectName objectName = new ObjectName(name);
                mbeanServer.registerMBean(service, objectName);
            } catch (MalformedObjectNameException e) {
                throw new CaptchaServiceException(e);
            } catch (InstanceAlreadyExistsException e) {
                throw new CaptchaServiceException(e);
            } catch (MBeanRegistrationException e) {
                // this exception should never be raised (raised
                // only by an MBean that implements the MBeanRegistration
                // interface.
                throw new CaptchaServiceException("An unexpected exception has been raised : "
                        + "CaptchaService needs maintenance !",
                        e);
            } catch (NotCompliantMBeanException e) {
                // this should never happens
                throw new CaptchaServiceException("Exception trying to register the service to"
                        + " the MBean server",
                        e);
            }
        }
    }

    /**
     * Unregister self from the first MBean server available in the JVM, if any
     */
    public static void unregisterFromMBeanServer(String name) {
        if (name != null) {
            ArrayList mbeanServers = MBeanServerFactory.findMBeanServer(null);
            MBeanServer mbeanServer = (MBeanServer) mbeanServers.get(0);
            try {
                ObjectName objectName = new ObjectName(name);
                mbeanServer.unregisterMBean(objectName);
            } catch (MalformedObjectNameException e) {
                // this should never happens
                throw new CaptchaServiceException("Exception trying to create the object name under witch"
                        + " the service is registered",
                        e);
            } catch (InstanceNotFoundException e) {
                // this should never happens
                throw new CaptchaServiceException("Exception trying to unregister the ImageCaptchaFilter from"
                        + " the MBean server",
                        e);
            } catch (MBeanRegistrationException e) {
                // this remains silent for the client
                throw new CaptchaServiceException("Exception trying to unregister the ImageCaptchaFilter from"
                        + "the MBean server",
                        e);
            }
        }else{
            throw new CaptchaServiceException("Service registration name can't be null");
        }
    }



}
