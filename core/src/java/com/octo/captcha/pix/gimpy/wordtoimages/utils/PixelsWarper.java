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

package com.octo.captcha.pix.gimpy.wordtoimages.utils;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class PixelsWarper {



        Point			fromPoint, toPoint;
        int				fromPixels[], toPixels[];
        int				width, height;			// width & height of warp image


    public PixelsWarper(int fromPixels[], int toPixels[], int w, int h, Point fromPoint, Point toPoint) {
        this.fromPixels = fromPixels;
        this.toPixels = toPixels;
        this.fromPoint = fromPoint;
        this.toPoint = toPoint;
        width = w;
        height = h;
    }

        public static int[] Warp( int fromPixels[],int w, int h, Point fromPoint, Point toPoint ){
            int[] out = new int[w*h];
            PixelsWarper warper = new PixelsWarper(fromPixels, out,w, h, fromPoint, toPoint);
            //warper.WarpPixels();
            return warper.toPixels;
        }

//        public PixelsWarper( int fromPixels[], int toPixels[], int w, int h, Point fromPoint, Point toPoint )
//        {
//            this.fromPixels = fromPixels;
//            this.toPixels = toPixels;
//            this.fromPoint = fromPoint;
//            this.toPoint = toPoint;
//            width = w;
//            height = h;
//        }

    // warp a quadrilateral into a rectangle (double-secret magic code!)
    void WarpRegion(Rectangle fromRect, Point nw, Point ne, Point sw, Point se) {
        int dx = fromRect.width, dy = fromRect.height;
        double invDX = 1.0 / dx, invDY = 1.0 / dy;

        for (int a = 0; a < dx; a++) {
            double aa = a * invDX;
            double x1 = ne.x + (nw.x - ne.x) * aa;
            double y1 = ne.y + (nw.y - ne.y) * aa;
            double x2 = se.x + (sw.x - se.x) * aa;
            double y2 = se.y + (sw.y - se.y) * aa;

            double xin = x1;
            double yin = y1;
            double dxin = (x2 - x1) * invDY;
            double dyin = (y2 - y1) * invDY;
            int toPixel = fromRect.x + a + fromRect.y * width;

            for (int b = 0; b < dy; b++) {
                if (xin < 0) xin = 0;
                if (xin >= width) xin = width - 1;
                if (yin < 0) yin = 0;
                if (yin >= height) yin = height - 1;

                int pixelValue = fromPixels[(int) xin + (int) yin * width];
                toPixels[toPixel] = pixelValue;

                xin += dxin;
                yin += dyin;
                toPixel += width;
            }
        }
    }

    void ClipRect(Rectangle r, int w, int h) {
        if (r.x < 0) {
            r.width += r.x;
            r.x = 0;
        }
        if (r.y < 0) {
            r.height += r.y;
            r.y = 0;
        }
        if (r.x + r.width >= w) r.width = w - r.x - 1;
        if (r.y + r.height >= h) r.height = h - r.y - 1;
    }

    // SetRect and SetPt are Mac OS functions. I wrote my own versions here
    // so I didn't have to rewrite too much of the code.

    void SetRect(Rectangle r, int left, int top, int right, int bottom) {
        r.x = left;
        r.y = top;
        r.width = right - left;
        r.height = bottom - top;
    }

    void SetPt(Point pt, int x, int y) {
        pt.x = x;
        pt.y = y;
    }

}


