package com.octo.captcha.engine.bufferedengine.buffer;

import com.octo.captcha.Captcha;




import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Random;

public class DiskCaptchaBufferTest extends TestCase
{

    protected Random myRandom = new Random();

    protected DiskCaptchaBuffer diskStore = null;
    
    protected String name = System.getProperty("java.io.tempdir");

    public static final int SIZE = 1000;

    public void testStoreObjects() throws Exception
    {

        this.diskStore = new DiskCaptchaBuffer(name, false);
        ArrayList  listToStore = new ArrayList(100);

        for (int i = 0; i < 100; ++i)
        {
            listToStore.add(new String("test"));
        }
        this.diskStore.putAllCaptcha(listToStore);
        

            Collection c1 = this.diskStore.removeCaptcha(100);
            assertNotNull(c1);
            assertEquals(100,c1.size());
            
            Collection c2 = this.diskStore.removeCaptcha(1);
            assertNotNull(c2);
            
            assertEquals(0,c2.size());
    }
    
   
    
    public void testEmptyStore() throws Exception
    {

        this.diskStore = new DiskCaptchaBuffer(name, false);
        ArrayList  listToStore = new ArrayList(1);

        for (int i = 0; i < 1; ++i)
        {
            listToStore.add(new String("test"));
        }
        this.diskStore.putAllCaptcha(listToStore);
        
       Collection c1 = this.diskStore.removeCaptcha(5);
       assertEquals(1,c1.size());
    }
    
    public void testLocaleStoreObjects() throws Exception
    {

        this.diskStore = new DiskCaptchaBuffer(name, false);
        ArrayList  listToStore = new ArrayList(100);

        for (int i = 0; i < 100; ++i)
        {
            listToStore.add(new String("test"));
        }
        this.diskStore.putAllCaptcha(listToStore, Locale.FRANCE);
        

            Collection c1 = this.diskStore.removeCaptcha(100, Locale.FRANCE);
            assertNotNull(c1);
            assertEquals(100,c1.size());
            
            //get default, but empty
            Collection c2 = this.diskStore.removeCaptcha(1);
            assertNotNull(c2);
            
            assertEquals(0,c2.size());
    }
    
    
    public void testLocaleMix() throws Exception
    {

        this.diskStore = new DiskCaptchaBuffer(name, false);
        ArrayList  listToStore = new ArrayList(1000);

        for (int i = 0; i < 10; ++i)
        {
            listToStore.add(new String("test"));
        }
       this.diskStore.putAllCaptcha(listToStore, Locale.FRANCE);
       
       this.diskStore.putAllCaptcha(listToStore, Locale.US);
       
       this.diskStore.putAllCaptcha(listToStore, Locale.GERMAN);
        
       Collection c1 = this.diskStore.removeCaptcha(5, Locale.FRANCE);
       assertNotNull(c1);
       assertEquals(5,c1.size());

       Collection c2 = this.diskStore.removeCaptcha(10, Locale.US);
       assertNotNull(c2);
       assertEquals(10,c2.size());
       
       Collection c3 = this.diskStore.removeCaptcha(11, Locale.GERMAN);
       assertNotNull(c3);
       assertEquals(10,c3.size());
    }
    
    public void testLocaleEmptyStore() throws Exception
    {

        this.diskStore = new DiskCaptchaBuffer(name, false);
        ArrayList  listToStore = new ArrayList(1);

        for (int i = 0; i < 1; ++i)
        {
            listToStore.add(new String("test"));
        }
       this.diskStore.putAllCaptcha(listToStore);
        
       Collection c1 = this.diskStore.removeCaptcha(5);
       assertEquals(1,c1.size());
    }
    
    
    public void testPesistant() throws Exception
    {

        this.diskStore = new DiskCaptchaBuffer(name, false);
        ArrayList  listToStore = new ArrayList(1);

        for (int i = 0; i < 10000; ++i)
        {
            listToStore.add(new String("test"));
        }
       this.diskStore.putAllCaptcha(listToStore);
       
       Collection c1 =  this.diskStore.removeCaptcha(2);
       assertEquals(2,c1.size());
       this.diskStore.dispose();
       
       DiskCaptchaBuffer diskStore2 = new DiskCaptchaBuffer(name, true);
        
       Collection c2 = diskStore2.removeCaptcha(5);
       assertEquals(5,c2.size());
    }

}
