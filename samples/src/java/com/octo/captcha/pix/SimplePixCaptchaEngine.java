package com.octo.captcha.pix;

import com.octo.captcha.engine.FactoryInitializer;
import com.octo.captcha.pix.gimpy.GimpyFactory;
import com.octo.captcha.pix.gimpy.WordGenerator;
import com.octo.captcha.pix.gimpy.WordToImage;
import com.octo.captcha.pix.gimpy.wordgenerators.DictionaryWordGenerator;
import com.octo.captcha.pix.gimpy.wordgenerators.FileDictionnary;
import com.octo.captcha.pix.gimpy.wordtoimages.AbstractBackgroundGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.AbstractFontGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.AbstractTextPaster;
import com.octo.captcha.pix.gimpy.wordtoimages.ComposedWordToImage;
import com.octo.captcha.pix.gimpy.wordtoimages.backgroundgenerators.FunkyBackgroundGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.fontgenerator.RandomFontGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.textpasters.DoubleTextPaster;

/**
 * <p>Description: very s </p>
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class SimplePixCaptchaEngine extends PixCaptchaEngine {

    PixCaptchaFactory factory;

    public SimplePixCaptchaEngine(){
        init();
    }

    public  PixCaptchaFactory getPixCaptchaFactory(){
        return factory;
    }

    private void init() {
        AbstractTextPaster paster = new DoubleTextPaster(new Integer(8),new Integer(8));
        AbstractBackgroundGenerator back = new FunkyBackgroundGenerator(new Integer(50),new Integer(100));
        AbstractFontGenerator font = new RandomFontGenerator(new Integer(20));
        WordGenerator words = new DictionaryWordGenerator(new FileDictionnary("toddlist"));
        WordToImage word2image = new ComposedWordToImage(font, back, paster);
        factory = new GimpyFactory(words, word2image);
    }
}
