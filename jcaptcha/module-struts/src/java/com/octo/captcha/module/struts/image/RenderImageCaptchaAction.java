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
package com.octo.captcha.module.struts.image;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;

import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.module.struts.CaptchaServicePlugin;
import com.octo.captcha.module.config.CaptchaModuleConfigHelper;

/**
 * <p><ul><li></li></ul></p>
 *
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class RenderImageCaptchaAction extends Action {
    private Log log = LogFactory.getLog(RenderImageCaptchaAction.class);


    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
                                 HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws Exception {

     ImageCaptchaService service = (ImageCaptchaService)CaptchaServicePlugin.getInstance().getService();
     String captchaID = CaptchaModuleConfigHelper.getId(httpServletRequest);
        //(String) theRequest.getParameter(captchaIDParameterName);

        // call the ManageableImageCaptchaService methods
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            BufferedImage challenge =
                    service.getImageChallengeForID(captchaID, httpServletRequest.getLocale());
            // the output stream to render the captcha image as jpeg into

            // a jpeg encoder
            JPEGImageEncoder jpegEncoder =
                    JPEGCodec.createJPEGEncoder(jpegOutputStream);
            jpegEncoder.encode(challenge);
        } catch (IllegalArgumentException e) {
            // log a security warning and return a 404...
            if (log.isWarnEnabled())
            {
                log.warn(
                    "There was a try from "
                        + httpServletRequest.getRemoteAddr()
                        + " to render an URL without ID"
                        + " or with a too long one");
                httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                 log.error("should never pass here!");
            return actionMapping.findForward("error");
            }
        } catch (CaptchaServiceException e) {
            // log and return a 404 instead of an image...
            log.warn(
                "Error trying to generate a captcha and "
                    + "render its challenge as JPEG",
                e);
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            // log.error("should never pass here!");
            return actionMapping.findForward("error");
        }

        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

        // render the captcha challenge as a JPEG image in the response
        httpServletResponse.setHeader("Cache-Control","no-store");
        httpServletResponse.setHeader("Pragma","no-cache");
        httpServletResponse.setDateHeader ("Expires", 0);
       httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
       // log.error("should never pass here!");
        return null;
    }

}

