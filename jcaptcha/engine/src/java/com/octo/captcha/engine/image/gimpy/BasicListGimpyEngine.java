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

package com.octo.captcha.engine.image.gimpy;


import com.octo.captcha.image.wordtoimage.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.image.wordtoimage.backgroundgenerator.FileReaderRandomBackgroundGenerator;
import com.octo.captcha.image.wordtoimage.fontgenerator.TwistedAndShearedRandomFontGenerator;
import com.octo.captcha.image.wordtoimage.fontgenerator.FontGenerator;
import com.octo.captcha.image.wordtoimage.textpaster.DoubleRandomTextPaster;
import com.octo.captcha.image.wordtoimage.textpaster.TextPaster;

import java.awt.*;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class BasicListGimpyEngine extends com.octo.captcha.engine.image.ListImageCaptchaEngine
{

    protected void buildInitialFactories()
    {
        //word generator
        com.octo.captcha.wordgenerator.WordGenerator dictionnaryWords = new com.octo.captcha.wordgenerator.DictionaryWordGenerator(new com.octo.captcha.wordgenerator.FileDictionnary("toddlist"));
        //wordtoimage components
        TextPaster randomPaster = new DoubleRandomTextPaster(new Integer(6), new Integer(8), Color.WHITE);
        BackgroundGenerator fileBack = new FileReaderRandomBackgroundGenerator(new Integer(200), new Integer(100), "./images");
        //BackgroundGenerator funkyBack = new FunkyBackgroundGenerator(new Integer(200), new Integer(100));
        FontGenerator shearedFont = new TwistedAndShearedRandomFontGenerator(new Integer(30), new Integer(45));
        //word2image 1
        com.octo.captcha.image.wordtoimage.WordToImage word2image = new com.octo.captcha.image.wordtoimage.ComposedWordToImage(shearedFont, fileBack, randomPaster);
        this.addFactory(new com.octo.captcha.image.gimpy.GimpyFactory(dictionnaryWords, word2image));
    }
}
