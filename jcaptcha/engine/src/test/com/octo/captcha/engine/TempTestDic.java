/*
 * Created on May 2, 2005
 *
 */
package com.octo.captcha.engine;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;


import junit.framework.TestCase;

import com.octo.captcha.component.wordgenerator.FileDictionnary;
import com.octo.captcha.component.wordgenerator.WordList;
import com.octo.captcha.engine.image.gimpy.BasicListGimpyEngine;
import com.octo.captcha.engine.image.utils.ImageToFile;
import com.octo.captcha.engine.sound.utils.SoundToFile;
import com.octo.captcha.image.ImageCaptcha;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @author Doumas Benoit
 */
public class TempTestDic extends TestCase
{

  
    public void testDic() throws Exception
    {
        FileDictionnary fileDictionnary = new FileDictionnary("toddlist");
        System.out.println(Locale.FRANCE); 
        WordList wordList = fileDictionnary.getWordList(Locale.FRANCE);
        String word = wordList.getNextWord(new Integer(4));
        System.out.println(word); 
        
        BasicListGimpyEngine engine = new BasicListGimpyEngine();
        ImageCaptcha captcha = engine.getNextImageCaptcha(Locale.FRANCE);
        

            BufferedImage challenge = captcha.getImageChallenge();


        try
        {
            ImageToFile.serialize(challenge, new File("c:\\test.jpg"));
        }
        catch (IOException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String text = null;
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter text: ");
        System.out.flush();

        try
        {
            text = reader.readLine();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (captcha.validateResponse(text).booleanValue())
        {
            System.out.print("Passed!!!");
        }
        else
        {
            System.out.print("Failed!!!");
        }
        captcha.disposeChallenge();
     }
}
