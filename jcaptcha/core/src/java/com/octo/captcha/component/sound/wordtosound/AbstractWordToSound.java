package com.octo.captcha.component.sound.wordtosound;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.component.sound.WordToSound;
import com.octo.captcha.component.sound.utils.Sound;
import com.octo.captcha.component.sound.utils.StringToSound;

/**
 * @author Gandin Mathieu
 * @version 1.0
 */
public abstract class AbstractWordToSound implements WordToSound{

	protected StringToSound stringToSound;

	public Sound getSound(String word) throws CaptchaException {
		return stringToSound.getSound(word);
	}

	public abstract int getMaxAcceptedWordLenght();

	public abstract int getMinAcceptedWordLenght();

}
