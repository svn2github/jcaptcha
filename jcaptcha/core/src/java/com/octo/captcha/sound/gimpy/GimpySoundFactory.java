package com.octo.captcha.sound.gimpy;

import java.util.Locale;
import java.util.Random;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.CaptchaQuestionHelper;
import com.octo.captcha.component.sound.WordToSound;
import com.octo.captcha.component.wordgenerator.WordGenerator;
import com.octo.captcha.sound.SoundCaptcha;
import com.octo.captcha.sound.SoundCaptchaFactory;
import com.octo.captcha.component.sound.utils.*;

/**
 * @author Gandin Mathieu
 * @version 1.0
 */
public class GimpySoundFactory extends SoundCaptchaFactory {

	private WordGenerator wordGenerator;
	private WordToSound word2Sound;
	private Random myRandom = new Random();
	public static final String BUNDLE_QUESTION_KEY = GimpySound.class.getName();

	public GimpySoundFactory(WordGenerator thewordGenerator,WordToSound theword2Sound) {
		if(thewordGenerator == null) {
			throw new CaptchaException("Invalid configuration for a " +
				"GimpySoundFactory : WordGenerator can't be null");
		}
		if(theword2Sound == null) {
					throw new CaptchaException("Invalid configuration for a " +
						"GimpySoundFactory : Word2Sound can't be null");
				}
		this.wordGenerator = thewordGenerator;
		this.word2Sound = theword2Sound;
	}
	
	public WordToSound getWordToSound() {
		return this.word2Sound;
	}

	public WordGenerator getWordGenerator() {
		return this.wordGenerator;
	}
	
	/**
	 *
	 */

	public SoundCaptcha getSoundCaptcha() {
		return getSoundCaptcha(Locale.getDefault());
	}

	/**
	 *
	 */

	public SoundCaptcha getSoundCaptcha(Locale locale) {
		String word = this.wordGenerator.getWord(getRandomLenght(),locale);
		Sound sound = this.word2Sound.getSound(word);
		SoundCaptcha soundCaptcha = new GimpySound(
			CaptchaQuestionHelper.getQuestion(locale,BUNDLE_QUESTION_KEY),
				sound,word);
		return soundCaptcha;
	}
	
	protected Integer getRandomLenght() {
			Integer wordLenght;
			int range = getWordToSound().getMaxAcceptedWordLenght() -
					getWordToSound().getMinAcceptedWordLenght();
			int randomRange = range != 0 ? myRandom.nextInt(range + 1) : 0;
			wordLenght = new Integer(randomRange + getWordToSound().getMinAcceptedWordLenght());
			return wordLenght;
		}

}
