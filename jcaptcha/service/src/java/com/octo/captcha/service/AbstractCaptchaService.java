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

import com.octo.captcha.Captcha;
import com.octo.captcha.service.captchastore.CaptchaStore;

import java.util.Locale;

/**
 * This is a base class for CaptchaService implementations.
 * It implements the lyfe cycle stuff.
 * It uses  : a CaptchaStore to store captcha during the life cycle,
 * and a CaptchaEngine to build captchas.
 * All concrete implementation (that uses a specific capthcaStore and captchaEngine) should
 * provide a default non argument constructor (by subclassing this class, and calling the constructor of
 * the abstract class)
 *
 * @author Marc-Antoine Garrigue mailto:mag@octo.com
 */
public abstract class AbstractCaptchaService implements CaptchaService {

    protected CaptchaStore store;
    protected com.octo.captcha.engine.CaptchaEngine engine;


    protected AbstractCaptchaService(CaptchaStore captchaStore,
                                     com.octo.captcha.engine.CaptchaEngine captchaEngine) {
        if(captchaEngine==null||captchaStore==null)throw new IllegalArgumentException("Store or gimpy can't be null");
        this.engine = captchaEngine;
        this.store = captchaStore;
    };





    /**
     * Method to retrive the challenge corresponding to the given ticket from the store.
     *
     * @param ID the ticket provided by the buildCaptchaAndGetID method
     * @return the challenge
     * @throws com.octo.captcha.service.CaptchaServiceException
     *          if the ticket is invalid
     */
    public Object getChallengeForID(String ID) throws CaptchaServiceException {
        return this.getChallengeForID(ID, Locale.getDefault());
    }

    /**
     * Method to retrive the challenge corresponding to the given ticket.
     *
     * @param ID     ticket
     * @param locale the desired localized capthca
     * @return the localized challenge
     * @throws com.octo.captcha.service.CaptchaServiceException
     *          if the ticket is invalid
     */
    public Object getChallengeForID(String ID, Locale locale) throws CaptchaServiceException {
        Captcha captcha;
        //check if has capthca
        if (!this.store.hasCaptcha(ID)) {
            //if not see if it
            captcha = generateAndStoreCaptcha(locale, ID);
        } else {
            captcha = this.store.getCaptcha(ID);
            if(captcha.hasGetChalengeBeenCalled().booleanValue()){
                captcha = generateAndStoreCaptcha(locale, ID);
            }
        }
        Object challenge = getChallengeClone(captcha);
        captcha.disposeChallenge();
        return challenge;
    }


    /**
     * Method to retrive the question corresponding to the given ticket.
     *
     * @param ID     ticket
     * @param locale the desired localized capthca
     * @return the localized question
     * @throws com.octo.captcha.service.CaptchaServiceException
     *          if the ticket is invalid
     */
    public String getQuestionForID(String ID, Locale locale) throws CaptchaServiceException {
        Captcha captcha;
        //check if has capthca
        if (!this.store.hasCaptcha(ID)) {
            //if not generate it
            captcha = generateAndStoreCaptcha(locale, ID);
        } else {
            captcha = this.store.getCaptcha(ID);
        }
        return captcha.getQuestion();
    }

    /**
     * Method to retrive the question corresponding to the given ticket from the store.
     *
     * @param ID the ticket provided by the buildCaptchaAndGetID method
     * @return the question
     * @throws com.octo.captcha.service.CaptchaServiceException
     *          if the ticket is invalid
     */
    public String getQuestionForID(String ID) throws CaptchaServiceException {
        return this.getQuestionForID(ID, Locale.getDefault());
    }

    /**
     * Method to validate a response to the challenge corresponding to the given ticket and remove the coresponding
     * captcha from the store.
     *
     * @param ID the ticket provided by the buildCaptchaAndGetID method
     * @return true if the response is correct, false otherwise.
     * @throws com.octo.captcha.service.CaptchaServiceException
     *          if the ticket is invalid
     */
    public Boolean validateResponseForID(String ID, Object response) throws CaptchaServiceException {
        if (!store.hasCaptcha(ID)) {
            throw new CaptchaServiceException("Invalid ID, could not validate!");
        } else {
            Boolean valid = store.getCaptcha(ID).validateResponse(response);
            store.removeCaptcha(ID);
            return valid;
        }
    }


    protected Captcha generateAndStoreCaptcha(Locale locale, String ID) {
        Captcha captcha = engine.getNextCaptcha(locale);
        this.store.storeCaptcha(ID, captcha);
        return captcha;
    }

    /**
     * This method must be implemented by sublcasses and :
     * Retrieve the challenge from the captcha
     * Make and return a clone of the challenge
     * Return the clone
     * It has be design in order to let the service dipose
     * the challenge of the captcha after rendering.
     * It should be implemented for all captcha type (@see ImageCaptchaService implementations
     * for exemple)
     * @param captcha
     * @return a Challenge Clone
     */
    protected abstract Object getChallengeClone(Captcha captcha);


}
