/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * Copyright (c) 2005 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */
package com.octo.captcha.service;


import net.sf.ehcache.CacheManager;

public class EhcacheManageableCaptchaServiceTest extends AbstractManageableCaptchaServiceTest {
    EhcacheManageableCaptchaService ehcacheManageableCaptchaService;

    protected void setUp() throws Exception {
        this.service = new MockedEhCacheManageableCaptchaService(new MockCaptchaEngine(),
                MIN_GUARANTED_STORAGE_DELAY_IN_SECONDS, MAX_CAPTCHA_STORE_SIZE);
        AbstractManageableCaptchaServiceTest.CAPTCHA_STORE_LOAD_BEFORE_GARBAGE_COLLECTION = 0;
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        getMService().emptyCaptchaStore();
        CacheManager.getInstance().removeCache(EhcacheManageableCaptchaService.CACHE_NAME_PREFIX + EhcacheManageableCaptchaService.DEFAULT_CACHE_NAME);
    }

    /*
    OVERRIDE TO SKIP or replace
    */

    public void testSetCaptchaStoreMaxSize() throws Exception {
        super.getMService().setCaptchaStoreMaxSize(CAPTCHA_STORE_LOAD_BEFORE_GARBAGE_COLLECTION);
        assertEquals("modified size",
                CAPTCHA_STORE_LOAD_BEFORE_GARBAGE_COLLECTION, getMService().getCaptchaStoreMaxSize());
    }

    public void testSetCaptchaStoreSizeBeforeGarbageCollection() throws Exception {

    }

    public void testGetNumberOfGarbageCollectedCaptcha() throws Exception {

    }

    public void testGetCaptchaStoreSizeBeforeGarbageCollection() throws Exception {

    }

    public void testAutomaticGarbaging() throws Exception {

    }

    public void testGetNumberOfGarbageCollectableCaptchas() throws Exception {

    }


}