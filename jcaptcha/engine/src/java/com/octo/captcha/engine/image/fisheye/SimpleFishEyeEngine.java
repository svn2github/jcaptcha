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
package com.octo.captcha.engine.image.fisheye;

import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.component.image.deformation.ImageDeformation;
import com.octo.captcha.component.image.deformation.ImageDeformationByFilters;
import com.octo.captcha.component.image.backgroundgenerator.FileReaderRandomBackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.image.fisheye.FishEyeFactory;

import java.awt.image.ImageFilter;

/**
 * Produce fishEye from files. FishEye are done from sphere
 *
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class SimpleFishEyeEngine extends ListImageCaptchaEngine {
    /**
     * this method should be implemented as folow :
     * <ul>
     * <li>First construct all the factories you want to initialize the gimpy with</li>
     * <li>then call the this.addFactoriy method for each factory</li>
     * </ul>
     */
    protected void buildInitialFactories() {
        //build filters
        com.jhlabs.image.SphereFilter sphere = new com.jhlabs.image.SphereFilter();
        com.jhlabs.image.RippleFilter ripple = new com.jhlabs.image.RippleFilter();
        com.jhlabs.image.TwirlFilter twirl = new com.jhlabs.image.TwirlFilter();
        com.jhlabs.image.WaterFilter water = new com.jhlabs.image.WaterFilter();

        ripple.setWaveType(com.jhlabs.image.RippleFilter.NOISE);
        ripple.setXAmplitude(10);
        ripple.setYAmplitude(10);
        ripple.setXWavelength(10);
        ripple.setYWavelength(10);
        ripple.setEdgeAction(com.jhlabs.image.TransformFilter.CLAMP);

        water.setAmplitude(10);
        water.setAntialias(true);
        water.setWavelength(20);

        twirl.setAngle(4);


        sphere.setRefractionIndex(2);



        ImageDeformation rippleDef = new ImageDeformationByFilters(new ImageFilter[]{ripple});
        ImageDeformation sphereDef = new ImageDeformationByFilters(new ImageFilter[]{sphere});
        ImageDeformation waterDef = new ImageDeformationByFilters(new ImageFilter[]{water});
        ImageDeformation twirlDef = new ImageDeformationByFilters(new ImageFilter[]{twirl});


        //add background from files
        BackgroundGenerator generator = new FileReaderRandomBackgroundGenerator(new Integer(300),new Integer(300),
                "./core/src/conf/images");
        addFactory(new FishEyeFactory(generator,sphereDef,new Integer(30),new Integer(11)));
        addFactory(new FishEyeFactory(generator,rippleDef,new Integer(30),new Integer(11)));
        addFactory(new FishEyeFactory(generator,waterDef,new Integer(30),new Integer(11)));
        addFactory(new FishEyeFactory(generator,twirlDef,new Integer(30),new Integer(11)));

    }
}
