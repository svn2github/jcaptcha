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

package com.octo.captcha.image.gimpy.wordtoimage.backgroundgenerator;

import com.octo.captcha.CaptchaException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>File reader background generator that return a random image from the ones found in the directory
 * NOTE : this implementation only takes </p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class FileReaderRandomBackgroundGenerator extends AbstractBackgroundGenerator
{

    private List images = new ArrayList();
    private String rootPath = ".";

    public FileReaderRandomBackgroundGenerator(Integer width, Integer height, String rootPath)
    {
        super(width, height);
        //this.images=images;
        if (rootPath != null) this.rootPath = rootPath;
//        if(images==null||images.length == 0){
//            throw new CaptchaException("Can't be initialized with a null or empty array of images");
//        }
        File dir = new File(this.rootPath);
        if (!dir.canRead() || !dir.isDirectory())
        {
            throw new CaptchaException("Root path is not a directory or cannot be read");
        } else
        {
            File[] files = dir.listFiles();

            //get all jpeg
            if (files != null)
            {
                for (int i = 0 ; i < files.length ; i++)
                {
                    File file = files[i];
                    BufferedImage out = null;
                    if (file.isFile())
                    {
                        out = getImage(file);
                    }
                    if (out != null)
                    {
                        images.add(images.size(), out);
                    }
                }

            }
            if (images.size() != 0)
            {
                for (int i = 0 ; i < images.size() ; i++)
                {
                    BufferedImage bufferedImage = (BufferedImage) images.get(i);
                    images.set(i, tile(bufferedImage));
                }
            } else
            {
                throw new CaptchaException("Root path directory is valid but " +
                        "does not contains any image files");
            }
        }
    }

    private BufferedImage tile(BufferedImage tileImage)
    {
        BufferedImage image = new BufferedImage(getImageWidth(), getImageHeight(), tileImage.getType());
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        int NumberX = (getImageWidth() / tileImage.getWidth());
        int NumberY = (getImageHeight() / tileImage.getHeight());
        for (int k = 0 ; k <= NumberY ; k++)
        {
            for (int l = 0 ; l <= NumberX ; l++)
            {
                g2.drawImage(tileImage, l * tileImage.getWidth(), k * tileImage.getHeight(),
                        Math.min(tileImage.getWidth(), getImageWidth()), Math.min(tileImage.getHeight(), getImageHeight()), null);
            }
        }
        g2.dispose();
        return image;
    }

    // Returns the format name of the image in the object 'o'.
    // 'o' can be either a File or InputStream object.
    // Returns null if the format is not known.
    private static BufferedImage getImage(File o)
    {
        BufferedImage out = null;
        try
        {
            // Create an image input stream on the image
            ImageInputStream iis = ImageIO.createImageInputStream(o);

            // Find all image readers that recognize the image format
            Iterator iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext())
            {
                // No readers found
                return out;
            }

            // Use the first reader
            ImageReader reader = (ImageReader) iter.next();
            reader.setInput(iis);
            out = reader.read(0);
            // Close stream
            iis.close();

            // Return the format name
            return out;
        } catch (IOException e)
        {
            throw new CaptchaException("Unknown error during file reading ", e);
        }
    }

    /**
     * Generates a backround image on wich text will be paste.
     * Implementations must take into account the imageHeigt and imageWidth.
     * @return the background image
     */
    public BufferedImage getBackround()
    {
        return (BufferedImage) images.get(myRandom.nextInt(images.size()));
    }

}
