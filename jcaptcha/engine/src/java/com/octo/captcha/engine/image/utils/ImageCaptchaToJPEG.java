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

package com.octo.captcha.engine.image.utils;

import com.octo.captcha.engine.image.ImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptcha;

import java.io.File;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class ImageCaptchaToJPEG
{

    public static void main(String[] args) throws Exception
    {
        System.out.println("args : gimpy class='" + args[0] + "', output dir='" + args[1] + "',iterations='" + args[2] + "'");
        ImageCaptchaEngine pixCapchaEngine = (ImageCaptchaEngine) Class.forName(args[0]).newInstance();
        System.out.println("gimpy initialized");
        int i;

        //ImageToFile encoder = new ImageToFile();
        System.out.println("Beginning generation");

        for (i = 0 ; i < Integer.parseInt(args[2]) ; i++)
        {
            ImageCaptcha captcha = pixCapchaEngine.getNextImageCaptcha();
            System.out.println("Captcha " + i + " retrieved");
            File out = new File(args[1] + File.separator + "captcha_" + i + ".jpg");
            ImageToFile.serialize(captcha.getImageChallenge(), out);
            System.out.println("File i created " + out.toURI());
        }

    }

}
