/*
 * Created on 13 mai 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.octo.captcha.engine.bufferedengine.manager;

import java.util.Map;

import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer;

/**
 * Interface that adds manageability options to the BufferedEngineContainer
 * 
 * @author Benoit Doumas
 */
public interface BufferedEngineContainerManager
{

    /**
     * Get number of captchas to feed the disk buffer
     */
    public Integer getFeedSize();

    /**
     * @param size
     *                  Set number of captchas to swap between the volatil buffer and the disk buffer
     */
    public void setSwapSize(Integer size);

    /**
     * Get number of captchas to swap between the volatil buffer and the disk buffer
     */
    public Integer getSwapSize();

    /**
     * @param size
     *                  Set maximun size for the volatile buffer
     */
    public void setMaxVolatileMemorySize(Integer size);

    /**
     * Get maximun size for the volatile buffer
     */
    public Integer getMaxVolatileMemorySize();

    /**
     * @param size
     *                  Set maximum size for the disk buffer
     */
    public void setMaxPersistentMemorySize(Integer size);

    /**
     * Set maximum size for the disk buffer
     */
    public Integer getMaxPersistentMemorySize();

    /**
     * @param localeName
     *                  Name of th locale to set or to create
     * @param ratio
     *                  The ratio of the locale
     */
    public void setLocaleRatio(String localeName, double ratio);

    /**
     * @return the number of volatile memory access
     */
    public Integer getVolatileMemoryHits();

    /**
     * @return the number of persistent accesses
     */
    public Integer getPersistentMemoryHits();

    /**
     * @return the number of disk feedings
     */
    public Integer getPersistentFeedings();

    /**
     * @return the number of swap from disk to memory
     */
    public Integer getPersistentToVolatileSwaps();

    /**
     * Tell the scheduler to start to feed the persistent buffer
     */
    public void startToFeedPersistantBuffer();

    /**
     * Tell the scheduler to stop to feed the persistent buffer
     */
    public void stopToFeedPersistentBuffer();

    /**
     * Tell the scheduler to start to swap captchas from persistent buffer to memory buffer
     */
    public void startToSwapFromPersistentToVolatileMemory();

    /**
     * Tell the scheduler to stop to swap captchas from persistent buffer to memory buffer
     */
    public void stopToSwapFromPersistentToVolatileMemory();

    /**
     * Pause the scheduler, both the swapping and the feeding process are paused
     */
    public void pause();

    /**
     * Resume the scheduler, both the swapping and the feeding process are resumed
     */
    public abstract void resume();

    /**
     * Shutdown scheduling, the container will use its memory buffer until its empty and swtich to
     * the engine.
     */
    public void shutdown();
}
