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

/**
 * <p><ul><li></li></ul></p>
 *
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class MockCaptcha implements Captcha {
    private boolean isDisposed = false;
    private boolean asBeenCalled = false;

    public static final String QUESTION = "mockQuestion";
    public static final String CHALLENGE = "mockChallenge";

    /**
     * Accessor captcha question.
     *
     * @return the question
     */
    public String getQuestion() {
        return QUESTION;
    }

    /**
     * Accerssor for the questionned challenge.
     *
     * @return the challenge (may be an image for image captcha...
     */
    public Object getChallenge() {
        asBeenCalled = true;
        return !isDisposed ? CHALLENGE : null;
    }

    /**
     * Validation routine for the response.
     *
     * @param response to the question concerning the chalenge
     * @return true if the answer is correct, false otherwise.
     */
    public Boolean validateResponse(Object response) {
        return new Boolean(response.toString());
    }

    /**
     * Dispose the challenge, once this method is call the getChallenge method will return null.<br>
     * It has been added for technical reasons : a captcha is always used in a two step fashion<br>
     * First submit the challenge, and then wait until the response arrives.<br>
     * It had been asked to have a method to dispose the challenge that is no longer used after being dipslayed.
     * So here it is!
     */
    public void disposeChallenge() {
        isDisposed = true;
    }

    /**
     * This method should return true if the getChalenge method has been called (has been added in order to properly
     * manage the captcha state.
     *
     * @return true if getChallenge has been called false otherwise.
     */
    public Boolean hasGetChalengeBeenCalled() {
        return new Boolean(asBeenCalled);
    }

}
