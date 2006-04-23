/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * Copyright (c) 2005 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */
package com.octo.captcha.service;

import com.octo.captcha.Captcha;
import com.octo.captcha.engine.CaptchaEngine;

/**
 * User: mag Date: 17 oct. 2004 Time: 12:47:51
 */
public class MockedEhCacheManageableCaptchaService extends EhcacheManageableCaptchaService {

    protected MockedEhCacheManageableCaptchaService(com.octo.captcha.engine.CaptchaEngine captchaEngine,

                                                    int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize) {
        super(captchaEngine, minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize);
    }

    /**
     * @return the engine served by this service
     */
    public CaptchaEngine getEngine() {
        return this.engine;
    }

    /**
     * Updates the engine served by this service
     *
     * @param engine
     */
    public void setCaptchaEngine(CaptchaEngine engine) {
        this.engine =engine;
    }

    /**
     * This method must be implemented by sublcasses and : Retrieve the challenge from the captcha Make and return a
     * clone of the challenge Return the clone It has be design in order to let the service dipose the challenge of the
     * captcha after rendering. It should be implemented for all captcha type (@see ImageCaptchaService implementations
     * for exemple)
     *
     * @return a Challenge Clone
     */
    protected Object getChallengeClone(Captcha captcha) {
        return new String(captcha.getChallenge().toString()) + MockedCaptchaService.CLONE_CHALLENGE;
    }

}
