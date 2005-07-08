package com.octo.captcha.engine.bufferedengine.buffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.collections.BufferUnderflowException;
import org.apache.commons.collections.buffer.UnboundedFifoBuffer;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.octo.captcha.Captcha;

/**
 * Simple implmentation of a memory captcha buffer with HashedMap from commons collection
 * 
 * @author Benoit Doumas
 */
public class MemoryCaptchaBuffer implements CaptchaBuffer
{

    public static final String BUFFER_CACHE_NAME = "BasicCacheCaptchaBuffer";

    public static final String SCHEDULER_ID = "BasicCacheCaptchaBuffer";

    private static final Log log = LogFactory.getLog(MemoryCaptchaBuffer.class);

    protected HashedMap buffers = new HashedMap();

    

    /**
     * 
     */
    public MemoryCaptchaBuffer()
    {
        log.debug("Initializing Buffer");
    }

    /**
     * @see com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer#removeCaptcha(java.util.Locale)
     */
    public Captcha removeCaptcha(Locale locale) throws BufferUnderflowException
    {
        Captcha captcha = null;

        if (buffers.containsKey(locale))
        {
            try
            {
                captcha = (Captcha) ((UnboundedFifoBuffer) buffers.get(locale)).remove();
                log.debug("get captcha from MemoryBuffer");
            }
            catch (BufferUnderflowException e)
            {
                log.debug("Buffer empty for locale : " + locale.toString());
            }
        }
        else
        {
            if (log.isDebugEnabled())
            {
                log.debug("Locale not present : " + locale.toString());
                throw new BufferUnderflowException();
            }
        }

        return captcha;
    }

    /**
     * @see com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer#removeCaptcha(int,
     *          java.util.Locale)
     */
    public Collection removeCaptcha(int number, Locale locale)
    {
        ArrayList list = new ArrayList(number);

        UnboundedFifoBuffer buffer = (UnboundedFifoBuffer) buffers.get(locale);
        if (buffer == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Locale not found in Memory buffer map : " + locale.toString());
            }
            return list;
        }

        try
        {

            for (int i = 0; i < number; i++)
            {
                list.add(buffer.remove());
            }
        }
        catch (BufferUnderflowException e)
        {
            log.debug("Buffer empty for locale : " + locale.toString());
        }
        if (log.isDebugEnabled())
        {
            log.debug("Removed from locale :'"+locale+"' a list of '" + list.size()+"' elements.");
        }
        return list;
    }

    /**
     * @see com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer#removeCaptcha(int)
     */
    public Collection removeCaptcha(int number)
    {
        return removeCaptcha(number, Locale.getDefault());
    }

    public Captcha removeCaptcha() throws BufferUnderflowException
    {
        return removeCaptcha(Locale.getDefault());
    }

    /**
     * @see com.octo.captcha.service.buffer.CaptchaBuffer#addCaptcha(com.octo.captcha.Captcha)
     */
    public void putCaptcha(Captcha captcha, Locale locale)
    {

        if (!buffers.containsKey(locale))
        {
            buffers.put(locale, new UnboundedFifoBuffer());
        }

        ((UnboundedFifoBuffer) buffers.get(locale)).add(captcha);
    }

    /**
     * @see com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer#putAllCaptcha(java.util.Collection)
     */
    public void putAllCaptcha(Collection captchas, Locale locale)
    {
        if (!buffers.containsKey(locale))
        {
            buffers.put(locale, new UnboundedFifoBuffer());
        }

        ((UnboundedFifoBuffer) buffers.get(locale)).addAll(captchas);

        if (log.isDebugEnabled())
        {
            log.debug("put into mem  : " + captchas.size() + " for locale :" + locale.toString()
                + " with size : " + ((UnboundedFifoBuffer) buffers.get(locale)).size());
        }

    }

    /**
     * @see com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer#size()
     */
    public int size()
    {
        int total = 0;

        Iterator it = buffers.keySet().iterator();
        while (it.hasNext())
        {
            total += ((UnboundedFifoBuffer) buffers.get(it.next())).size();
        }

        return total;
    }

    /**
     * @see com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer#size()
     */
    public int size(Locale locale)
    {
        if (!buffers.containsKey(locale))
        {
            buffers.put(locale, new UnboundedFifoBuffer());
        }
        return ((UnboundedFifoBuffer) buffers.get(locale)).size();
    }

    /**
     * @see com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer#putCaptcha(com.octo.captcha.Captcha)
     */
    public void putCaptcha(Captcha captcha)
    {
        putCaptcha(captcha, Locale.getDefault());
    }

    /**
     * @see com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer#putAllCaptcha(java.util.Collection)
     */
    public void putAllCaptcha(Collection captchas)
    {
        putAllCaptcha(captchas, Locale.getDefault());
    }

    /**
     * @see com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer#dispose()
     */
    public void dispose()
    {
        
    }

    /**
     * @see com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer#clear()
     */
    public void clear()
    {
       buffers.clear();
    }

    /**
     * @see com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer#getLocales()
     */
    public Collection getLocales()
    {
        return buffers.keySet();
    }

}
