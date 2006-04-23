/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * Copyright (c) 2005 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.sound;

import com.octo.captcha.Captcha;
import com.octo.captcha.CaptchaException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * <p/>
 * Description: String question about a Line challenge, this class is abstract. </p>
 *
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin </a>
 * @author Benoit Doumas
 * @version 1.1
 */
public abstract class SoundCaptcha implements Captcha {

    protected Boolean hasChallengeBeenCalled = Boolean.FALSE;

    protected String question;

    protected transient AudioInputStream challenge;

    protected SoundCaptcha(String thequestion, AudioInputStream thechallenge) {
        this.question = thequestion;
        this.challenge = thechallenge;
    }

    /**
     * Accessor to the question.
     */
    public final String getQuestion() {
        return this.question;
    }

    /**
     * Accessor to the challenge.
     */
    public final Object getChallenge() {
        return this.getSoundChallenge();
    }

    /**
     * Accessor to the sound challenge.
     *
     * @return an AudioInputStream
     */
    public final AudioInputStream getSoundChallenge() {
        hasChallengeBeenCalled = Boolean.TRUE;
        return this.challenge;
    }

    /*
     * public Boolean validateResponse(Object response) { return null; }
     */

    /**
     * this method is to clean the challenge.
     */
    public void disposeChallenge() {
//        try
//        {
//            challenge.close();
//        }
//        catch (IOException e)
//        {
//            throw new CaptchaException(e);
//        }
        this.challenge = null;
    }

    public Boolean hasGetChalengeBeenCalled() {
        return hasChallengeBeenCalled;
    }

    //use Wave encoding
    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {

        if (this.challenge != null) {
            out.defaultWriteObject();
            AudioSystem.write(challenge, AudioFileFormat.Type.WAVE, out);
        }

    }

    ;

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        try {
            this.challenge = AudioSystem.getAudioInputStream(in);
        } catch (UnsupportedAudioFileException e) {
            throw new CaptchaException("unable to deserialize input stream", e);
        }
    }

    ;

}