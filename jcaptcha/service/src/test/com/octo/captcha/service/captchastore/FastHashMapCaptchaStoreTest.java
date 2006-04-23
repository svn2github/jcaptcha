/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * Copyright (c) 2005 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */
package com.octo.captcha.service.captchastore;

public class FastHashMapCaptchaStoreTest extends CaptchaStoreTestAbstract {

    public CaptchaStore initStore() {
        return new FastHashMapCaptchaStore();
    }

}
