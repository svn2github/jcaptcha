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
package com.octo.captcha.component.image.wordtoimage;

import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.utils.ToolkitFactory;
import com.octo.captcha.component.image.deformation.ImageDeformation;
import com.octo.captcha.CaptchaException;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.text.AttributedString;

/**
 * <p>This implementation uses deformation components to distord the image.
 * </br>It takes three array of deformations : for the background image, for the text only, and for the final image
 * it proceeds as folows :
 * <ul>
 * <li>Checks the word lenght</li>
 * <li>Creates an java.text.AttributedString from the word</li>
 * <li>Create an image for the background a BackgroundGenerator component</li>
 * <li>Apply background deformations</li>
 * <li>Apply font to the AttributedString using the abstract method getFont</li>
 * <li>Create a transparent backround </li>
 * <li>Put the text on the transparent backround using the abstact method pasteText</li>
 * <li>Apply the text deformations </li>
 * <li>Paste the transparent image using an alpha composite</li>
 * <li>Apply the final deformations </li>
 * <li>Return the newly created image</li>
 * </ul>
 *
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class DeformedComposedWordToImage extends ComposedWordToImage{
        private ImageDeformation backgroundDeformation;
    private ImageDeformation textDeformation;
    private ImageDeformation finalDeformation;

    /**
     * Composed word to image that applys filters
     *
     * @param fontGenerator     a AbstractFontGenerator to implement the getFont() method
     * @param background        a AbstractBackgroundGenerator to implement the getBackround() method
     * @param textPaster        a AbstractTextParser to implement the pasteText() method
     * @param backgroundDeformation to be apply on the background image
     * @param textDeformation       to be apply on the text image
     * @param finalDeformation      to be apply on the final image
     */
    public DeformedComposedWordToImage(FontGenerator fontGenerator, BackgroundGenerator background,
                                       TextPaster textPaster,
                                       ImageDeformation backgroundDeformation, ImageDeformation textDeformation,
                                       ImageDeformation finalDeformation) {
        super(fontGenerator, background, textPaster);
        this.backgroundDeformation = backgroundDeformation;
        this.textDeformation = textDeformation;
        this.finalDeformation = finalDeformation;
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
     * @throws com.octo.captcha.CaptchaException if word is invalid or if image generation fails.
     */
    public BufferedImage getImage(String word) throws CaptchaException {
        BufferedImage background = getBackround();
        AttributedString aword = getAttributedString(word, checkWordLenght(word));
        //copy background
        BufferedImage out =
                new BufferedImage(background.getWidth(), background.getHeight(), background.getType());
        Graphics2D g2 = (Graphics2D) out.getGraphics();
        //paste background
        g2.drawImage(background, 0, 0, out.getWidth(), out.getHeight(), null);

        //apply filters to backround
        out =  backgroundDeformation.deformImage(out);

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

        //and apply deformation
        transparent  =  textDeformation.deformImage(transparent);


        // Set a composite with transparency.
        Composite c = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f);
        g2.setComposite(c);
        g2.drawImage(transparent, 0, 0, null);
        g2.dispose();
        //apply final deformation
        out = finalDeformation.deformImage(out);
        return out;
    }
}
