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

package com.octo.captcha.image.gimpy.wordtoimage;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.image.utils.ToolkitFactory;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.text.AttributedString;

/**
 * <p>This implementation uses filters to distord the image.
 * </br>It takes three array of filters : for the background image, for the text only, and for the final image
 * it proceeds as folows :
 * <ul>
 * <li>Checks the word lenght</li>
 * <li>Creates an java.text.AttributedString from the word</li>
 * <li>Create an image for the background using the abstact method getBackround</li>
 * <li>Apply background filters</li>
 * <li>Apply font to the AttributedString using the abstract method getFont</li>
 * <li>Create a transparent backround </li>
 * <li>Put the text on the transparent backround using the abstact method pasteText</li>
 * <li>Apply the text filters </li>
 * <li>Paste the transparent image using an alpha composite</li>
 * <li>Apply the final filters </li>
 * <li>Return the newly created image</li>
 * </ul>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class FilteredComposedWordToImage extends ComposedWordToImage
{

    private ImageFilter[] backgroundFilters;
    private ImageFilter[] textFilters;
    private ImageFilter[] finalFilters;

    /**
     *Composed word to image that applys filters
     * @param fontGenerator a AbstractFontGenerator to implement the getFont() method
     * @param background a AbstractBackgroundGenerator to implement the getBackround() method
     * @param textPaster a AbstractTextParser to implement the pasteText() method

     * @param backgroundFilters to be apply on the background image
     * @param textFilters to be apply on the text image
     * @param finalFilters to be apply on the final image
     */
    public FilteredComposedWordToImage(FontGenerator fontGenerator, BackgroundGenerator background,
                                       TextPaster textPaster,
                                       ImageFilter[] backgroundFilters, ImageFilter[] textFilters,
                                       ImageFilter[] finalFilters)
    {
        super(fontGenerator, background, textPaster);
        this.backgroundFilters = backgroundFilters;
        this.textFilters = textFilters;
        this.finalFilters = finalFilters;
    }

    /**
     * Creates an image of the provided String
     * This method is a skeleton for creation algorithm.
     * it proceeds as folows :
     * <ul>
     * <li>Checks the word lenght</li>
     * <li>Creates an java.text.AttributedString from the word</li>
     * <li>Create an image for the background using the abstact method getBackround</li>
     * <li>Apply background filters</li>
     * <li>Apply font to the AttributedString using the abstract method getFont</li>
     * <li>Create a transparent backround </li>
     * <li>Put the text on the transparent backround using the abstact method pasteText</li>
     * <li>Apply the text filters </li>
     * <li>Paste the transparent image using an alpha composite</li>
     * <li>Apply the final filters </li>
     * <li>Return the newly created image</li>
     * </ul>
     *
     * @return an image representation of the word
     * @throws CaptchaException if word is invalid or if image generation fails.
     */
    public BufferedImage getImage(String word) throws CaptchaException
    {
        BufferedImage background = getBackround();
        AttributedString aword = getAttributedString(word, checkWordLenght(word));
        //copy background
        BufferedImage out =
                new BufferedImage(background.getWidth(), background.getHeight(), background.getType());
        Graphics2D g2 = (Graphics2D) out.getGraphics();
        //paste background
        g2.drawImage(background, 0, 0, out.getWidth(), out.getHeight(), null);

        //apply filters to backround
        applyFilters(out, backgroundFilters);

        //paste text on a transparent background
        BufferedImage transparent =
                new BufferedImage(out.getWidth(), out.getHeight(), out.getType());
        Graphics2D tpie = (Graphics2D) transparent.getGraphics();

        tpie.setBackground(Color.WHITE);
        tpie.clearRect(0, 0, out.getWidth(), out.getHeight());
        tpie.setPaint(Color.WHITE);
        tpie.dispose();
        //use textpaster to paste the text
        transparent = pasteText(transparent, aword);

        //and apply filters
        applyFilters(transparent, textFilters);


        // Set a composite with transparency.
        Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f);
        g2.setComposite(c);
        g2.drawImage(transparent, 0, 0, null);
        g2.dispose();
        //apply final filters
        applyFilters(out, finalFilters);
        return out;
    }

    private void applyFilters(BufferedImage image, ImageFilter[] filters)
    {
        FilteredImageSource filtered;
        if (filters != null)
        {
            for (int i = 0 ; i < filters.length ; i++)
            {
                ImageFilter backgroundFilter = filters[i];
                filtered = new FilteredImageSource(image.getSource(), backgroundFilter);
                Image temp = ToolkitFactory.getToolkit().createImage(filtered);
                image.getGraphics().drawImage(temp, 0, 0, Color.WHITE, null);
            }
        }
    }
}
