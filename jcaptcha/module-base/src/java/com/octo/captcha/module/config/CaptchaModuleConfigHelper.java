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

import javax.servlet.http.HttpServletRequest;
import java.util.ResourceBundle;

/**
 * Class that provides static utility method to use the Module configuration.
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class CaptchaModuleConfigHelper {

/**
 * method that get an id using the plugin configuration.<br/>
 * It may be retrieved from the session via the getId() native method
 * if the idType is set to 'session'
 * or retrieved from the request using the 'idKey' parameter if the idType is set to 'generated';
 * @param httpServletRequest
 * @return
 */
     public static String getId(HttpServletRequest httpServletRequest) {
        String captchaID;
        boolean generatedId = CaptchaModuleConfig.getInstance().getIdType().equals(CaptchaModuleConfig.ID_GENERATED)?
                      true:false;

        if(generatedId){
            //get it from the request
            captchaID = httpServletRequest.getParameter(CaptchaModuleConfig.getInstance().getIdKey());

        }else{
        //get captcha ID from the session!!
        captchaID = httpServletRequest.getSession().getId();//theRequest.getParameter(captchaIDParameterName);
        }
        return captchaID;
    }

    /***
     * Method that return the fail or error message from the specified bundle or directly from the value specified
     */

    public static String getMessage(HttpServletRequest httpServletRequest){
        String message=null;
        boolean messageBundle =
                CaptchaModuleConfig.getInstance().getMessageType().equals(CaptchaModuleConfig.MESSAGE_TYPE_BUNDLE)?
                true:false;
        //get it from the bundle with the specified locale
        if(messageBundle){
            ResourceBundle bundle=ResourceBundle.getBundle(CaptchaModuleConfig.getInstance().getMessageValue(),
                    httpServletRequest.getLocale());
            if(bundle!=null){
                message = bundle.getString(CaptchaModuleConfig.getInstance().getMessageKey());
            }
            //get it with no locale if still null
            if(message==null){
                bundle = ResourceBundle.getBundle(CaptchaModuleConfig.getInstance().getMessageValue());
                message = bundle.getString(CaptchaModuleConfig.getInstance().getMessageKey());
            }

        }else{
            //directly get the value
            message = CaptchaModuleConfig.getInstance().getMessageValue();
        }
    return message;
    }

}
