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

package com.octo.captcha.image.gimpy;

import com.octo.captcha.image.ImageCaptcha;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class Gimpy extends ImageCaptcha implements Serializable
{

    private String response;

    Gimpy(String question, BufferedImage challenge, String response)
    {
        super(question, challenge);
        this.response = response;
    }

    /**
     * Validation routine from the CAPTCHA interface. this methods verify if the response is not null and a String
     * and then compares the given response to the internal string.
     * @param response
     * @return true if the given response equals the internal response, false otherwise.
     */
    public final Boolean validateResponse(final Object response)
    {
        return (null != response && response instanceof String) ? validateResponse((String) response) : Boolean.FALSE;
    }

    /**
     * Very simple validation routine that compares the given response to the internal string.
     * @return true if the given response equals the internal response, false otherwise.
     */
    private final Boolean validateResponse(final String response)
    {
        return Boolean.valueOf(response.equals(this.response));
    };

    /**
     *
     * @param out
     * @throws java.io.IOException
     */
    public void writeObject(final ObjectOutputStream out) throws IOException
    {
        //write serializable values
        out.writeUTF(this.question);
        out.writeUTF(response);
        //write image as jpeg
        final JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(this.getImageChallenge());

    }

    /**
     *
     * @param in
     * @throws java.io.IOException
     */
    public void readObject(final ObjectInputStream in) throws IOException
    {
        //read serializable values
        this.question = in.readUTF();
        this.response = in.readUTF();
        // read image as JPEG object
        final JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
        super.challenge = decoder.decodeAsBufferedImage();
    }
}
