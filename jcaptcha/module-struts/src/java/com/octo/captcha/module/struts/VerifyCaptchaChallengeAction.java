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
package com.octo.captcha.module.struts;

import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.module.config.CaptchaModuleConfigHelper;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p><ul><li></li></ul></p>
 *
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class VerifyCaptchaChallengeAction extends Action {

    private Log log = LogFactory.getLog(VerifyCaptchaChallengeAction.class);

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        log.debug("enter captcha challenge verification");

        CaptchaService service = CaptchaServicePlugin.getInstance().getService();

        String responseKey = CaptchaServicePlugin.getInstance().getResponseKey();


        ActionErrors errors = new ActionErrors();

        String captchaID;

        captchaID = CaptchaModuleConfigHelper.getId(httpServletRequest);

        // get challenge response from the request
        String challengeResponse =
                httpServletRequest.getParameter(responseKey);

        if(log.isDebugEnabled())log.debug("response for id " +captchaID +" : "+challengeResponse);

        //cleanning the request
             httpServletRequest.removeAttribute(responseKey);

        Boolean isResponseCorrect = Boolean.FALSE;

        if (challengeResponse != null) {


            // Call the Service method
            try {
                isResponseCorrect = service.validateResponseForID(captchaID,
                        challengeResponse);
            } catch (CaptchaServiceException e) {

                log.debug("Error during challenge verification", e);
                // so the user will be redirected to the error page
                httpServletRequest.setAttribute(CaptchaServicePlugin.getInstance().getMessageKey(),
                CaptchaModuleConfigHelper.getMessage(httpServletRequest));

                log.debug("forward to error with message : "+CaptchaModuleConfigHelper.getMessage(httpServletRequest));

                return actionMapping.findForward("error");
            }
        }
        // forward user to the success URL or redirect it to the error URL
        if (isResponseCorrect.booleanValue()) {
            // clean the request and call the next action
            // (forward success)
            log.debug("correct : forward to success");
            return actionMapping.findForward("success");
        } else {
            if(log.isDebugEnabled()){
                log.debug("false  : forward to failure with message : "+CaptchaModuleConfigHelper.getMessage(httpServletRequest));
                log.debug("in request attribute key : "+CaptchaServicePlugin.getInstance().getMessageKey());
            }
            // If the challenge response is not specified, forward failure
            httpServletRequest.setAttribute(CaptchaServicePlugin.getInstance().getMessageKey(),
            CaptchaModuleConfigHelper.getMessage(httpServletRequest));
            return actionMapping.findForward("failure");
        }

    }



}
