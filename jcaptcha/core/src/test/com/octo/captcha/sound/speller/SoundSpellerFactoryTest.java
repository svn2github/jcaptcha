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

package com.octo.captcha.sound.speller;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.component.sound.soundconfigurator.FreeTTSSoundConfigurator;
import com.octo.captcha.component.sound.wordtosound.FreeTTSWordToSound;
import com.octo.captcha.component.word.worddecorator.SpellerWordDecorator;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import junit.framework.TestCase;

public class SoundSpellerFactoryTest extends TestCase {

    private static String voiceName = "kevin16";

    private static String voicePackage = "com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory,com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory";

    SpellerSoundFactory tested;

    public SoundSpellerFactoryTest(String s) {
        super(s);
    }

    protected void setUp() throws Exception {
        super.setUp();
        tested = new SpellerSoundFactory(new RandomWordGenerator("a"), new FreeTTSWordToSound(new FreeTTSSoundConfigurator(voiceName, voicePackage, 1.0f, 100, 100), 3, 6), new SpellerWordDecorator(";"));
    }


    public void testSpellerSoundFactory() throws Exception {
        try {
            new SpellerSoundFactory(null, null, null);
            fail("Test is not implemented");
        } catch (CaptchaException e) {
            assertNotNull(e.getMessage());
        }
        try {
            new SpellerSoundFactory(new RandomWordGenerator("a"), null, null);
            fail("Test is not implemented");
        } catch (CaptchaException e) {
            assertNotNull(e.getMessage());
        }

        try {
            new SpellerSoundFactory(null, new FreeTTSWordToSound(new FreeTTSSoundConfigurator(voiceName, voicePackage, 1.0f, 100, 100), 3, 6), null);
            fail("Test is not implemented");
        } catch (CaptchaException e) {
            assertNotNull(e.getMessage());
        }
    }

}
