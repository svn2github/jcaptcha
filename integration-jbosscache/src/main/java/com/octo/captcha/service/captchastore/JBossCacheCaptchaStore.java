package com.octo.captcha.service.captchastore;

import com.octo.captcha.Captcha;
import com.octo.captcha.service.CaptchaServiceException;

import java.util.Locale;
import java.util.Collection;
import java.util.Set;
import java.util.Collections;

import org.jboss.cache.TreeCache;
import org.jboss.cache.Node;
import org.jboss.cache.CacheException;

/**
 * @author mag
 * JBossCache implementation of the captcha store
 * @Date 15 déc. 2007
 */
public class JBossCacheCaptchaStore implements CaptchaStore{

    private static String FQN_STORE = "jcaptcha";

    private TreeCache cache;

    public JBossCacheCaptchaStore(TreeCache cache) {
        this.cache= cache;
        if(cache==null)throw new IllegalArgumentException("cache cannot be null, " +
                "please initialize this captcha store with a valid JbossCache instance");

    }

    public boolean hasCaptcha(String id) {
        try {
            final CaptchaAndLocale captchaAndLocale = (CaptchaAndLocale)this.cache.get(FQN_STORE,id);
            return captchaAndLocale!=null;
        }catch(CacheException e){
            return false;
        }
    }

    public void storeCaptcha(String id, Captcha captcha) throws CaptchaServiceException {
         try {
            this.cache.put(FQN_STORE,id,new CaptchaAndLocale(captcha));
        }catch(CacheException e){
           throw new CaptchaServiceException("error during jbosscache operation",e);
        }
    }

    public void storeCaptcha(String id, Captcha captcha, Locale locale) throws CaptchaServiceException {
        try {
            this.cache.put(FQN_STORE,id,new CaptchaAndLocale(captcha,locale));
        }catch(CacheException e){
           throw new CaptchaServiceException("error during jbosscache operation",e);
        }
    }

    public boolean removeCaptcha(String id) {
         try {
            Object cached = this.cache.remove(FQN_STORE,id);
             return cached!=null;
        }catch(CacheException e){
           throw new CaptchaServiceException("error during jbosscache operation",e);
        }
    }

    public Captcha getCaptcha(String id) throws CaptchaServiceException {
        try {
            final CaptchaAndLocale captchaAndLocale = (CaptchaAndLocale) this.cache.get(FQN_STORE,id);
            if(captchaAndLocale==null)return null;
            return captchaAndLocale.getCaptcha();
        }catch(CacheException e){
           throw new CaptchaServiceException("error during jbosscache operation",e);
        }
    }

    public Locale getLocale(String id) throws CaptchaServiceException {
       try {
            final CaptchaAndLocale captchaAndLocale = (CaptchaAndLocale)this.cache.get(FQN_STORE,id);
            return captchaAndLocale.getLocale();
        }catch(CacheException e){
           throw new CaptchaServiceException("error during jbosscache operation",e);
        }
    }

    public int getSize() {
        return getKeys().size();
    }

    public Collection getKeys() {
        try {
            final Set keys = this.cache.getKeys(FQN_STORE);
            return keys!=null?keys: Collections.EMPTY_SET;
        }catch(CacheException e){
           throw new CaptchaServiceException("error during jbosscache operation",e);
        }
    }

    public void empty() {
           try {
            this.cache.remove(FQN_STORE);
        }catch(CacheException e){
            throw new CaptchaServiceException("error during jbosscache operation",e);
        }
    }
}
