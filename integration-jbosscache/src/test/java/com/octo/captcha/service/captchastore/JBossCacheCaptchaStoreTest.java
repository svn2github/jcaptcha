package com.octo.captcha.service.captchastore;

import org.jboss.cache.TreeCache;

/**
 * @author mag
 * @Date 15 déc. 2007
 */
public class JBossCacheCaptchaStoreTest extends CaptchaStoreTestAbstract {

    public CaptchaStore initStore() {
        TreeCache tree = null;
        try {
            tree = new TreeCache();
            tree.setClusterName("test-cluster");
            tree.setCacheMode(TreeCache.LOCAL);
            tree.createService(); // not necessary, but is same as MBean lifecycle
            tree.startService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new JBossCacheCaptchaStore(tree);
    }
}
