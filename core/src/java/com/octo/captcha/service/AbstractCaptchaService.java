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

package com.octo.captcha.service;

import com.octo.captcha.CaptchaService;
import com.octo.captcha.CaptchaEngine;
import com.octo.captcha.CaptchaServiceException;
import com.octo.captcha.Captcha;

import java.util.Locale;

/**
 * This is a base class for CaptchaService implementations.
 * It implements the lyfe cycle stuff.
 * It uses  : a IDGenerator to generate ids, a CaptchaStore to store captcha during the life cycle,
 * and a CaptchaEngine to build captchas.
 *
 * @author Marc-Antoine Garrigue mailto:mag@octo.com
 */
public class AbstractCaptchaService implements CaptchaService
{

    private IDGenerator idGenerator;
    private CaptchaStore store;
    private CaptchaEngine engine;

    

    /**
     * Method to retrive a unique ticket,and building a corresponding captcha.
     * User can also use the localized method.
     * Uses the IDGenerator to get an id, the engine to build a captcha and the store to...store.
     *
     * @return a unique id
     */
    public final String getCaptchaID()
    {
        String id = idGenerator.getNextId();
        Captcha captcha = engine.getNextCaptcha();
        store.storeCaptcha(id,captcha);
        return id;
    }

    /**
     * Method to retrive a unique ticket.
     * Uses the IDGenerator to get an id, the engine to build a captcha and the store to...store.
     * @param locale
     * @return a unique id
     */
    public final String getCaptchaID(Locale locale)
    {
        String id = idGenerator.getNextId();
        Captcha captcha = engine.getNextCaptcha(locale);
        store.storeCaptcha(id,captcha);
        return id;
    }

    /**
     * Method to retrive the challenge corresponding to the given ticket from the store.
     * @param ID the ticket provided by the buildCaptchaAndGetID method
     * @return the challenge
     * @throws com.octo.captcha.CaptchaServiceException if the ticket is invalid
     */
    public final Object getChallengeForID(String ID) throws CaptchaServiceException
    {
        return store.getCaptcha(ID).getChallenge();
    }

    /**
     * Method to retrive the question corresponding to the given ticket from the store.
     * @param ID the ticket provided by the buildCaptchaAndGetID method
     * @return the question
     * @throws com.octo.captcha.CaptchaServiceException if the ticket is invalid
     */
    public final String getQuestionForID(String ID) throws CaptchaServiceException
    {
        return store.getCaptcha(ID).getQuestion();
    }

    /**
     * Method to validate a response to the challenge corresponding to the given ticket and remove the coresponding
     * captcha from the store.
     * @param ID the ticket provided by the buildCaptchaAndGetID method
     * @return true if the response is correct, false otherwise.
     * @throws com.octo.captcha.CaptchaServiceException if the ticket is invalid
     */
    public Boolean validateResponseForID(String ID, Object response) throws CaptchaServiceException
    {
        Boolean valid = store.getCaptcha(ID).validateResponse(response);
        store.removeCaptcha(ID);
        return valid;
    }

}
