/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 */

package com.octo.captcha.image.gimpy.wordgenerator;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.TreeMap;

/**
 * <p>Container for words that is initialized from a Dictionnary. </p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class WordList
{


    private TreeMap sortedWords = new TreeMap();

    private Locale locale;

    private Random myRandom = new Random();

    /**
     * A word list has to be constructed with a locale
     * @param locale
     */
    public WordList(Locale locale)
    {
        this.locale = locale;
    };

    /**
     * Return a locale for this list
     * @return th e locale
     */
    public Locale getLocale()
    {
        return locale;
    }

    /**
     * Adds a word to the list
     * @param word
     */
    public void addWord(String word)
    {
        Integer lenght = new Integer(word.length());
        if(sortedWords.containsKey(lenght)){
            ArrayList thisLenghtwords = (ArrayList)sortedWords.get(lenght);
            thisLenghtwords.add(word);
            sortedWords.put(lenght,thisLenghtwords);
        }else{
            ArrayList thisLenghtwords = new ArrayList();
            thisLenghtwords.add(word);
            sortedWords.put(lenght,thisLenghtwords);
        }
        //words.add(word);
        //lengths.add(new Integer(word.length()));

    }

    /**
     * Return the min lenght of contained word in this wordlist
     * @return the min lenght of contained word in this wordlist
     */
    public Integer getMinWord(){
        return (Integer)sortedWords.firstKey();
    }


    /**
     * Return the max lenght of contained word in this wordlist
     * @return the max lenght of contained word in this wordlist
     */
    public Integer getMaxWord(){
        return (Integer)sortedWords.lastKey();
    }


    /**
     * Return a word of randomly choosen of the specified lenght.
     * Return null if none found
     * @param lenght
     * @return a word of this lenght
     */
    public String getNextWord(Integer lenght)
    {
        if(sortedWords.containsKey(lenght)){
            ArrayList thisLenghtwords = (ArrayList)sortedWords.get(lenght);
            int pickAWord = myRandom.nextInt(thisLenghtwords.size());
            return (String) thisLenghtwords.get(pickAWord);
        }else{
            return null;
        }
    }
}
