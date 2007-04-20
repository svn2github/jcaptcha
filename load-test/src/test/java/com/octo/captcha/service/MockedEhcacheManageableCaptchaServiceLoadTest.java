/*
 * JCaptcha, the open source java framework for captcha definition and integration
 * Copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.service;


public class MockedEhcacheManageableCaptchaServiceLoadTest extends ServiceLoadTestAbstract {
    EhcacheManageableCaptchaService ehcacheManageableCaptchaService;

    protected void setUp() throws Exception {
        this.service = new MockedEhCacheManageableCaptchaService(new MockCaptchaEngine(),
                160, 1000);
        AbstractManageableCaptchaServiceTest.CAPTCHA_STORE_LOAD_BEFORE_GARBAGE_COLLECTION = 0;
    }


}