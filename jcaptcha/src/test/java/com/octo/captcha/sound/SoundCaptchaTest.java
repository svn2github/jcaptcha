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
package com.octo.captcha.sound;


import com.octo.captcha.component.sound.soundconfigurator.FreeTTSSoundConfigurator;
import com.octo.captcha.component.sound.soundconfigurator.SoundConfigurator;
import com.octo.captcha.component.sound.wordtosound.FreeTTSWordToSound;
import com.octo.captcha.component.sound.wordtosound.WordToSound;
import com.octo.captcha.component.word.ArrayDictionary;
import com.octo.captcha.component.word.wordgenerator.DictionaryWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.sound.gimpy.GimpySoundFactory;
import junit.framework.TestCase;

import javax.sound.sampled.AudioInputStream;

/**
 * <p/>
 * Description: </p>
 *
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin </a>
 * @author Antoine Véret
 * @version 1.0
 */
public class SoundCaptchaTest extends TestCase {
    private SoundCaptcha soundCaptcha;

    private static String voiceName = "kevin16";

    private static String voicePackage = "com.sun.speech.freetts.en.us.cmu_time_awb.AlanVoiceDirectory,com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory";

    /**
     * Constructor for ImageCaptchaTest.
     */
    public SoundCaptchaTest(String name) {
        super(name);
    }

    /**
     * this method is for initialisation for all the test cases
     */
    public void setUp() throws Exception {
        super.setUp();
        String[] wordlist = {"and", "oh", "test", "test", "hello", "lame", "eating", "snake"};

        WordGenerator words = new DictionaryWordGenerator(new ArrayDictionary(wordlist));

        SoundConfigurator configurator = new FreeTTSSoundConfigurator(voiceName, voicePackage, 1.0f, 100, 100);

        WordToSound word2sound = new FreeTTSWordToSound(configurator, 3, 6);
        SoundCaptchaFactory factory = new GimpySoundFactory(words, word2sound);
        soundCaptcha = factory.getSoundCaptcha();
    }

    /**
     * This test is for verifying if the question of the captcha is correctly instantiated.
     */
    public void testGetQuestion() {
        assertNotNull(soundCaptcha.getQuestion());
    }

    /**
     * This test is for verifying if the challenge of the captcha is correctly instantiated.
     */
    public void testGetChallenge() {
        assertNotNull(soundCaptcha.getChallenge());
        assertTrue("Captcha challenge is not an AudioInputStream",
                soundCaptcha.getChallenge() instanceof AudioInputStream);
    }

    /**
     * This test is for verifying if the audio captcha are different stream but have the same content.
     */
    public void testGetAudioChallenge() throws Exception {
        Object challengeObject = soundCaptcha.getChallenge();
        assertEquals(AudioInputStream.class, challengeObject.getClass());
        AudioInputStream challengeAudioStream = (AudioInputStream) challengeObject;
        AudioInputStream soundChallengeAudioStream = soundCaptcha.getSoundChallenge();
        assertEquals(soundChallengeAudioStream.getFormat().toString(), challengeAudioStream.getFormat().toString());
        assertEquals(soundChallengeAudioStream.getFrameLength(), challengeAudioStream.getFrameLength());
    }

    /**
     * This test is for verifying if the question of the captcha is correctly instantiated.
     */
    public static void main(String[] args) {
        String[] wordlist = {"and", "oh", "test", "test", "hello", "lame", "eating", "snake"};

        WordGenerator words = new DictionaryWordGenerator(new ArrayDictionary(wordlist));

        SoundConfigurator configurator = new FreeTTSSoundConfigurator(voiceName, voicePackage, 1.0f, 100, 100);

        WordToSound word2sound = new FreeTTSWordToSound(configurator, 3, 6);

        SoundCaptchaFactory factory = new GimpySoundFactory(words, word2sound);
        SoundCaptcha tCaptcha = factory.getSoundCaptcha();

        System.out.println(tCaptcha.getQuestion());
        AudioInputStream tInputStream = tCaptcha.getSoundChallenge();
    }
}