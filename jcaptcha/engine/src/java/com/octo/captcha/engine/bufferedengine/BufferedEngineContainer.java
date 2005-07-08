/*
 * Created on 13 mai 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.octo.captcha.engine.bufferedengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.BufferUnderflowException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.octo.captcha.Captcha;
import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer;

/**
 * Abstact class that encapsulate a CaptchaEngine to allow buffering. A BufferedEngineContainer has
 * mainly one function : to provide cached captchas to increase performances. This is done through
 * two embedded buffers : a disk buffer and a memory buffer. When captchas are requested, the
 * bufferedEngine take them either from the memory buffer if not empty or directly from the engine.
 * Some good periods are defined with a scheduler to feed the disk buffer with captchas and some
 * others to swap captchas from the disk buffer to the memory buffer.
 * 
 * @author Benoit Doumas
 */
public abstract class BufferedEngineContainer implements CaptchaEngine
{

    private static final Log log = LogFactory.getLog(BufferedEngineContainer.class.getName());

    protected CaptchaBuffer persistentBuffer = null;

    protected CaptchaBuffer volatileBuffer = null;

    protected CaptchaEngine engine = null;

    protected ContainerConfiguration config = null;

    protected int volatileMemoryHits = 0;

    protected int persistentMemoryHits = 0;

    protected int persistentToVolatileSwaps = 0;

    protected int persistentFeedings = 0;

    /**
     * Construct an BufferedEngineContainer with and Captcha engine, a memory buffer, a diskBuffer
     * and a ContainerConfiguration.
     * 
     * @param engine
     *                  engine to generate captcha for buffers
     * @param volatileBuffer
     *                  the memory buffer, which store captcha and provide a fast access to them
     * @param persistentBuffer
     *                  the disk buffer which store captchas not in a volatil and memory consuming way
     * @param containerConfiguration
     */
    public BufferedEngineContainer(CaptchaEngine engine, CaptchaBuffer volatileBuffer,
        CaptchaBuffer persistentBuffer, ContainerConfiguration containerConfiguration)
    {
        this.engine = engine;
        this.volatileBuffer = volatileBuffer;
        this.persistentBuffer = persistentBuffer;
        this.config = containerConfiguration;

        //define hook when JVM is shutdown
        Shutdown sh = new Shutdown();
        Runtime.getRuntime().addShutdownHook(sh);
    }

    /**
     * @see com.octo.captcha.engine.CaptchaEngine#getNextCaptcha()
     */
    public Captcha getNextCaptcha()
    {
        log.debug("entering getNextCaptcha()");
        return getNextCaptcha(Locale.getDefault());
    }

    /**
     * @see com.octo.captcha.engine.CaptchaEngine#getNextCaptcha(java.util.Locale)
     */
    public Captcha getNextCaptcha(Locale locale)
    {
        log.debug("entering getNextCaptcha(Locale locale)");
        Captcha captcha = null;

        try {
            captcha = volatileBuffer.removeCaptcha(locale);
        } catch (BufferUnderflowException e) {
            log.debug("no captcha under this locale", e);  
        }


        if (captcha == null)
        {
            //get from engine directly
            captcha = engine.getNextCaptcha();
            log.info("get captcha from engine");
        }
        else
        {
            log.info("get captcha from mem buffer");
            //stats
            volatileMemoryHits++;
        }
        return captcha;
    }

