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

package com.octo.captcha.image.wordtoimage.textpaster;

import com.octo.captcha.CaptchaException;
import junit.framework.TestCase;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.AttributedString;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class BaffleRandomTextPasterTest extends TestCase {

    protected BaffleRandomTextPaster baffleRandomTextPaster;
    protected Integer minAcceptedWordLength = new Integer(10);
    protected Integer maxAcceptedWordLength = new Integer(10);
    protected Integer numberHoles = new Integer(10);
    protected Color textColor = Color.BLACK;
    protected Color holesColor = Color.WHITE;
    /**
     * Constructor for BaffleRandomTextPasterTest.
     * @param name
     */
    public BaffleRandomTextPasterTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.baffleRandomTextPaster = new BaffleRandomTextPaster(
            this.minAcceptedWordLength,
            this.maxAcceptedWordLength,
            this.textColor,
            this.numberHoles,
            this.holesColor
            );
    }

    public void testPasteText() {
        BufferedImage imageTest = new BufferedImage(100,50,BufferedImage.TYPE_INT_RGB);
        AttributedString stringTest = new AttributedString("test");
        BufferedImage test = null;
        try
        {
            test = this.baffleRandomTextPaster.pasteText(imageTest,stringTest);
            assertNotNull(test);
            assertEquals(imageTest.getHeight(),test.getHeight());
            assertEquals(imageTest.getWidth(),test.getWidth());
        }catch(CaptchaException e) {
            assertNotNull(e);
        }
    }

    public void testGetTextColor() {
        Color colorTest = this.baffleRandomTextPaster.getTextColor();
        assertNotNull(colorTest);
    }

    public void testGetMaxAcceptedWordLenght() {
        assertEquals(this.maxAcceptedWordLength.intValue(),
            this.baffleRandomTextPaster.getMaxAcceptedWordLenght());
    }

    public void testGetMinAcceptedWordLenght() {
        assertEquals(this.minAcceptedWordLength.intValue(),
            this.baffleRandomTextPaster.getMinAcceptedWordLenght());
    }

}
