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

package com.octo.captcha.wordgenerator;

import com.octo.captcha.CaptchaException;
import junit.framework.TestCase;

import java.util.Locale;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class DictionaryWordGeneratorTest extends TestCase {

    private DictionaryWordGenerator dictionaryWordGenerator;
    private static String[] wordlist ={"1","1234","123456","123456789","123"};
    private static int[] lenghts={1,4,6,9,3};
    private static Integer UNKNOWN_LENGHT =new Integer(100);

    /**
     * Constructor for DictionaryWordGeneratorTest.
     * @param name
     */
    public DictionaryWordGeneratorTest(String name) {
        super(name);
    }

    public void setUp() {
        this.dictionaryWordGenerator = new DictionaryWordGenerator(
            new ArrayDictionary(wordlist));
    }

    public void testGetWordInteger() {
        for(int i=0;i<lenghts.length;i++){
            Integer length = new Integer(lenghts[i]);
            String test = this.dictionaryWordGenerator.getWord(length);
        assertNotNull(test);
        assertTrue(test.length() > 0);
        assertEquals(length.intValue(),test.length());

        }
        try
        {
            String test = this.dictionaryWordGenerator.getWord(UNKNOWN_LENGHT);
            fail("Should throw a CaptchaException");
        } catch (CaptchaException e)
        {
            assertNotNull(e.getMessage());
        }
    }

    public void testGetWordIntegerLocale() {
        for(int i=0;i<lenghts.length;i++){
            Integer length = new Integer(lenghts[i]);
        String test = this.dictionaryWordGenerator.getWord(length,Locale.US);
        assertNotNull(test);
        assertTrue(test.length() > 0);
        assertEquals(length.intValue(),test.length());
            }
           try
        {
            String test = this.dictionaryWordGenerator.getWord(UNKNOWN_LENGHT);
            fail("Should throw a CaptchaException");
        } catch (CaptchaException e)
        {
            assertNotNull(e.getMessage());
        }
    }


}
