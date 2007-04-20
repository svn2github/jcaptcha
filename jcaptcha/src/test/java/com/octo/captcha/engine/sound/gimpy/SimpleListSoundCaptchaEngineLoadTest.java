/*
 * JCaptcha, the open source java framework for captcha definition and integration
 * Copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.engine.sound.gimpy;

import com.octo.captcha.engine.EngineLoadTestAbstract;

/**
 * <p><ul><li></li></ul></p>
 *
 * @author Benoit Doumas
 * @version 1.0
 */
public class SimpleListSoundCaptchaEngineLoadTest extends EngineLoadTestAbstract {
    protected void setUp() throws Exception {
        super.setUp();
        loader = SoundEngineLoadTestHelper.class;
        this.engine = new SimpleListSoundCaptchaEngine();
    }
}
