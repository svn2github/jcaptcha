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
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * Portions of this software are based upon public domain software
 * originally written at the National Center for Supercomputing Applications,
 * University of Illinois, Urbana-Champaign.
 */

package com.octo.captcha.service;

import com.octo.captcha.Captcha;
import com.octo.captcha.CaptchaServiceException;

/**
 * Provides a way to temporally store captchas with a unique key
 */
public interface CaptchaStore
{
    /**
     * Check if a captcha is stored for this id
     * @param id
     * @return true if a captcha for this id is stored, false otherwise
     */
    boolean hasCaptcha(String id);

    /**
     * Store the captcha with the provided id as key.
     * The key is assumed to be unique, so if the same key is used twice to store a captcha, the store will return an
     * exception
     *
     * @param id the key
     * @param captcha the captcha
     * @throws CaptchaServiceException if the captcha already exists, or if an error occurs during storing routine.
     */
    void storeCaptcha(String id, Captcha captcha) throws CaptchaServiceException;

    /**
     * Retrieve the captcha for this key from the store.
     * @param id
     * @return the captcha for this id
     * @throws CaptchaServiceException if a captcha for this key is not found or if
     * an error occurs during retrieving routine.
     */
    Captcha getCaptcha(String id) throws CaptchaServiceException;

     /**
     * Remove the captcha with the provided id as key.
     * @param id the key
      * @return true if found, false otherwise
      * @throws CaptchaServiceException if an error occurs during remove routine
     */
    boolean removeCaptcha(String id);
}
