
package com.octo.captcha.image.gimpy;

import com.octo.captcha.image.ImageCaptchaEngine;
import com.octo.captcha.image.DefaultImageCaptchaEngine;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.image.gimpy.WordGenerator;
import com.octo.captcha.image.gimpy.WordToImage;
import com.octo.captcha.image.gimpy.wordgenerator.DictionaryWordGenerator;
import com.octo.captcha.image.gimpy.wordgenerator.FileDictionnary;
import com.octo.captcha.image.gimpy.wordtoimage.ComposedWordToImage;
import com.octo.captcha.image.gimpy.wordtoimage.BackgroundGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.TextPaster;
import com.octo.captcha.image.gimpy.wordtoimage.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.backgroundgenerator.MultipleShapeBackgroundGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.backgroundgenerator.FileReaderRandomBackgroundGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.fontgenerator.RandomFontGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.fontgenerator.TwistedAndShearedRandomFontGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.fontgenerator.TwistedRandomFontGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.FontGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.FilteredComposedWordToImage;
import com.octo.captcha.image.gimpy.wordtoimage.textpaster.DoubleTextPaster;
import com.octo.captcha.image.gimpy.wordtoimage.textpaster.DoubleRandomTextPaster;
import com.octo.captcha.image.gimpy.wordtoimage.textpaster.RandomTextPaster;

import java.awt.Color;
import java.awt.image.ImageFilter;

/**
 * <p>Description: very simple engine</p>
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class MultipleGimpyEngine extends DefaultImageCaptchaEngine
{

    static ImageCaptchaFactory[] factories;

    static
    {
       //word generator
       WordGenerator dictionnaryWords = new DictionaryWordGenerator(new FileDictionnary("toddlist"));

       //wordtoimage components
        TextPaster randomPaster = new RandomTextPaster(new Integer(6), new Integer(8), Color.WHITE);
        TextPaster doublePaster = new DoubleRandomTextPaster(new Integer(6), new Integer(8), Color.WHITE);


        BackgroundGenerator fileBack = new FileReaderRandomBackgroundGenerator(new Integer(200), new Integer(100), "./images");
        BackgroundGenerator funkyBack = new FunkyBackgroundGenerator(new Integer(200), new Integer(100));

        FontGenerator twistedFont = new TwistedRandomFontGenerator(new Integer(30), new Integer(45));
        FontGenerator shearedFont = new TwistedAndShearedRandomFontGenerator(new Integer(30), new Integer(45));


        //word2image 1
        WordToImage word2image = new ComposedWordToImage(twistedFont, fileBack, randomPaster);
        WordToImage word2image1 = new ComposedWordToImage(shearedFont, fileBack, randomPaster);
        WordToImage word2image2 = new ComposedWordToImage(twistedFont, funkyBack, randomPaster);
        WordToImage word2image3 = new ComposedWordToImage(shearedFont, funkyBack, randomPaster);
        WordToImage word2image4 = new ComposedWordToImage(twistedFont, fileBack, doublePaster);
        WordToImage word2image5 = new ComposedWordToImage(shearedFont, fileBack, doublePaster);
        WordToImage word2image6 = new ComposedWordToImage(twistedFont, funkyBack, doublePaster);
        WordToImage word2image7 = new ComposedWordToImage(shearedFont, funkyBack, doublePaster);

        //Add to array
        factories = new ImageCaptchaFactory[8];
        factories[0] = new GimpyFactory(dictionnaryWords, word2image);
        factories[1] = new GimpyFactory(dictionnaryWords, word2image1);
        factories[2] = new GimpyFactory(dictionnaryWords, word2image2);
        factories[3] = new GimpyFactory(dictionnaryWords, word2image3);
        factories[4] = new GimpyFactory(dictionnaryWords, word2image4);
        factories[5] = new GimpyFactory(dictionnaryWords, word2image5);
        factories[6] = new GimpyFactory(dictionnaryWords, word2image6);
        factories[7] = new GimpyFactory(dictionnaryWords, word2image7);

    }

    public MultipleGimpyEngine()
    {

        super(factories);

    }

}
