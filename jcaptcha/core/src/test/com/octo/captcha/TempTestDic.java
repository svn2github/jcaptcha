/*
 * Created on May 2, 2005
 *
 */
package com.octo.captcha;

import java.util.Locale;

import junit.framework.TestCase;

import com.octo.captcha.component.wordgenerator.FileDictionnary;
import com.octo.captcha.component.wordgenerator.WordList;

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
     }
}
