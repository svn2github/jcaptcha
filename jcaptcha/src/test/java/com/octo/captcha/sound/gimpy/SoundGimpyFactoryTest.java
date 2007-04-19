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

package com.octo.captcha.sound.gimpy;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.component.sound.soundconfigurator.FreeTTSSoundConfigurator;
import com.octo.captcha.component.sound.wordtosound.FreeTTSWordToSound;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import junit.framework.TestCase;

public class SoundGimpyFactoryTest extends TestCase {

    private static String voiceName = "kevin16";

    private static String voicePackage = "com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory,com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory";

    GimpySoundFactory tested;

    public SoundGimpyFactoryTest(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        tested = new GimpySoundFactory(new RandomWordGenerator("a"), new FreeTTSWordToSound(new FreeTTSSoundConfigurator(voiceName, voicePackage, 1.0f, 100, 100), 3, 6));
    }

    public void testGetRandomRange() throws Exception {
    }

    public void testGetRandomLength() throws Exception {

    }

    public void testGimpyFactory() throws Exception {
        try {
            new GimpySoundFactory(null, null);
            fail("Test is not implemented");
        } catch (CaptchaException e) {
            assertNotNull(e.getMessage());
        }
        try {
            new GimpySoundFactory(new RandomWordGenerator("a"), null);
            fail("Test is not implemented");
        } catch (CaptchaException e) {
            assertNotNull(e.getMessage());
        }

        try {
            new GimpySoundFactory(null, new FreeTTSWordToSound(new FreeTTSSoundConfigurator(voiceName, voicePackage, 1.0f, 100, 100), 3, 6));
            fail("Test is not implemented");
        } catch (CaptchaException e) {
            assertNotNull(e.getMessage());
        }
    }

}
