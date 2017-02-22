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
package com.octo.captcha.engine;

import java.util.Locale;

import com.octo.captcha.sound.SoundCaptcha;
import com.octo.captcha.sound.SoundCaptchaFactory;

/**
 * @author <a href="mailto:marc.antoine.garrigue@gmail.com">Marc-Antoine Garrigue</a>
 * @version $Id$
 */
public class MockSoundCaptchaFactory extends SoundCaptchaFactory {
    /**
     * SoundCaptcha.
     *
     * @return a Sound captcha
     */
    public SoundCaptcha getSoundCaptcha() {
        return new SoundCaptcha(null, null) {
            private static final long serialVersionUID = 6412472463777426196L;

			public Boolean validateResponse(Object response) {
                return Boolean.FALSE;
            }
        };
    }

    /**
     * a SoundCaptcha.
     *
     * @return a localized SoundCaptcha
     */
    public SoundCaptcha getSoundCaptcha(Locale locale) {
        return getSoundCaptcha();
    }


}
