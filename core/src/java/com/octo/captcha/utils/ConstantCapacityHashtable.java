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
package com.octo.captcha.utils;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A Hashtable that has a constant capacity. It has a load factor of 1
 * and is garbage collected when full. If one try to put a new entry
 * in the Hashtable whereas it is full and no current entry can be
 * garbage collected, a ConstantCapacityHashtableFullException is raised.
 * <p>
 * The garbage collection mecanism is based on a time to live delay
 * for each of the entries, which is specified in the constructor.
 * At garbage collection time, all entries which time to live is over
 * are removed from the Hashtable
 * 
 * @version $Id$
 * 
 * @author <a href="mailto:sbr@octo.com">Sebastien Brunot</a>
 */
public class ConstantCapacityHashtable extends Hashtable
{
    ////////////////////////////////
    // Private attributes
    ////////////////////////////////

    /**
     * Time to live, in milliseconds
     */
    private int timeToLive = 0;

    /**
     * An internal Hashtable that stores a time to live timestamp associated
     * witheach entry (using the same key). The timestampt is the time in
     * millisecond at which the entry was putten + the time to live
     */
    private Hashtable timestamps = null;

    /**
     * Number of entries garbage collected since the creation of the
     * ConstantCapacityHashtable
     */
    private long numberOfGarbageCollectedEntriesSinceCreation = 0;
    
    ////////////////////////////////
    // Constructor
    ////////////////////////////////

    /**
     * Create a new ConstantSizeHashtable
     * @param theCapacity number of entries for this hashtable
     * @param theTimeToLive time to live for each entry, in millisecond
     */
    public ConstantCapacityHashtable(int theCapacity, int theTimeToLive)
    {
        // Create a Hashtable with an initial capacity of capacity
        // and a load factor of 1
        super(theCapacity, 1);
        this.timeToLive = theTimeToLive;
        this.timestamps = new Hashtable(theCapacity, 1);
    }

    ////////////////////////////////
    // Protected methods
    ////////////////////////////////

    /**
     * This is a method to be used by unit tests.
     * @return true if internal hashtables have correct size
     *         false if internal hashtables have incorrect size
     */
    protected boolean checkHashtablesSize()
    {
        // return true if this and timestamps have the same keys
        return (this.keySet().equals(this.timestamps.keySet()));
    }

    ////////////////////////////////
    // Hashtable redefined methods
    ////////////////////////////////

    /**
     * Garbage collect the HashTable to get some free space. If no entries can
     * be deleted by the garbage collector, a
     * ConstantCapacityHashtableFullException is throws.
     */
    protected void rehash()
    {
        // rehash is executed when the Hashtable is full. Instead of
        // increasing its size and rehashing, we garbage collect...
        int nbOfDeltedEntries = this.garbageCollect();
        // if no entry was deleted, it means that the hashtable is full...
        if (nbOfDeltedEntries == 0)
        {
            throw new ConstantCapacityHashtableFullException();
        }
    }

    /**
     * @see java.util.Dictionary#put(java.lang.Object, java.lang.Object)
     */
    public synchronized Object put(Object theKey, Object theValue)
    {
        Object returned = null;
        try
        {
            returned = super.put(theKey, theValue);
        }
        catch (ConstantCapacityHashtableFullException e)
        {
            // the hashtable is full and can't be garbage collected...
            throw e;
        }

        // create the corresponding entry in timestamps
        timestamps.put(theKey,
                       new Long(new Date().getTime() + this.timeToLive));
        return returned;
    }

    /**
     * @see java.util.Dictionary#remove(java.lang.Object)
     */
    public synchronized Object remove(Object theKey)
    {
        // remove the entry and the associate timestamp
        timestamps.remove(theKey);
        return super.remove(theKey);
    }

    ////////////////////////////////
    // Public management methods
    ////////////////////////////////

    /**
     * Get current time to live value
     * @return current time to live in milliseconds
     */
    public int getTimeToLive()
    {
        return this.timeToLive;
    }

    /**
     * Set time to live value
     * @param theTimeToLive the new time to live in milliseconds
     */
    public void setTimeToLive(int theTimeToLive)
    {
        this.timeToLive = theTimeToLive;
        // @TODO : modify time to live for current entries ?
        // Synchronization problem ?
    }
    
    /**
     * Get the number of entries garbage collected since the creation
     * of the ConstantCapacityHashtable
     * @return the number of entries garbage collected since the
     * creation of the ConstantCapacityHashtable
     */
    public long getNumberOfGarbageCollectedEntriesSinceCreation()
    {
        return this.numberOfGarbageCollectedEntriesSinceCreation;
    }

    /**
     * Get the number of entries that can be garbage collected
     * @return number of entries that can be garbage collected
     */
    public int getNumberOfGarbageCollectableEntries()
    {
        int returnedValue = 0;
        long currentTime = new Date().getTime();
        Enumeration keys = this.keys();
        while (keys.hasMoreElements())
        {
            Object key = keys.nextElement();
            long keyTimestamp = ((Long) this.timestamps.get(key)).longValue();
            if (keyTimestamp < currentTime)
            {
                // this entry could be garbage collected
                returnedValue++;
            }
        }
        return returnedValue;
    }

    /**
     * Run the Hashtable entries garbage collector.
     * @return the number of garbage collected entries
     */
    public int garbageCollect()
    {
        int nbOfEntriesGarbageCollected = 0;
        long currentTime = new Date().getTime();
        Enumeration keys = this.keys();
        while (keys.hasMoreElements())
        {
            Object key = keys.nextElement();
            long keyTimestamp = ((Long) this.timestamps.get(key)).longValue();
            if (keyTimestamp < currentTime)
            {
                // remove the corresponding entries in this (and the associated
                // timestamp)
                this.remove(key);
                nbOfEntriesGarbageCollected++;
            }
        }
        // update statistics
        this.numberOfGarbageCollectedEntriesSinceCreation += nbOfEntriesGarbageCollected;
        
        return nbOfEntriesGarbageCollected;
    }

}
