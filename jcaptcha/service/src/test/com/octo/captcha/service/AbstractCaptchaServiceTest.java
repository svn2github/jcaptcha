/*
 * JCaptcha, the open source java framework for captcha definition and integration
 * Copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.service;

import com.octo.captcha.service.captchastore.MapCaptchaStore;
import junit.framework.TestCase;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

public class AbstractCaptchaServiceTest extends TestCase {

    protected Random myRandom = new SecureRandom();

    protected AbstractCaptchaService service = new MockedCaptchaService(new MapCaptchaStore(),
            new MockCaptchaEngine());

    public static final int SIZE = 1000;

    public void testAbstractCaptchaService() throws Exception {
        try {
            new MockedCaptchaService(null, new MockCaptchaEngine());
            fail("should have thrown an exception");
        } catch (Exception e) {
            assertTrue("IllegalArgumentException attended", e instanceof IllegalArgumentException);
        }
        try {
            new MockedCaptchaService(new MapCaptchaStore(), null);
            fail("should have thrown an exception");
        } catch (Exception e) {
            assertTrue("IllegalArgumentException attended", e instanceof IllegalArgumentException);
        }
    }

    public void testGetChallengeForID() throws Exception {

        for (int i = 0; i < SIZE; i++) {
            String id = String.valueOf(myRandom.nextInt());
            assertEquals("Should always return a cloned challenge",
                    MockCaptcha.CHALLENGE + MockedCaptchaService.CLONE_CHALLENGE, service.getChallengeForID(id));
            assertEquals("Should always return a cloned challenge",
                    MockCaptcha.CHALLENGE + MockedCaptchaService.CLONE_CHALLENGE, service.getChallengeForID(id));
        }

    }

    public void testGetQuestionForID() throws Exception {
        for (int i = 0; i < SIZE; i++) {
            String id = String.valueOf(myRandom.nextInt());
            assertEquals("Should always return The mock question and default locale",
                    MockCaptcha.QUESTION_BASE + Locale.getDefault(), service.getQuestionForID(id));
            assertEquals("Should always return The mock question and specified locale",
                    MockCaptcha.QUESTION_BASE + Locale.CHINESE, service.getQuestionForID(id, Locale.CHINESE));
        }

    }

    public void testValidateResponseForID() throws Exception {

        for (int i = 0; i < SIZE; i++) {
            String id = String.valueOf(myRandom.nextInt());
            try {
                service.validateResponseForID(id, "true");
                fail("The tiket is invalid, should throw an exception");
            } catch (CaptchaServiceException e) {
                //ok
            }
            //Should be ok after new question
            service.getQuestionForID(id);
            assertTrue("Sould be ok", service.validateResponseForID(id, "true").booleanValue());

            //Should be ok after new challenge
            service.getChallengeForID(id);
            assertTrue("Sould be ok", service.validateResponseForID(id, "true").booleanValue());

            //Should be ok after new challenge and question
            service.getChallengeForID(id);
            service.getQuestionForID(id);
            assertTrue("Sould be ok", service.validateResponseForID(id, "true").booleanValue());

            //Should be ok after new question and challenge
            service.getChallengeForID(id);
            service.getQuestionForID(id);
            assertTrue("Sould be ok", service.validateResponseForID(id, "true").booleanValue());
        }
    }

    public void testGenerateAndStoreCaptcha() throws Exception {
        for (int i = 0; i < SIZE; i++) {
            String id = String.valueOf(myRandom.nextInt());
            service.generateAndStoreCaptcha(Locale.getDefault(), id);
            assertTrue("Sould be ok", service.validateResponseForID(id, "true").booleanValue());

        }
    }


    public void testCaptchaRegenerationWhenNewLocaleIsAsked() throws Exception {
        String french = service.getQuestionForID("1", Locale.FRENCH);
        String english = service.getQuestionForID("1", Locale.ENGLISH);
        assertFalse(french.equals(english));

    }


}
