/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

package com.octo.captcha.component.image.wordtoimage;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.deformation.ImageDeformation;
import com.octo.captcha.component.image.deformation.ImageDeformationByFilters;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.SimpleTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import junit.framework.TestCase;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.awt.image.ReplicateScaleFilter;

/**
 * <p>Description: </p>
 *
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 * @deprecated
 */
public class DeformedComposedWordToImageTest extends TestCase {

    private DeformedComposedWordToImage deformedComposedWordToImage;
    private Integer minAcceptedWordLength = new Integer(1);
    private Integer maxAcceptedWordLength = new Integer(10);
    private Integer imageHeight = new Integer(100);
    private Integer imageWidth = new Integer(100);
    private Integer minFontSize = new Integer(10);
    private Integer maxFontSize = new Integer(10);

    /**
     * Constructor for FilteredComposedWordToImageTest.
     */
    public DeformedComposedWordToImageTest(String name) {
        super(name);
    }

    public void setUp() {

        BackgroundGenerator background = new GradientBackgroundGenerator(this.imageHeight, this.imageWidth, Color.black, Color.white);
        FontGenerator fontGenerator = new RandomFontGenerator(this.minFontSize, this.maxFontSize);
        TextPaster textPaster = new SimpleTextPaster(this.minAcceptedWordLength, this.maxAcceptedWordLength, Color.blue);

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

        ImageDeformation back = new ImageDeformationByFilters(backFilters);
        ImageDeformation text = new ImageDeformationByFilters(textFilters);
        ImageDeformation finalD = new ImageDeformationByFilters(finalFilters);
        this.deformedComposedWordToImage = new DeformedComposedWordToImage(fontGenerator,
                background,
                textPaster,
                back,
                text,
                finalD);
    }

    public void testGetImage() throws CaptchaException {
        String test = "test";
        assertNotNull(this.deformedComposedWordToImage.getImage(test));
    }

    public void testGetImageNull() {
        try {
            BufferedImage test = this.deformedComposedWordToImage.getImage(null);
        } catch (CaptchaException e) {
            assertNotNull(e);
        }
    }

}
