
package com.octo.captcha.component.sound;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.component.sound.utils.Sound;

/**
 * @author Gandin Mathieu
 * @version 1.0
 */
public interface WordToSound
{

    /**
     * @return the max word lenght accepted by this word2sound service
     */
    int getMaxAcceptedWordLenght();

    /**
     * @return the min word lenght accepted by this word2sound service
     */
    int getMinAcceptedWordLenght();

    /**
     * Main method for this service Return an image with the specified
     *
     * @return the generated sound
     * @throws com.octo.captcha.CaptchaException
     *          if word is invalid or an exception occurs during the sound
     *          generation
     */
    Sound getSound(String word) throws CaptchaException;
}
