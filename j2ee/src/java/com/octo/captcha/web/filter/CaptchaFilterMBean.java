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
 *
 */
package com.octo.captcha.web.filter;

/**
 * JMX management inteface for CaptchaFilter
 *
 * @version $Id$
 * 
 * @author <a href="mailto:sbr@octo.com">Sebastien Brunot</a>
 */
public interface CaptchaFilterMBean
{
    /**
     * Get the fully qualified name of the WebCaptcha implementation that is
     * used by the filter
     * @return the fully qualified name of the WebCaptcha implementation that
     * is ised by the filter
     */
    String getCaptchaImplementation();

    /**
     * Set the fully qualified name of the WebCaptcha implementation that is
     * used by the filter
     * @param theClassName the fully qualified name of the WebCaptcha
     * implementation that is ised by the filter
     * @throws IllegalArgumentException if className can't be used as the
     * captcha implementation, either because it can't be instanciated by the
     * factory or it is not a WebCaptcha implementation.
     */
    void setCaptchaImplementation(String theClassName)
        throws IllegalArgumentException;

    /**
     * Get the capacity of the internal store
     * @return the capacity of the internal store
     */
    int getInternalStoreCapacity();

    /**
     * Get the current time to live for entries in the internal store
     * @return the time to live in milliseconds
     */
    int getTimeToLiveInMilliseconds();

    /**
     * Set a new time to live for entries in the internal store
     * @param theTimeToLive the time to live in milliseconds
     */
    void setTimeToLiveInMilliseconds(int theTimeToLive);

    /**
     * Get the load of the internal store (number of current entries
     * / capacity of the store)
     * @return the load of the internal store expressed in percent
     */
    double getInternalStoreLoad();

    /**
     * Get the number of timeouted entries in the internal store
     * WARNING : this value won't be significant if the total number
     * is > Long.MAX_VALUE
     * @return the number of entries of the internal store that are timeouted
     */
    int getNumberOfTimeoutedEntriesInInternalStore();

    /**
     * Get the number of captcha generated since the Filter is up
     * WARNING : this value won't be significant if the total number
     * is > Long.MAX_VALUE
     * @return the number of captcha generated since the Filter is up
     */
    long getTotalNumberOfGeneratedCaptcha();

    /**
     * Get the number of captcha for wich the challenge was correctly answered
     * since the Filter is up
     * WARNING : this value won't be significant if the total number
     * is > Long.MAX_VALUE
     * @return the number of captcha for wich the challenge was correctly
     * answered since the Filter is up
     */
    long getTotalNumberOfCaptchaCorrectlyAnswered();

    /**
     * Get the number of captcha for wich the challenge was badly answered
     * since the Filter is up
     * WARNING : this value won't be significant if the total number
     * is > Long.MAX_VALUE
     * @return the number of captcha for wich the challenge was badly answered
     * since the Filter is up
     */
    long getTotalNumberOfCaptchaBadlyAnswered();

    /**
     * Get the number of captcha garbage collected since the Filter is up
     * WARNING : this value won't be significant if the total number
     * is > Long.MAX_VALUE
     * @return the number of captcha garbage collected since the Filter is up
     */
    long getTotalNumberOfGarbageCollectedCaptcha();

    /**
     * Get the maximum delay recorded between generation of a capatcha and the
     * response to its challenge by user since the Filter is up. This delay is
     * in millisecond.
     * WARNING : this value is significant only if 
     * totalNumberOfCaptchaCorrectlyAnswered + totalNumberOfCaptchaBadlyAnswered
     * > 0
     * @return the maximum delay recorded betwwen generation of a capatcha and
     * the response to his challenge by user since the Filter is up
     */
    int getMaximumCaptchaAnsweringDelayInMilliseconds();

    /**
     * Get the average delay recorded between generation of a capatcha and the
     * response to his challenge by user since the Filter is up. This delay is
     * in millisecond.
     * WARNING : this value is significant only if
     * totalNumberOfCaptchaCorrectlyAnswered + totalNumberOfCaptchaBadlyAnswered
     * > 0
     * @return the average delay recorded betwwen generation of a capatcha and
     * the response to his challenge by user since the Filter is up
     */
    int getAverageCaptchaAnsweringDelayInMilliseconds();

    /**
     * Garbage collect the internal store
     */
    void garbageCollectInternalStore();
}
