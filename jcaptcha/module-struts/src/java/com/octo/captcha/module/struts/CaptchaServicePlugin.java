package com.octo.captcha.module.struts;

import org.apache.struts.action.PlugIn;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.servlet.ServletException;


import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.service.AbstractManageableCaptchaServiceMBean;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.module.config.CaptchaModuleConfig;
import com.octo.captcha.module.jmx.JMXRegistrationHelper;

import java.util.ResourceBundle;

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

/**
 * <p><ul><li></li></ul></p>
 *
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class CaptchaServicePlugin implements PlugIn {

    private static CaptchaServicePlugin instance;

    public static CaptchaServicePlugin getInstance(){
        return instance;
    }

    private static CaptchaService service;
    private Log log = LogFactory.getLog(CaptchaServicePlugin.class);
    private ActionServlet servlet;


    public CaptchaService getService() {
            return service;
        }

    private CaptchaModuleConfig captchaModuleConfig;


    //~ Methods ================================================================

    public CaptchaServicePlugin(){
        captchaModuleConfig = CaptchaModuleConfig.getInstance();
    }

    public void init(ActionServlet servlet, ModuleConfig config)
    throws ServletException {
        instance = this;

        if (log.isDebugEnabled()) {
            log.debug("Starting struts-captcha plugin initialization");
        }

        this.servlet = servlet;

        //validate configuration
        captchaModuleConfig.validate();

         // create the CaptchaService
        try {
            service = (CaptchaService) Class.forName(captchaModuleConfig.getServiceClass()).newInstance();
        } catch (InstantiationException e) {
            log.error("Error during Service Class initialization", e);

            throw new CaptchaServiceException(e);
        } catch (IllegalAccessException e) {
            log.error("Error during Service Class initialization", e);

            throw new CaptchaServiceException(e);
        } catch (ClassNotFoundException e) {
            log.error("Error during Service Class initialization", e);
            throw new CaptchaServiceException(e);
        }



        // register the CaptchaService to an MBean server if specified
        if (captchaModuleConfig.getRegisterToMbean().booleanValue()&&service instanceof AbstractManageableCaptchaServiceMBean) {
                AbstractManageableCaptchaServiceMBean manageable = (AbstractManageableCaptchaServiceMBean) service;
                JMXRegistrationHelper.registerToMBeanServer(manageable,CaptchaModuleConfig.JMX_REGISTERING_NAME);
            }
            if (log.isDebugEnabled()) {
                log.debug("struts-captcha plugin initialization successfull");
            }
    }

    public void destroy() {
        if (service instanceof AbstractManageableCaptchaServiceMBean&&captchaModuleConfig.getRegisterToMbean().booleanValue()) {
                    JMXRegistrationHelper.unregisterFromMBeanServer(CaptchaModuleConfig.JMX_REGISTERING_NAME);
                }

    }

    //*******
    //delegate to module config
    //*****

    public String getIdKey() {
        return captchaModuleConfig.getIdKey();
    }

    public void setIdKey(String idKey) {
        captchaModuleConfig.setIdKey(idKey);
    }

    public String getMessageType() {
        return captchaModuleConfig.getMessageType();
    }

    public void setMessageType(String messageType) {
        captchaModuleConfig.setMessageType(messageType);
    }

    public String getMessageValue() {
        return captchaModuleConfig.getMessageValue();
    }

    public void setMessageValue(String messageValue) {
        captchaModuleConfig.setMessageValue(messageValue);
    }

    public String getMessageKey() {
        return captchaModuleConfig.getMessageKey();
    }

    public void setMessageKey(String messageKey) {
        captchaModuleConfig.setMessageKey(messageKey);
    }

    public String getIdType() {
        return captchaModuleConfig.getIdType();
    }

    public void setIdType(String idType) {
        captchaModuleConfig.setIdType(idType);
    }

    public String getServiceClass() {
        return captchaModuleConfig.getServiceClass();
    }

    public void setServiceClass(String serviceClass) {
        captchaModuleConfig.setServiceClass(serviceClass);
    }

    public String getResponseKey() {
        return captchaModuleConfig.getResponseKey();
    }

    public void setResponseKey(String responseKey) {
        captchaModuleConfig.setResponseKey(responseKey);
    }

    public Boolean getRegisterToMbean() {
        return captchaModuleConfig.getRegisterToMbean();
    }

    public void setRegisterToMbean(Boolean registerToMbean) {
        captchaModuleConfig.setRegisterToMbean(registerToMbean);
    }



}