    /**
     * Method launch by a scheduler to swap captcha from disk buffer to the memory buffer. The ratio
     * of swaping for each locale is defined in the configuration component.
     */
    public void swapCaptchasFromPersistentToVolatileMemory()
    {
        //TODO refactor this
        log.debug("entering swapCaptchasFromDiskBufferToMemoryBuffer()");

        MapIterator it = config.getLocaleRatio().mapIterator();
        Locale locale = null;
        double ratio = 0;
        int ratioCount = 0;

        while (it.hasNext())
        {
            locale = (Locale) it.next();
            ratio = ((Double) it.getValue()).doubleValue();
            ratioCount = (int) (config.getSwapSize().intValue() * ratio);

            //get the reminding size corresponding to the ratio
            int diff = (int) ((config.getMaxVolatileMemorySize().intValue() - this.volatileBuffer
                .size()) * ratio);

            if (diff <= 0)
                diff = 0;

            Collection temp = this.persistentBuffer.removeCaptcha((diff < ratioCount) ? diff
                : ratioCount, locale);

            if (log.isDebugEnabled())
            {
                log.debug("Diff " + diff);
                log.debug("Swap  " + temp.size() + " Captchas to volatile memory with locale : "
                    + locale.toString());
            }

            this.volatileBuffer.putAllCaptcha(temp, locale);
            //stats
            persistentMemoryHits += temp.size();
        }

        if (log.isDebugEnabled())
        {
            log.debug("Volatil size  " + this.volatileBuffer.size());
        }

        log.info("Volatil Buffer size : " + volatileBuffer.size());

        // stats
        persistentToVolatileSwaps++;
    }

    public Class getEngineClass()
    {
        return engine.getClass();
    }

    /**
     * Method launch by a scheduler to feed the disk buffer with captcha. The ratio of feeding for
     * each locale is defined in the configuration component.
     */
    public void feedPersistentBuffer()
    {
        //TODO refactor this
        log.debug("entering feedDiskBuffer()");
        ArrayList captchas = new ArrayList(config.getFeedSize().intValue());

        int i = 0;

        MapIterator it = config.getLocaleRatio().mapIterator();

        Locale locale = (Locale) it.next();
        double ratio = ((Double) it.getValue()).doubleValue();
        int ratioCount = (int) (config.getFeedSize().intValue() * ratio);

        if (log.isDebugEnabled())
        {
            log.debug("feed locale :   " + locale.toString() + " ratio count : " + ratioCount);
        }

        boolean hasLocale = it.hasNext();

        while (i < config.getFeedSize().intValue() && hasLocale
            && persistentBuffer.size() <= config.getMaxPersistentMemorySize().intValue())
        {
            persistentBuffer.putCaptcha(engine.getNextCaptcha(locale), locale);
            i++;
            ratioCount--;
            //if buffer has been fill filled enough for locale ratio or buffer is full
            if (ratioCount <= 0
                || persistentBuffer.size(locale) >= config.getMaxPersistentMemorySize().intValue()
                    * ratio)
            {
                //time to fill another locale
                hasLocale = it.hasNext();
                if (hasLocale)
                {
                    locale = (Locale) it.next();
                    ratio = ((Double) it.getValue()).doubleValue();
                    ratioCount = (int) (config.getFeedSize().intValue() * ratio);

                    if (log.isDebugEnabled())
                    {
                        log.debug("feed locale :   " + locale.toString() + " ratio count : "
                            + ratioCount);
                    }
                }
            }

        }
        if (log.isDebugEnabled())
        {
            log.debug("feed disk with  " + i + " Captchas");
        }

        log.info("persitantBuffer size : " + persistentBuffer.size());
        persistentFeedings++;
    }

    public ContainerConfiguration getConfig()
    {
        return config;
    }

    public CaptchaBuffer getPersistentBuffer()
    {
        return persistentBuffer;
    }

    public Integer getPersistentFeedings()
    {
        return new Integer(persistentFeedings);
    }

    public Integer getPersistentMemoryHits()
    {
        return new Integer(persistentMemoryHits);
    }

    public Integer getPersistentToVolatileSwaps()
    {
        return new Integer(persistentToVolatileSwaps);
    }

    public CaptchaBuffer getVolatileBuffer()
    {
        return volatileBuffer;
    }

    public Integer getVolatileMemoryHits()
    {
        return new Integer(volatileMemoryHits);
    }

    class Shutdown extends Thread
    {
        public Shutdown()
        {
            super();
        }

        public void run()
        {
            System.out.println("MyShutDown thread started");
            try
            {
                closeBuffers();
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }
        }
    }

    public void closeBuffers()
    {
        this.persistentBuffer.dispose();
        this.volatileBuffer.dispose();
        log.info("Buffers disposed");
    }
}
