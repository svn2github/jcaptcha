package com.octo.captcha.component.sound.wordtosound;

import com.octo.captcha.component.sound.utils.StringToSound;

/**
 * @author Gandin Mathieu
 * @version 1.0
 */
public class SimpleWordToSound extends AbstractWordToSound {

	protected String BUNDLE_SIMPLE_WORD_TO_SOUND = "sound";

	public SimpleWordToSound() {
		this.stringToSound = new StringToSound(this.BUNDLE_SIMPLE_WORD_TO_SOUND);
	}

	public int getMaxAcceptedWordLenght() {
		return 15;
	}

	public int getMinAcceptedWordLenght() {
		return 10;
	}

}
