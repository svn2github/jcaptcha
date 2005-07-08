package com.octo.captcha.engine.bufferedengine.buffer;

import com.octo.captcha.Captcha;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Random;

public class MemoryCaptchaBufferTest extends TestCase
{

    protected Random myRandom = new Random();

    protected MemoryCaptchaBuffer memBuffer = null;

    protected String name = "C:/temp/monStore";

    public static final int SIZE = 1000;

    public void testStoreObjects() throws Exception
    {

        this.memBuffer = new MemoryCaptchaBuffer();
        ArrayList listToStore = new ArrayList(100);

        for (int i = 0; i < 100; ++i)
        {
            listToStore.add(new String("test"));
        }
        this.memBuffer.putAllCaptcha(listToStore);

        Collection c1 = this.memBuffer.removeCaptcha(5);
        assertNotNull(c1);
        assertEquals( 5, c1.size());

        Collection c2 = this.memBuffer.removeCaptcha(1);
        assertNotNull(c2);

        assertEquals( 1,c2.size());
    }

    public void testEmptyStore() throws Exception
    {

        this.memBuffer = new MemoryCaptchaBuffer();
        ArrayList listToStore = new ArrayList(1);

        for (int i = 0; i < 1; ++i)
        {
            listToStore.add(new String("test"));
        }
        this.memBuffer.putAllCaptcha(listToStore);

        Collection c1 = this.memBuffer.removeCaptcha(5);
        assertEquals(c1.size(), 1);
    }

    public void testLocaleStoreObjects() throws Exception
    {

        this.memBuffer = new MemoryCaptchaBuffer();
        ArrayList listToStore = new ArrayList(100);

        for (int i = 0; i < 100; ++i)
        {
            listToStore.add(new String("test"));
        }
        this.memBuffer.putAllCaptcha(listToStore, Locale.FRANCE);

        Collection c1 = this.memBuffer.removeCaptcha(100, Locale.FRANCE);
        assertNotNull(c1);
        assertEquals(c1.size(), 100);

        //get default, but empty
        Collection c2 = this.memBuffer.removeCaptcha(1);
        assertNotNull(c2);

        assertEquals(c2.size(),0);
    }

    public void testLocaleMix() throws Exception
    {

        this.memBuffer = new MemoryCaptchaBuffer();
        ArrayList listToStore = new ArrayList(1000);

        for (int i = 0; i < 10; ++i)
        {
            listToStore.add(new String("test"));
        }
        this.memBuffer.putAllCaptcha(listToStore, Locale.FRANCE);

        this.memBuffer.putAllCaptcha(listToStore, Locale.US);

        this.memBuffer.putAllCaptcha(listToStore, Locale.GERMAN);

        Collection c1 = this.memBuffer.removeCaptcha(5, Locale.FRANCE);
        assertNotNull(c1);
        assertEquals( 5,c1.size());

        Collection c2 = this.memBuffer.removeCaptcha(10, Locale.US);
        assertNotNull(c2);
        assertEquals(10,c2.size());

        Collection c3 = this.memBuffer.removeCaptcha(11, Locale.GERMAN);
        assertNotNull(c3);
        assertEquals(10,c3.size());
    }

    public void testLocaleEmptyStore() throws Exception
    {

        this.memBuffer = new MemoryCaptchaBuffer();
        ArrayList listToStore = new ArrayList(1);

        for (int i = 0; i < 1; ++i)
        {
            listToStore.add(new String("test"));
        }
        this.memBuffer.putAllCaptcha(listToStore);

        Collection c1 = this.memBuffer.removeCaptcha(5);
        assertEquals(1,c1.size());
    }

}
