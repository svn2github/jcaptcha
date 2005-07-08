/*
 * Created on 4 mai 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.octo.captcha.component.image.textpaster;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedString;

import com.octo.captcha.Captcha;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.color.ColorGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.color.SingleColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.TwistedAndShearedRandomFontGenerator;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.wordgenerator.WordGenerator;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Color;

/**
 * @author Benoit TODO To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Style - Code Templates
 */
public class SampleForColor
{

    public static void main(String[] args)
    {
        Color[] colorList = new Color[3];

        colorList[0] = Color.BLUE;
        colorList[1] = Color.RED;
        colorList[2] = Color.GRAY;
        
        Color col  = Color.BLUE;

        RandomListColorGenerator colorGenerator = new RandomListColorGenerator(colorList);
        TextPaster paster = new BaffleRandomTextPaster(new Integer(1), new Integer(10),
            colorGenerator, true, new Integer(1), Color.BLUE);

        BufferedImage imageTest = new BufferedImage(100, 150, BufferedImage.TYPE_INT_RGB);
        AttributedString stringTest = new AttributedString("testForColor");
        BufferedImage test = null;
        //test = paster.pasteText(imageTest, stringTest);

        File file = new File("c:/test.jpg");

        //word generator
        //WordGenerator dictionnaryWords = new DictionaryWordGenerator(new
        // FileDictionnary("toddlist"));
        com.octo.captcha.component.wordgenerator.WordGenerator randomWords = new com.octo.captcha.component.wordgenerator.RandomWordGenerator(
            "abcdefghijklmnopqurstABCDEFGHIJKLMNOPQRST");

        //wordtoimage components
        TextPaster randomPaster = new RandomTextPaster(new Integer(4), new Integer(7),
            colorGenerator, true);
        
        LineRandomTextPaster linePaster = new LineRandomTextPaster(new Integer(4), new Integer(7),
            colorGenerator, true, new Integer(2), colorGenerator);

        RandomRangeColorGenerator singleColGen1 = new RandomRangeColorGenerator(new int[]{0,20},new int[]{0,20},new int[]{190,255});
        RandomRangeColorGenerator singleColGen2 = new RandomRangeColorGenerator(new int[]{0,225},new int[]{0,20},new int[]{0,10});
        RandomRangeColorGenerator singleColGen3 = new RandomRangeColorGenerator(new int[]{0,20},new int[]{58,225},new int[]{190,255});
        RandomRangeColorGenerator singleColGen4 = new RandomRangeColorGenerator(new int[]{0,90},new int[]{0,90},new int[]{0,30});
        //BackgroundGenerator fileBack = new FileReaderRandomBackgroundGenerator(new Integer(200),
        // new Integer(100), "gimpybackgrounds");
        
        BackgroundGenerator funkyBack = new FunkyBackgroundGenerator(new Integer(200), new Integer(
            100), singleColGen1, singleColGen2, singleColGen3, singleColGen4, 0.7f);

        BackgroundGenerator grad = new GradientBackgroundGenerator(new Integer(200), new Integer(100),
            colorGenerator, new SingleColorGenerator(Color.BLUE));

        FontGenerator shearedFont = new TwistedAndShearedRandomFontGenerator(new Integer(30),
            new Integer(40));

        //word2image 1
        com.octo.captcha.component.image.wordtoimage.WordToImage word2image = new com.octo.captcha.component.image.wordtoimage.ComposedWordToImage(
            shearedFont, grad, linePaster);

        test = (new GimpyFactory(randomWords, word2image)).getImageCaptcha().getImageChallenge();

        try
        {
            serialize(test, file);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void serialize(BufferedImage image, File file) throws IOException
    {
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        encodeJPG(fos, image);
        fos.flush();
        fos.close();
    }

    public static void encodeJPG(OutputStream sos, BufferedImage image) throws IOException
    {
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(sos);

        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
        //        param.setHorizontalSubsampling(0, 1);
        //        param.setHorizontalSubsampling(1, 1);
        //        param.setHorizontalSubsampling(2, 1);
        //        param.setVerticalSubsampling(0, 1);
        //        param.setVerticalSubsampling(1, 1);
        //        param.setVerticalSubsampling(2, 1);
        param.setQuality(1.0f, false);
        encoder.setJPEGEncodeParam(param);
        encoder.encode(image);
        encoder.getOutputStream().close();
    }
}
