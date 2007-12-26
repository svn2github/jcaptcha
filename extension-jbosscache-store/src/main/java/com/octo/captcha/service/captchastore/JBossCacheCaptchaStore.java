/*
 * JCaptcha, the open source java framework for captcha definition and integration
 * Copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.service.captchastore;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import org.jboss.cache.CacheException;
import org.jboss.cache.PropertyConfigurator;
import org.jboss.cache.TreeCache;

import com.octo.captcha.Captcha;
import com.octo.captcha.service.CaptchaServiceException;

/**
 * JBossCache implementation of the captcha store. Needs JDK 5.0 with version 1.4.x 
 * @see http://wiki.jboss.org/wiki/Wiki.jsp?page=JBossCache
 * @author <a href="mailto:antoine.veret@gmail.com">Antoine Véret</a>
 * @version 1.0
 */
public class JBossCacheCaptchaStore implements CaptchaStore {

	public static final String JCAPTCHA_JBOSSCACHE_CONFIG = "jcaptcha.jbosscache.config";
    private static final String DEFAULT_CACHE_NAME = "/captcha";
    private String cacheQualifiedName;
    private TreeCache treeCache;

    public JBossCacheCaptchaStore() {
        this(DEFAULT_CACHE_NAME);
    }
    
    public JBossCacheCaptchaStore(String cacheQualifiedName) {
        this.cacheQualifiedName = cacheQualifiedName;
    }

    public boolean hasCaptcha(String s) {

        return treeCache.exists(cacheQualifiedName, s);
    }

    public void storeCaptcha(String s, Captcha captcha) throws CaptchaServiceException {

        try {
            treeCache.put(cacheQualifiedName, s, new CaptchaAndLocale(captcha));
        } catch (CacheException e) {
            throw new CaptchaServiceException(e);
        }
    }

    public void storeCaptcha(String s, Captcha captcha, Locale locale) throws CaptchaServiceException {

        try {
            treeCache.put(cacheQualifiedName, s, new CaptchaAndLocale(captcha, locale));
        } catch (CacheException e) {
            throw new CaptchaServiceException(e);
        }
    }

    public boolean removeCaptcha(String s) {
        try {
            Object captcha = treeCache.remove(cacheQualifiedName, s);
            if (captcha != null)
                return true;
            else
                return false;
        } catch (CacheException e) {
            throw new RuntimeException(e);
        }
    }

    public Captcha getCaptcha(String s) throws CaptchaServiceException {

        try {
            Object result = treeCache.get(cacheQualifiedName, s);
            if (result != null) {
                CaptchaAndLocale captchaAndLocale = (CaptchaAndLocale) result;
                return captchaAndLocale.getCaptcha();
            }
            else
                return null;

        } catch (CacheException e) {
            throw new CaptchaServiceException(e);
        }
    }

    public Locale getLocale(String s) throws CaptchaServiceException {

        try {
            Object result = treeCache.get(cacheQualifiedName, s);
            if (result != null) {
                CaptchaAndLocale captchaAndLocale = (CaptchaAndLocale) result;
                return captchaAndLocale.getLocale();
            }
            else
                return null;

        } catch (CacheException e) {
            throw new CaptchaServiceException(e);
        }
    }

    public int getSize() {

        try {
            Collection keys = treeCache.getKeys(cacheQualifiedName);
            if (keys != null)
                return keys.size();
            else
                return 0;
        } catch (CacheException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection getKeys() {
        try {
        	Collection keys = treeCache.getKeys(cacheQualifiedName); 
            if (keys != null)
            	return keys;
            else
            	return Collections.EMPTY_SET;
        } catch (CacheException e) {
            throw new RuntimeException(e);
        }
    }

    public void empty() {
        try {
            treeCache.removeData(cacheQualifiedName);
            treeCache.remove(cacheQualifiedName);
        } catch (CacheException e) {
            throw new RuntimeException(e);
        }
    }
    
    /* (non-Javadoc)
	 * @see com.octo.captcha.service.captchastore.CaptchaStore#initAndStart()
	 */
	public void initAndStart() {
		
		String configFileName = System.getProperty(JCAPTCHA_JBOSSCACHE_CONFIG);
        if (configFileName == null)
            throw new RuntimeException("The system property " + JCAPTCHA_JBOSSCACHE_CONFIG + " have to be set");
		
        try {
			treeCache = new TreeCache();
			
			PropertyConfigurator config = new PropertyConfigurator();
			config.configure(treeCache, configFileName);
					
			treeCache.startService();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.octo.captcha.service.captchastore.CaptchaStore#shutdownAndClean()
	 */
	public void cleanAndShutdown() {
		treeCache.stopService();
		treeCache.destroyService();
	}
}
