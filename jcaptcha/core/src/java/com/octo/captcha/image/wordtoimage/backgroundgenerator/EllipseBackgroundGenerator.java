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

package com.octo.captcha.image.wordtoimage.backgroundgenerator;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

/**
 * <p>Black ellipses drawn on a white background</p>
 *
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class EllipseBackgroundGenerator extends AbstractBackgroundGenerator {

    public EllipseBackgroundGenerator(Integer width, Integer height) {
        super(width, height);
    }

    /**
     * Generates a backround image on wich text will be paste.
     * Implementations must take into account the imageHeigt and imageWidth.
     *
     * @return the background image
     */
    public BufferedImage getBackround() {
        BufferedImage bimgTP = new BufferedImage(getImageWidth(), getImageHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bimgTP.createGraphics();
        // g2d.setColor(Color.white);
        //g2d.fillRect(0, 0, getImageWidth(), getImageHeight());
        BasicStroke bs = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 2.0f, new float[]{2.0f, 2.0f}, 0.0f);
        g2d.setStroke(bs);
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f);
        g2d.setComposite(ac);

        g2d.translate(getImageWidth() * -1.0, 0.0);
        double delta = 5.0;
        double xt;
        double ts = 0.0;
        for (xt = 0.0; xt < (2.0 * getImageWidth()); xt += delta) {
            Arc2D arc = new Arc2D.Double(0, 0,
                    getImageWidth(), getImageHeight(), 0.0, 360.0, Arc2D.OPEN);
            g2d.draw(arc);
            g2d.translate(delta, 0.0);
            ts += delta;
        }
        return bimgTP;
    }
}
