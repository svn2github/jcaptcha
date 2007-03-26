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

/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */
package com.octo.captcha.engine.sound;

import com.octo.captcha.Captcha;
import com.octo.captcha.sound.SoundCaptcha;

import java.util.Locale;

/**
 * <p>Description: abstract base class for SoundCaptcha engines</p>.
 *
 * @author Benoit Doumas
 * @version 1.0
 */
public abstract class SoundCaptchaEngine
        implements com.octo.captcha.engine.CaptchaEngine {

    /**
     * This return a new captcha. It may be used directly.
     *
     * @return a new Captcha
     */
    public final Captcha getNextCaptcha() {
        return getNextSoundCaptcha();
    }

    /**
     * This return a new captcha. It may be used directly.
     *
     * @param locale the desired locale
     *
     * @return a new Captcha
     */
    public final Captcha getNextCaptcha(Locale locale) {
        return getNextSoundCaptcha(locale);
    }

    /**
     * This method build a SoundCaptchaFactory.
     *
     * @return a SoundCaptcha
     */
    public abstract SoundCaptcha getNextSoundCaptcha();

    /**
     * This method build a SoundCaptchaFactory.
     *
     * @return a SoundCaptcha
     */
    public abstract SoundCaptcha getNextSoundCaptcha(Locale locale);

}
