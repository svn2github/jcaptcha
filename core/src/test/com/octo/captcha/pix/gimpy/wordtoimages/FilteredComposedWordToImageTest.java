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
 
package com.octo.captcha.pix.gimpy.wordtoimages;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.awt.image.ReplicateScaleFilter;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.pix.gimpy.wordtoimages.backgroundgenerators.GradientBackgroundGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.fontgenerator.RandomFontGenerator;
import com.octo.captcha.pix.gimpy.wordtoimages.textpasters.SimpleTextPaster;

import junit.framework.TestCase;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class FilteredComposedWordToImageTest extends TestCase {

    private FilteredComposedWordToImage filteredComposedWordToImage;
    private Integer minAcceptedWordLength = new Integer(1);
    private Integer maxAcceptedWordLength = new Integer(10);
    private Integer imageHeight = new Integer(100);
    private Integer imageWidth = new Integer(100);
    private Integer minFontSize = new Integer(10);

    /**
     * Constructor for FilteredComposedWordToImageTest.
     * @param name
     */
    public FilteredComposedWordToImageTest(String name) {
        super(name);
    }

    public void setUp() {
        
        AbstractBackgroundGenerator background = new GradientBackgroundGenerator(this.imageHeight,this.imageWidth,Color.BLACK,Color.WHITE);
        AbstractFontGenerator fontGenerator = new RandomFontGenerator(this.minFontSize);
        AbstractTextPaster textPaster = new SimpleTextPaster(this.maxAcceptedWordLength,this.minAcceptedWordLength);
        
        ImageFilter backFilter = new ReplicateScaleFilter(background.getImageWidth(),
            background.getImageHeight());
        
        ImageFilter textFilter = new ReplicateScaleFilter(background.getImageWidth(),
                    background.getImageHeight());
        
        ImageFilter finalFilter = new ReplicateScaleFilter(background.getImageWidth(),
                    background.getImageHeight());
        
        ImageFilter[] backFilters = {backFilter
        };
        
        ImageFilter[] textFilters = {textFilter
        };
        
        ImageFilter[] finalFilters = {finalFilter
        };
        
        this.filteredComposedWordToImage = new FilteredComposedWordToImage(
            fontGenerator,
            background,
            textPaster,
            backFilters,
            textFilters,
            finalFilters
        );
    }

    public void testGetImage() throws CaptchaException {
        String test = "test";
        assertNotNull(this.filteredComposedWordToImage.getImage(test));
    }

    public void testGetImageNull() {
        try {
            BufferedImage test = this.filteredComposedWordToImage.getImage(null);
        } catch (CaptchaException e) {
            assertNotNull(e);
        }
    }

}
