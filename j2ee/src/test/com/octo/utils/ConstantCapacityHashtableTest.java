/*
 * Created on 29 nov. 03
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.octo.utils;

import java.util.Hashtable;

import junit.framework.TestCase;

/**
 * 
 */
public class ConstantCapacityHashtableTest extends TestCase
{
    private int CAPACITY = 1000;

    /**
     * Constructor for ConstantCapacityHashtableTest.
     * @param arg0
     */
    public ConstantCapacityHashtableTest(String arg0)
    {
        super(arg0);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    //////////////////////////////
    // Unit tests
    /////////////////////////////
    
    /**
     * one try to put an entry in a full hashtable that can be garcbage collected
     *
     */
    public void testCapacityOverloadWithGCOk()
    {
        // we use a negative time to live to be sure that all entries are garbage collected when hashtable is full
        ConstantCapacityHashtable tested = new ConstantCapacityHashtable(CAPACITY, -10);
        for (int i=0; i < CAPACITY + 1; i++)
        {
            tested.put("test" + i, "FOO");
            assertTrue(tested.checkHashtablesSize());
        }
        assertTrue(tested.checkHashtablesSize());
    }

    /**
     * one try to put an entry in a full hashtable that cannot be garcbage collected
     *
     */
    public void testCapacityOverloadWithGCKo()
    {
        ConstantCapacityHashtable tested = new ConstantCapacityHashtable(CAPACITY,1000000);
        for (int i=0; i < CAPACITY; i++)
        {
           tested.put("test" + i, "FOO");
           assertTrue(tested.checkHashtablesSize());
        }
        try
        {
            tested.put("test", "FOO");
        }
        catch (ConstantCapacityHashtableFullException e)
        {
            assertTrue(tested.checkHashtablesSize());
            return;
        }
        assertTrue(false);
    }

    /**
     * use the putAll Method
     *
     */
    public void testPutAll()
    {
        ConstantCapacityHashtable tested = new ConstantCapacityHashtable(CAPACITY, 10000);
        Hashtable toPutInto = new Hashtable(CAPACITY);
        for (int i=0; i<CAPACITY; i++)
        {
            toPutInto.put("key"+i,"FOO");
        }
        tested.putAll(toPutInto);
        assertTrue(tested.checkHashtablesSize());
    }
    
    /**
     * Test the checkHashtableSize() methods with empty keys
     */
    public void testCheckHastableSizeWhenNoKeys()
    {
        ConstantCapacityHashtable tested = new ConstantCapacityHashtable(CAPACITY, 10000);
        assertTrue(tested.checkHashtablesSize());
    }
}
