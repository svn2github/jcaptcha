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
package com.octo.captcha.image.gimpy;

import com.octo.captcha.image.ListImageCaptchaEngine;
import java.awt.Color;
import java.awt.image.ImageFilter;

import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.TransformFilter;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.gimpy.wordgenerator.DictionaryWordGenerator;
import com.octo.captcha.image.gimpy.wordgenerator.FileDictionnary;
import com.octo.captcha.image.gimpy.wordtoimage.BackgroundGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.FilteredComposedWordToImage;
import com.octo.captcha.image.gimpy.wordtoimage.FontGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.TextPaster;
import com.octo.captcha.image.gimpy.wordtoimage.backgroundgenerator.MultipleShapeBackgroundGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.fontgenerator.DeformedRandomFontGenerator;
import com.octo.captcha.image.gimpy.wordtoimage.textpaster.DoubleRandomTextPaster;
/**
 * <p>Description: </p>
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class FilteredDoubleRandomListGimpyEngine
    extends ListImageCaptchaEngine {

    protected void buildInitialFactories() {
        
         RippleFilter rippleBack = new RippleFilter();
         

         rippleBack.setWaveType(RippleFilter.NOISE);
         rippleBack.setXAmplitude(5);
         rippleBack.setYAmplitude(5);
         rippleBack.setXWavelength(10);
         rippleBack.setYWavelength(10);
         rippleBack.setEdgeAction(TransformFilter.CLAMP);

         

         TextPaster paster =
             new DoubleRandomTextPaster(
                new Integer(8),new Integer(15),Color.BLACK);
         BackgroundGenerator back = new MultipleShapeBackgroundGenerator(
                new Integer(200),new Integer(100));
         FontGenerator font =
                new DeformedRandomFontGenerator(
                    new Integer(25),new Integer(27));
         WordGenerator words =
             new DictionaryWordGenerator(new FileDictionnary("toddlist"));
         
         WordToImage word2image =
             new FilteredComposedWordToImage(
                 font,
                 back,
                 paster,
                 new ImageFilter[] { rippleBack },
                 new ImageFilter[] {},
                 new ImageFilter[] {});
        ImageCaptchaFactory factory = new GimpyFactory(words, word2image);
         this.addFactory(factory);
    }

}
