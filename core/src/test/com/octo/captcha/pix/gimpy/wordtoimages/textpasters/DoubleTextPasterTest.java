package com.octo.captcha.pix.gimpy.wordtoimages.textpasters;

import java.awt.image.BufferedImage;
import java.text.AttributedString;

import com.octo.captcha.CaptchaException;

import junit.framework.TestCase;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class DoubleTextPasterTest extends TestCase {

    private DoubleTextPaster doubleTextPaster;
    private Integer minAcceptedWordLength = new Integer(10);    
    private Integer maxAcceptedWordLength = new Integer(15);

    /**
     * Constructor for DoubleTextPasterTest.
     * @param name
     */
    public DoubleTextPasterTest(String name) {
        super(name);
    }
    
    public void setUp() {
        this.doubleTextPaster = new DoubleTextPaster(this.maxAcceptedWordLength,
            this.minAcceptedWordLength);
    }

    public void testPasteText() {
        BufferedImage testBufferedImage = new BufferedImage(100,50,BufferedImage.TYPE_INT_RGB);
        AttributedString testAttributedString = new AttributedString("test");
        BufferedImage test = null;
        try {
            test = this.doubleTextPaster.pasteText(testBufferedImage,testAttributedString);
        } catch (CaptchaException e) {
            assertNotNull(e);
        }
        assertNotNull(test);
        assertEquals(testBufferedImage.getHeight(),test.getHeight());
        assertEquals(testBufferedImage.getWidth(),test.getWidth());
    }

    public void testGetMaxAcceptedWordLenght() {
        assertEquals(this.maxAcceptedWordLength.intValue(),
            this.doubleTextPaster.getMaxAcceptedWordLenght());
    }

    public void testGetMinAcceptedWordLenght() {
        assertEquals(this.minAcceptedWordLength.intValue(),
            this.doubleTextPaster.getMinAcceptedWordLenght());
    }

}
