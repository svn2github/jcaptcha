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

package com.octo.captcha.engine.image;

import com.octo.captcha.Captcha;
import com.octo.captcha.image.ImageCaptcha;

import java.util.Locale;

/**
 * <p>Description: abstract base class for ImageCaptcha engines</p>.
 *
 * @author <a href="mailto:mag@jcaptcha.net">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public abstract class ImageCaptchaEngine
        implements com.octo.captcha.engine.CaptchaEngine {

    /**
     * This return a new captcha. It may be used directly.
     *
     * @return a new Captcha
     */
    public final Captcha getNextCaptcha() {
        return getNextImageCaptcha();
    }

    /**
     * This return a new captcha. It may be used directly.
     *
     * @param locale the desired locale
     *
     * @return a new Captcha
     */
    public final Captcha getNextCaptcha(Locale locale) {
        return getNextImageCaptcha(locale);
    }

    /**
     * This method build a ImageCaptchaFactory.
     *
     * @return a CaptchaFactory
     */
    public abstract ImageCaptcha getNextImageCaptcha();

    /**
     * This method build a ImageCaptchaFactory.
     *
     * @return a CaptchaFactory
     */
    public abstract ImageCaptcha getNextImageCaptcha(Locale locale);

}
