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
package com.octo.captcha.module.config;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.module.CaptchaModuleException;

import java.util.ResourceBundle;

/**
 * <p><ul><li></li></ul></p>
 *
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class CaptchaModuleConfig {


    private static CaptchaModuleConfig instance = new CaptchaModuleConfig();

    public static CaptchaModuleConfig getInstance(){
        return instance;
    }

    public static final String MESSAGE_TYPE_BUNDLE = "bundle";
    public static final String ID_GENERATED = "generated";
    public static final String MESSAGE_TYPE_TEXT = "text";
    public static final String ID_SESSION = "session";
    public static final String JMX_REGISTERING_NAME =
        "com.octo.captcha.module.struts:object=CaptchaServicePlugin";


    private CaptchaModuleConfig(){
    }

    private Boolean registerToMbean = Boolean.FALSE;
    private String responseKey = "jcaptcha_response";
    private String serviceClass;
    private String messageType = com.octo.captcha.module.config.CaptchaModuleConfig.MESSAGE_TYPE_TEXT;
    private String messageValue = "You failed the jcaptcha test";
    private String messageKey = "jcaptcha_fail";
    private String idType = com.octo.captcha.module.config.CaptchaModuleConfig.ID_SESSION;
    private String idKey = "jcaptcha_id";

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageValue() {
        return messageValue;
    }

    public void setMessageValue(String messageValue) {
        this.messageValue = messageValue;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }


    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getResponseKey() {
         return responseKey;
     }

     public void setResponseKey(String responseKey) {
         this.responseKey = responseKey;
     }

    public Boolean getRegisterToMbean() {
        return registerToMbean;
    }

    public void setRegisterToMbean(Boolean registerToMbean) {
        this.registerToMbean = registerToMbean;
    }



    public void validate(){

        //verify values
        if(!(com.octo.captcha.module.config.CaptchaModuleConfig.MESSAGE_TYPE_TEXT.equals(messageType)||com.octo.captcha.module.config.CaptchaModuleConfig.MESSAGE_TYPE_BUNDLE.equals(messageType)))throw new com.octo.captcha.service.CaptchaServiceException("messageType can " +
                "only be set to '"+com.octo.captcha.module.config.CaptchaModuleConfig.MESSAGE_TYPE_TEXT+"' or '"+com.octo.captcha.module.config.CaptchaModuleConfig.MESSAGE_TYPE_BUNDLE+"'");

        if(!(com.octo.captcha.module.config.CaptchaModuleConfig.ID_SESSION.equals(idType)||com.octo.captcha.module.config.CaptchaModuleConfig.ID_GENERATED.equals(idType)))throw new com.octo.captcha.service.CaptchaServiceException("idType can " +
                        "only be set to '"+com.octo.captcha.module.config.CaptchaModuleConfig.ID_SESSION+"' or '"+com.octo.captcha.module.config.CaptchaModuleConfig.ID_GENERATED+"'");

        if(messageValue==null)throw new CaptchaModuleException("messageValue cannot be null");

        if(messageKey==null||"".equals(messageKey))throw new CaptchaModuleException(
                "messageKey cannot be null or empty");

        if(responseKey==null||"".equals(responseKey))throw new CaptchaModuleException(
                "responseKey cannot be null or empty");

         if((idType.equals(com.octo.captcha.module.config.CaptchaModuleConfig.ID_GENERATED))&&(idKey==null||"".equals(idKey)))throw new com.octo.captcha.service.CaptchaServiceException(
                 "idKey cannot be null or empty when id is generated (ie idType='"+com.octo.captcha.module.config.CaptchaModuleConfig.ID_GENERATED+"'");


        //if message is in a bundle, try to load it
        if(this.messageType.equals(com.octo.captcha.module.config.CaptchaModuleConfig.MESSAGE_TYPE_BUNDLE)){
            ResourceBundle bundle=ResourceBundle.getBundle(getMessageValue());
            if(bundle==null){
                throw new CaptchaModuleException("can't initialize module config with a unfound bundle : "
                +"resource bundle "+getMessageValue()+ " has  not been found");
            }else{
                if(bundle.getString(getMessageKey())==null){
                    throw new CaptchaModuleException("can't initialize module config with a unfound message : "
                    +"resource bundle "+getMessageValue()+ " has  no key named :"+getMessageKey());
                }
            }

        }

        // try to create the CaptchaService
          try {
              CaptchaService service = (CaptchaService) Class.forName(serviceClass).newInstance();
          } catch (InstantiationException e) {
              throw new CaptchaModuleException("Error during Service Class initialization", e);
          } catch (IllegalAccessException e) {
              throw new CaptchaModuleException("Error during Service Class initialization", e);
          } catch (ClassNotFoundException e) {
              throw new CaptchaModuleException("Error during Service Class initialization", e);
          }


    }
}
