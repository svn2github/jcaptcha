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

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.Raster;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.text.AttributedString;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class WarpedTextPaster extends AbstractTextPaster{

    public WarpedTextPaster(Integer maxAcceptedWordLenght, Integer minAcceptedWordLenght) {
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
        //first create an image from text and warp it
        BufferedImage image = new BufferedImage(background.getWidth(),background.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.drawString(attributedWord.getIterator(),10,10);
        //crop
        Rectangle2D borders = g.getFontMetrics().getMaxCharBounds(g);
        g.setClip(borders);

        //generate warping points : middle plus height/10 in the 4 directions

        Point fromPoint = new Point();
        fromPoint.setLocation(borders.getCenterX(),borders.getCenterY());

        Point toPoint = new Point();
        toPoint.setLocation(fromPoint.getX()+(myRandom.nextDouble()%borders.getWidth()/10),
                       fromPoint.getY()+(myRandom.nextDouble()%borders.getHeight()/10) );
        //get pixels
        g.dispose();
        Raster raster = image.getRaster();
        WritableRaster warped = raster.createCompatibleWritableRaster();
        int[] fromPixels = ((DataBufferInt)raster.getDataBuffer()).getData();
        //warped pixels
        int[] toPixels = PixelsWarper.Warp(fromPixels,image.getWidth(),image.getHeight(), fromPoint,toPoint);
        //generate a new raster with pixels
        try {
            warped.setDataElements(0,0,toPixels);
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(e);
        }
        image.setData(warped);

        return image;
        //paste image on backgroud
//        Graphics2D gb  = (Graphics2D)background.getGraphics();
//
//        gb.drawImage(image,AffineTransform.getTranslateInstance(myRandom.nextInt(background.getWidth()),
//                myRandom.nextInt(background.getHeight())),null);
//        gb.dispose();
//
    }
}
