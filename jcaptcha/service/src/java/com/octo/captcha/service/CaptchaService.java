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
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * Portions of this software are based upon public domain software
 * originally written at the National Center for Supercomputing Applications,
 * University of Illinois, Urbana-Champaign.
 */

package com.octo.captcha.service;

import java.util.Locale;

/**
 * <p>Main interface of the package. Used by client applications to expose Captchas Challenge validate the response in a
 * transparent and easy way.
 * The flow of operations for using this service is :
 * <ul>
 * <li>Call the getQuestionForID method to retrive a challenge and present it to the final user.(could be localized)</li>
 * <li>Call the getChallengeForID method to retrive a challenge and present it to the final user.(could be localized)</li>
 * <li>Call the validateResponseForID method to know if the final user is a human or not.</li>
 * </ul>
 * <p/>
 * Developpers should implement this interface using the following rules :
 * <br/>
 * When the getChallengeForID method is called,
 * If no captcha exist for this id,
 * create a new captcha
 * return the challenge.
 *
 * else if the getChallenge method has been called on the stored captcha,
 * generate a new captcha, else return this captcha challenge.
 *
 * <br/>
 * The getQuestionForId should be called after the getChallenge (because this generate a new captcha),
 * and must first see if a captcha is associated with this ID :
 * If no, associates a new one.
 * When the getQuestionForId method is called return the question
 * <br/>
 * When the validateResponseForID method is called.
 * Throw a CaptchaServiceException if the ID is invalid
 * else return a boolean, and free the ID (remove the captcha).
 * <br/>
 * All method may throw a CaptchaServiceException if an error occurs during Captcha Generation.
 * </p>
 *
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public interface CaptchaService {


    /**
     * Method to retrive the challenge corresponding to the given ticket.
     *
     * @param ID ticket
     * @return the challenge
     * @throws CaptchaServiceException if the ticket is invalid
     */
    Object getChallengeForID(String ID) throws CaptchaServiceException;

    /**
     * Method to retrive the challenge corresponding to the given ticket.
     *
     * @param ID     ticket
     * @param locale the desired localized capthca
     * @return the localized challenge
     * @throws CaptchaServiceException if the ticket is invalid
     */
    Object getChallengeForID(String ID, Locale locale) throws CaptchaServiceException;

    /**
     * Method to retrive the question corresponding to the given ticket.
     *
     * @param ID ticket
     *           * @param locale the desired localized capthca
     * @return the question
     * @throws CaptchaServiceException if the ticket is invalid
     */
    String getQuestionForID(String ID) throws CaptchaServiceException;

    /**
     * Method to retrive the question corresponding to the given ticket.
     *
     * @param ID     ticket
     * @param locale the desired localized capthca
     * @return the localized question
     * @throws CaptchaServiceException if the ticket is invalid
     */
    String getQuestionForID(String ID, Locale locale) throws CaptchaServiceException;


    /**
     * Method to validate a response to the challenge corresponding to the given ticket.
     *
     * @param ID ticket
     * @return true if the response is correct, false otherwise.
     * @throws CaptchaServiceException if the ticket is invalid
     */
    Boolean validateResponseForID(String ID, Object response) throws CaptchaServiceException;
}