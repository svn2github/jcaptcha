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

 */

package com.octo.captcha.pix;

import com.octo.captcha.pix.gimpy.GimpyFactory;
import com.octo.captcha.pix.gimpy.WordGenerator;
import com.octo.captcha.pix.gimpy.WordToImage;
import com.octo.captcha.pix.gimpy.wordgenerators.DictionaryWordGenerator;
import com.octo.captcha.pix.gimpy.wordgenerators.FileDictionnary;
import com.octo.captcha.pix.gimpy.wordtoimages.AbstractBackgroundGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.AbstractFontGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.AbstractTextPaster;
import com.octo.captcha.pix.gimpy.wordtoimages.ComposedWordToImage;
import com.octo.captcha.pix.gimpy.wordtoimages.FilteredComposedWordToImage;
import com.octo.captcha.pix.gimpy.wordtoimages.backgroundgenerators.FunkyBackgroundGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.backgroundgenerators.MultipleShapeBackgroundGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.backgroundgenerators.EllipseBackgroundGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.fontgenerator.RandomFontGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.textpasters.DoubleTextPaster;
import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.MarbleFilter;
import com.jhlabs.image.WaterFilter;
import com.jhlabs.image.TwirlFilter;
import com.jhlabs.image.SphereFilter;
import com.jhlabs.image.EmbossFilter;
import com.jhlabs.image.TransformFilter;

import java.awt.image.ImageFilter;

/**
 * <p>Static factory initializer, instanciates a PixCaptchaFactory.
 * This class is a sample which demonstrate how to obtain a factory </p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class FilteredPixCaptchaEngine extends PixCaptchaEngine {

    public PixCaptchaFactory getPixCaptchaFactory(){

        EmbossFilter emboss = new EmbossFilter();
        SphereFilter sphere= new SphereFilter();
        RippleFilter rippleBack= new RippleFilter();
        RippleFilter ripple= new RippleFilter();
        TwirlFilter twirl= new TwirlFilter();
        WaterFilter water = new WaterFilter();
        MarbleFilter marble= new MarbleFilter();

        ripple.setWaveType(RippleFilter.NOISE);
        ripple.setXAmplitude(3);
        ripple.setYAmplitude(3);
        ripple.setXWavelength(10);
        ripple.setYWavelength(10);
        ripple.setEdgeAction(TransformFilter.CLAMP);

            rippleBack.setWaveType(RippleFilter.NOISE);
                rippleBack.setXAmplitude(5);
                rippleBack.setYAmplitude(5);
                rippleBack.setXWavelength(10);
                rippleBack.setYWavelength(10);
                rippleBack.setEdgeAction(TransformFilter.CLAMP);


        water.setAmplitude(5);
        water.setAntialias(true);
        water.setWavelength(10);

        twirl.setAngle(3/360);

        //sphere.setDimensions(10,10);
        sphere.setRefractionIndex(25);

        ImageFilter[] backgroundFilters = {water};
        ImageFilter[] textFilters = {ripple};
        ImageFilter[] finalFilters = {};



        AbstractTextPaster paster = new DoubleTextPaster(new Integer(8),new Integer(8));
        AbstractBackgroundGenerator back = new MultipleShapeBackgroundGenerator(new Integer(100),new Integer(200));
        AbstractFontGenerator font = new RandomFontGenerator(new Integer(18));
        WordGenerator words = new DictionaryWordGenerator(new FileDictionnary("toddlist"));
        WordToImage word2image = new FilteredComposedWordToImage(font, back, paster,backgroundFilters,
                textFilters, finalFilters);
        PixCaptchaFactory factory = new GimpyFactory(words, word2image);
        return factory;
    }
}
