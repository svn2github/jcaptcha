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

package com.octo.captcha.pix.gimpy.wordtoimages.textpasters;

import com.octo.captcha.pix.gimpy.wordtoimages.AbstractTextPaster;
import com.octo.captcha.pix.gimpy.wordtoimages.utils.PixelsWarper;
import com.octo.captcha.CaptchaException;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.text.AttributedString;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class TwinstingTextPaster extends AbstractTextPaster {

    public TwinstingTextPaster(Integer maxAcceptedWordLenght, Integer minAcceptedWordLenght) {
        super(maxAcceptedWordLenght, minAcceptedWordLenght);
    }

    /**
     * Pastes the attributed string on the backround image and return the final image.
     * Implementation must take into account the fact that the text must be readable
     * by human and non by programs
     * @param background
     * @param attributedWord
     * @return the final image
     * @throws CaptchaException if any exception accurs during paste routine.
     */
    public BufferedImage pasteText(BufferedImage background, AttributedString attributedWord) throws CaptchaException {
        //create a simple image from AS
        BufferedImage buf = new BufferedImage(background.getWidth(), background.getHeight(),background.getType());
        Graphics2D g2 = (Graphics2D) buf.getGraphics();
        g2.drawString(attributedWord.getIterator(),5,buf.getHeight()/2);
        g2.dispose();
        //generate a pixel grabber
        PixelGrabber grabber = new PixelGrabber(buf,0,0,buf.getWidth(), buf.getHeight(),false );
        //sets grabers attributes
        grabber.setDimensions(buf.getWidth(), buf.getHeight());
        //grab!
        int fromPixels[] = (int[]) grabber.getPixels();
        //uses a pixel warper
        int toPixels[] = new int[buf.getWidth()*buf.getHeight()];
        Point fromWarp = new Point(5,buf.getHeight()/2);
        Point toWarp = new Point(5+3,buf.getHeight()/2+3);

        PixelsWarper warper = new PixelsWarper(fromPixels,toPixels,buf.getWidth(),buf.getHeight(),toWarp, fromWarp);

        return null;
    }



}
