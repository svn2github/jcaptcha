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
package com.octo.captcha.service;

import com.octo.captcha.Captcha;
import com.octo.captcha.service.captchastore.CaptchaStore;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.CaptchaEngine;

import java.util.*;

/**
 * This class provides default implementation for the management interface.
 * It uses an HashMap to store the timestamps for garbage collection.
 *
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public abstract class AbstractManageableCaptchaService extends AbstractCaptchaService
        implements AbstractManageableCaptchaServiceMBean {


    private int minGuarantedStorageDelayInSeconds;
    private int captchaStoreMaxSize;

    private int captchaStoreSizeBeforeGarbageCollection;

    private int numberOfGeneratedCaptchas = 0;
    private int numberOfCorrectResponse = 0;
    private int numberOfUncorrectResponse = 0;
    private int numberOfGarbageCollectedCaptcha = 0;

    private HashMap times;

    private long oldestCaptcha = 0;//OPTIMIZATION STUFF!


    protected AbstractManageableCaptchaService(CaptchaStore captchaStore, com.octo.captcha.engine.CaptchaEngine captchaEngine,
                                               int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize,
                                               int captchaStoreLoadBeforeGarbageCollection) {
        super(captchaStore, captchaEngine);
        if (maxCaptchaStoreSize < captchaStoreLoadBeforeGarbageCollection)
            throw new IllegalArgumentException("the max store size can't be less than garbage collection size. if you want to disable garbage" +
                    " collection (this is not recommended) you may set them equals (max=garbage)");
        this.setCaptchaStoreMaxSize(maxCaptchaStoreSize);
        this.setMinGuarantedStorageDelayInSeconds(minGuarantedStorageDelayInSeconds);
        this.setCaptchaStoreSizeBeforeGarbageCollection(captchaStoreLoadBeforeGarbageCollection);
        times = new HashMap();
    }


    /**
     * Get the fully qualified class name of the concrete CaptchaEngine
     * used by the service.
     *
     * @return the fully qualified class name of the concrete CaptchaEngine
     *         used by the service.
     */
    public String getCaptchaEngineClass() {
        return this.engine.getClass().getName();
    }

    /**
     * Set the fully qualified class name of the concrete CaptchaEngine
     * used by the service
     *
     * @param theClassName the fully qualified class name of the
     *                     CaptchaEngine used by the service
     * @throws IllegalArgumentException if className can't be used as the
     *                                  service CaptchaEngine, either because it can't be instanciated
     *                                  by the service or it is not a ImageCaptchaEngine concrete class.
     */
    public void setCaptchaEngineClass(String theClassName) throws IllegalArgumentException {
        try {
            Object engine = Class.forName(theClassName).newInstance();
            if (engine instanceof com.octo.captcha.engine.CaptchaEngine) {
                this.engine = (com.octo.captcha.engine.CaptchaEngine) engine;
            } else {
                throw new IllegalArgumentException("Class is not instance of CaptchaEngine! " + theClassName);
            }
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Get the minimum delay (in seconds) a client can
     * be assured that a captcha generated by the service
     * can be retrieved and a response to its challenge
     * tested
     *
     * @return the maximum delay in seconds
     */
    public int getMinGuarantedStorageDelayInSeconds() {
        return minGuarantedStorageDelayInSeconds;
    }

    /**
     * set the minimum delay (in seconds)a client can
     * be assured that a captcha generated by the service
     * can be retrieved and a response to its challenge
     * tested
     *
     * @param theMinGuarantedStorageDelayInSeconds
     *         the
     *         minimum guaranted delay
     */
    public void setMinGuarantedStorageDelayInSeconds(int theMinGuarantedStorageDelayInSeconds) {
        this.minGuarantedStorageDelayInSeconds = theMinGuarantedStorageDelayInSeconds;
    }


    /**
     * Get the number of captcha generated since the service is up
     * WARNING : this value won't be significant if the real number
     * is > Long.MAX_VALUE
     *
     * @return the number of captcha generated since the service is up
     */
    public long getNumberOfGeneratedCaptchas() {
        return numberOfGeneratedCaptchas;
    }

    /**
     * Get the number of correct responses to captcha challenges since
     * the service is up.
     * WARNING : this value won't be significant if the real number
     * is > Long.MAX_VALUE
     *
     * @return the number of correct responses since the service is up
     */
    public long getNumberOfCorrectResponses() {
        return numberOfCorrectResponse;
    }

    /**
     * Get the number of uncorrect responses to captcha challenges since
     * the service is up.
     * WARNING : this value won't be significant if the real number
     * is > Long.MAX_VALUE
     *
     * @return the number of uncorrect responses since the service is up
     */
    public long getNumberOfUncorrectResponses() {
        return numberOfUncorrectResponse;
    }

    /**
     * Get the curent size of the captcha store
     *
     * @return the size of the captcha store
     */
    public int getCaptchaStoreSize() {
        return this.store.getSize();
    }

    /**
     * Get the number of captchas that can be garbage collected in
     * the captcha store
     *
     * @return the number of captchas that can be garbage collected
     *         in the captcha store
     */
    public int getNumberOfGarbageCollectableCaptchas() {
        return getGarbageCollectableCaptchaIds(System.currentTimeMillis()).size();
    }


    /**
     * Get the number of captcha garbage collected since the service is up
     * WARNING : this value won't be significant if the real number
     * is > Long.MAX_VALUE
     *
     * @return the number of captcha garbage collected since the service is up
     */
    public long getNumberOfGarbageCollectedCaptcha() {
        return numberOfGarbageCollectedCaptcha;
    }

    /**
     * @return the max captchaStore load before garbage collection of the store
     */
    public int getCaptchaStoreSizeBeforeGarbageCollection() {
        return captchaStoreSizeBeforeGarbageCollection;
    }

    /**
     * max captchaStore size before garbage collection of the store
     *
     * @param captchaStoreSizeBeforeGarbageCollection
     *
     */
    public void setCaptchaStoreSizeBeforeGarbageCollection(int captchaStoreSizeBeforeGarbageCollection) {
        if (this.captchaStoreMaxSize < captchaStoreSizeBeforeGarbageCollection)
            throw new IllegalArgumentException("the max store size can't be less than garbage collection size. if you want to disable garbage" +
                    " collection (this is not recommended) you may set them equals (max=garbage)");

        this.captchaStoreSizeBeforeGarbageCollection =
                captchaStoreSizeBeforeGarbageCollection;
    }

    /**
     * This max size is used by the service : it will throw a CaptchaServiceException if the
     * store is full when a client ask for a captcha.
     *
     * @param size
     */
    public void setCaptchaStoreMaxSize(int size) {
        if (size < this.captchaStoreSizeBeforeGarbageCollection)
            throw new IllegalArgumentException("the max store size can't be less than garbage collection size. if you want to disable garbage" +
                    " collection (this is not recommended) you may set them equals (max=garbage)");
        this.captchaStoreMaxSize = size;
    }

    /**
     * @return the desired max size of the captcha store
     */
    public int getCaptchaStoreMaxSize() {
        return this.captchaStoreMaxSize;
    }

    /**
     * Garbage collect the captcha store, means all old capthca (captcha in the store wich has been stored
     * more than the MinGuarantedStorageDelayInSecond
     */
    public void garbageCollectCaptchaStore() {
        // this may cause a captcha disparition if a new captcha is asked between
        // this call and the effective removing from the store!
        long now = System.currentTimeMillis();
        long limit = now - 1000 * minGuarantedStorageDelayInSeconds;

        //construct a new collection in order to avoid iterations synchronization pbs :
        Iterator ids = getGarbageCollectableCaptchaIds(now).iterator();
        while (ids.hasNext()) {
            String id =  ids.next().toString();
            if (((Long) times.get(id)).longValue() < limit) {
                //remove from times
                times.remove(id);
                //remove from ids
                store.removeCaptcha(id);
                //update stats
                this.numberOfGarbageCollectedCaptcha++;
            }
        }
    }


    /**
     * Empty the Store
     */
    public void emptyCaptchaStore() {
        //empty the store
        this.store.empty();
        //And the timestamps
        this.times = new HashMap();
    }


    private Collection getGarbageCollectableCaptchaIds(long now) {

          //construct a new collection in order to avoid iterations synchronization pbs :
          // this may cause a captcha disparition if a new captcha is asked between
          // this call and the effective removing from the store!
          HashSet garbageCollectableCaptchas = new HashSet();

          //the time limit under which captchas are collectable
          long limit = now - 1000 * getMinGuarantedStorageDelayInSeconds();
          if(limit>oldestCaptcha){
              // iterate to find out if the captcha is perimed
              Iterator ids = new HashSet(times.keySet()).iterator();
              while (ids.hasNext()) {
                  String id = (String) ids.next();
                  long captchaDate = ((Long) times.get(id)).longValue();
                  oldestCaptcha = Math.min(captchaDate, oldestCaptcha==0?captchaDate:oldestCaptcha);
                  if (captchaDate < limit) {
                      garbageCollectableCaptchas.add(id);
                  }
              }
          }
          return garbageCollectableCaptchas;
      }
 


    //*******
    ///Overriding business methods to add some stats and store management hooks
    ///****

    protected Captcha generateAndStoreCaptcha(Locale locale, String ID) {
        long now = System.currentTimeMillis();

        //if the store is full try to garbage collect
        if (isCaptchaStoreFull()) {
            //see if possible
            if (getGarbageCollectableCaptchaIds(now).size() > 0) {
                //possible collect an rerun
                garbageCollectCaptchaStore();
                return this.generateAndStoreCaptcha(locale, ID);
            } else {
                //impossible ! has to wait
                throw new CaptchaServiceException("Store is full, try to increase CaptchaStore Size or" +
                        "to dercrease time out, or to decrease CaptchaStoreSizeBeforeGrbageCollection");
            }
        }

        if (isCaptchaStoreQuotaReached()) {
            //then garbage collect
            garbageCollectCaptchaStore();
        }
        return generateCountTimeStampAndStoreCaptcha(ID, locale);
    }

    private Captcha generateCountTimeStampAndStoreCaptcha(String ID, Locale locale) {
        //update stats
        numberOfGeneratedCaptchas++;
        //mark as now
        Long now = new Long(System.currentTimeMillis());
        //store in my timestampeds ids
        this.times.put(ID, now);
        //retrieve and store cpatcha
        Captcha captcha = super.generateAndStoreCaptcha(locale, ID);
        return captcha;
    }


    private boolean isCaptchaStoreFull() {
        return getCaptchaStoreMaxSize() == 0 ? false : getCaptchaStoreSize() >= getCaptchaStoreMaxSize();
    }

    private boolean isCaptchaStoreQuotaReached() {
        return getCaptchaStoreSize() >= getCaptchaStoreSizeBeforeGarbageCollection();
    }

    /**
     * Method to validate a response to the challenge corresponding to the given ticket and remove the coresponding
     * captcha from the store.
     *
     * @param ID the ticket provided by the buildCaptchaAndGetID method
     * @return true if the response is correct, false otherwise.
     * @throws CaptchaServiceException if the ticket is invalid
     */
    public Boolean validateResponseForID(String ID, Object response) throws CaptchaServiceException {

        Boolean valid = super.validateResponseForID(ID, response);
        //remove from local after because validate may throw an exception if id is not found
        this.times.remove(ID);
        //update stats
        if (valid.booleanValue()) {
            numberOfCorrectResponse++;
        } else {
            numberOfUncorrectResponse++;
        }
        return valid;
    }


}
