package com.octo.captcha.engine;

import java.awt.Color;

import com.octo.captcha.pix.PixCaptchaFactory;
import com.octo.captcha.pix.gimpy.wordtoimages.AbstractTextPaster;
import com.octo.captcha.pix.gimpy.wordtoimages.AbstractBackgroundGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.AbstractFontGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.ComposedWordToImage;
import com.octo.captcha.pix.gimpy.wordtoimages.textpasters.DoubleTextPaster;
import com.octo.captcha.pix.gimpy.wordtoimages.textpasters.WarpedTextPaster;
import com.octo.captcha.pix.gimpy.wordtoimages.fontgenerator.TwistedRandomFontGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.fontgenerator.TwistedAndShearedRandomFontGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.fontgenerator.RandomFontGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.backgroundgenerators.GradientBackgroundGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.backgroundgenerators.FunkyBackgroundGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.backgroundgenerators.EllipseBackgroundGenerator;
import com.octo.captcha.pix.gimpy.WordGenerator;
import com.octo.captcha.pix.gimpy.WordToImage;
import com.octo.captcha.pix.gimpy.GimpyFactory;
import com.octo.captcha.pix.gimpy.wordgenerators.DictionaryWordGenerator;
import com.octo.captcha.pix.gimpy.wordgenerators.FileDictionnary;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class AlternateFactoryInitializer extends FactoryInitializer {
    public static PixCaptchaFactory getPixCaptchaFactory(){
                 AbstractTextPaster paster = new WarpedTextPaster(new Integer(8),new Integer(8));
            AbstractBackgroundGenerator back = new FunkyBackgroundGenerator(new Integer(50),new Integer(100));
            AbstractFontGenerator font = new RandomFontGenerator(new Integer(20));
            WordGenerator words = new DictionaryWordGenerator(new FileDictionnary("toddlist"));
            WordToImage word2image = new ComposedWordToImage(font, back, paster);
            PixCaptchaFactory factory = new GimpyFactory(words, word2image);
            return factory;
        }
}
