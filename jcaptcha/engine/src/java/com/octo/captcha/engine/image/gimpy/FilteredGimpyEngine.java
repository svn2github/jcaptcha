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

package com.octo.captcha.engine.image.gimpy;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;

import java.awt.*;
import java.awt.image.ImageFilter;

/**
 * <p>Static factory initializer, instanciates a ImageCaptchaFactory. This class
 * is a sample which demonstrate how to use teh jhlabs filter with the
 * Filtered</p>
 *
 * @author <a href="mailto:mag@jcaptcha.net">Marc-Antoine Garrigue</a>
 * @version 1.0
 * @deprecated 
 */
public class FilteredGimpyEngine
        extends com.octo.captcha.engine.image.DefaultImageCaptchaEngine
{

    static com.octo.captcha.image.ImageCaptchaFactory[] factories;

    static
    {
        //build filters
        com.jhlabs.image.EmbossFilter emboss = new com.jhlabs.image.EmbossFilter();
        com.jhlabs.image.SphereFilter sphere = new com.jhlabs.image.SphereFilter();
        com.jhlabs.image.RippleFilter rippleBack = new com.jhlabs.image.RippleFilter();
        com.jhlabs.image.RippleFilter ripple = new com.jhlabs.image.RippleFilter();
        com.jhlabs.image.TwirlFilter twirl = new com.jhlabs.image.TwirlFilter();
        com.jhlabs.image.WaterFilter water = new com.jhlabs.image.WaterFilter();
        com.jhlabs.image.MarbleFilter marble = new com.jhlabs.image.MarbleFilter();
        com.jhlabs.image.WeaveFilter weaves = new com.jhlabs.image.WeaveFilter();
        com.jhlabs.image.CrystalizeFilter crystal = new com.jhlabs.image.CrystalizeFilter();

        emboss.setBumpHeight(2.0f);

        ripple.setWaveType(com.jhlabs.image.RippleFilter.NOISE);
        ripple.setXAmplitude(10);
        ripple.setYAmplitude(3);
        ripple.setXWavelength(20);
        ripple.setYWavelength(10);
        ripple.setEdgeAction(com.jhlabs.image.TransformFilter.CLAMP);

        rippleBack.setWaveType(com.jhlabs.image.RippleFilter.NOISE);
        rippleBack.setXAmplitude(5);
        rippleBack.setYAmplitude(5);
        rippleBack.setXWavelength(10);
        rippleBack.setYWavelength(10);
        rippleBack.setEdgeAction(com.jhlabs.image.TransformFilter.CLAMP);

        water.setAmplitude(5);
        water.setAntialias(true);
        water.setWavelength(10);

        twirl.setAngle(3 / 360);

        sphere.setRefractionIndex(1);

        weaves.setUseImageColors(true);

        crystal.setScale(0.5f);
        crystal.setGridType(com.jhlabs.image.CrystalizeFilter.RANDOM);
        crystal.setFadeEdges(false);
        crystal.setEdgeThickness(0.2f);
        crystal.setRandomness(0.1f);

        TextPaster paster = new RandomTextPaster(new Integer(8),
                new Integer(10), Color.gray);
        BackgroundGenerator back = new FunkyBackgroundGenerator(
                new Integer(200), new Integer(100));
        FontGenerator font = new RandomFontGenerator(new Integer(25),
                new Integer(35));
        com.octo.captcha.component.wordgenerator.WordGenerator words = new com.octo.captcha.component.wordgenerator.DictionaryWordGenerator(
                new com.octo.captcha.component.wordgenerator.FileDictionnary(
                        "toddlist"));


        //build factories
        factories = new com.octo.captcha.image.ImageCaptchaFactory[3];
        com.octo.captcha.component.image.wordtoimage.WordToImage word2image = new com.octo.captcha.component.image.wordtoimage.FilteredComposedWordToImage(
                font, back, paster, new ImageFilter[]{water},
                new ImageFilter[]{emboss}, new ImageFilter[]{ripple});
        factories[0] =
                new com.octo.captcha.image.gimpy.GimpyFactory(words,
                        word2image);
        //select filters for 2
        word2image =
                new com.octo.captcha.component.image.wordtoimage.FilteredComposedWordToImage(
                        font, back, paster, new ImageFilter[]{rippleBack},
                        new ImageFilter[]{crystal}, new ImageFilter[]{ripple});
        factories[1] =
                new com.octo.captcha.image.gimpy.GimpyFactory(words,
                        word2image);
        //select filters for 3
        word2image =
                new com.octo.captcha.component.image.wordtoimage.FilteredComposedWordToImage(
                        font, back, paster, new ImageFilter[]{rippleBack},
                        new ImageFilter[]{}, new ImageFilter[]{weaves});
        factories[2] =
                new com.octo.captcha.image.gimpy.GimpyFactory(words,
                        word2image);

    }

    public FilteredGimpyEngine()
    {

        super(factories);

    }

}
