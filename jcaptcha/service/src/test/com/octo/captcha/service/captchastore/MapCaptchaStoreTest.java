package com.octo.captcha.service.captchastore;

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

import com.octo.captcha.Captcha;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.MockCaptcha;
import junit.framework.TestCase;

import java.util.Collection;

public class MapCaptchaStoreTest extends TestCase {
    MapCaptchaStore mapCaptchaStore;

    private MapCaptchaStore store;
    private Captcha capctha;
    public static final int SIZE = 10000;


    protected void setUp() throws Exception {
        super.setUp();
        store = new MapCaptchaStore();
        capctha = new MockCaptcha();
    }

    public void testMapCaptchaStore() throws Exception {
        new MapCaptchaStore();
    }

    public void testHasCaptcha() throws Exception {
        assertFalse("should not have", store.hasCaptcha("1"));
        store.storeCaptcha("2", capctha);
        store.storeCaptcha("1 ", capctha);
        store.storeCaptcha(" 1", capctha);
        assertFalse("should not have", store.hasCaptcha("1"));
        store.storeCaptcha("1", capctha);
        assertTrue("should", store.hasCaptcha("1"));

    }

    public void testStoreCaptcha() throws Exception {


        for (int i = 0; i < SIZE; i++) {
            store.storeCaptcha(String.valueOf(i), capctha);
        }
        for (int i = 0; i < SIZE; i++) {
            assertNotNull(store.getCaptcha(String.valueOf(i)));
        }

    }

    public void testRemoveCaptcha() throws Exception {

        for (int i = 0; i < SIZE; i++) {
            store.storeCaptcha(String.valueOf(i), capctha);

        }
        assertEquals("should have a size of " + SIZE, store.getSize(), SIZE);

        for (int i = 0; i < SIZE; i++) {
            assertTrue("Should be removed", store.removeCaptcha(String.valueOf(i)));
        }

        for (int i = 0; i < SIZE; i++) {
            assertFalse("Should not be removed", store.removeCaptcha(String.valueOf(i)));
        }

        assertTrue("should be empty now", store.getSize() == 0);

    }

    public void testGetSize() throws Exception {
        for (int i = 0; i < SIZE; i++) {
            store.storeCaptcha(String.valueOf(i), capctha);
            assertEquals("Size should be : " + i, i + 1, store.getSize());
        }
        assertEquals("should have a size of " + SIZE, store.getSize(), SIZE);

        for (int i = 0; i < SIZE; i++) {
            store.removeCaptcha(String.valueOf(i));
            assertEquals("Size should be : " + (SIZE - i - 1), SIZE - i - 1, store.getSize());
        }

    }

    public void testGetKeys() throws Exception {

        for (int i = 0; i < SIZE; i++) {
            store.storeCaptcha(String.valueOf(i), capctha);

        }
        Collection keys = store.getKeys();


        for (int i = 0; i < SIZE; i++) {
            assertTrue("store should have key ", keys.contains(String.valueOf(i)));
        }

        for (int i = 0; i < SIZE; i++) {
            store.removeCaptcha(String.valueOf(i));
        }
        assertTrue("keys should be empty", store.getKeys().size() == 0);
    }

    public void testGetCaptcha() throws Exception {
        for (int i = 0; i < SIZE; i++) {
            store.storeCaptcha(String.valueOf(i), capctha);

        }

        for (int i = 0; i < SIZE; i++) {
            assertEquals("store should a captcha for this key ", capctha, store.getCaptcha(String.valueOf(i)));
        }
        try {
            store.getCaptcha("unknown");
            fail("should throw an Exception");
        } catch (CaptchaServiceException e) {
            //ok
        }
    }

    public void testEmpty() throws Exception {
        for (int i = 0; i < SIZE; i++) {
            store.storeCaptcha(String.valueOf(i), capctha);
        }
        store.empty();
        assertEquals("Size should be 0", 0, store.getSize());
        assertTrue("keys should be empty", store.getKeys().size() == 0);

    }


}