package com.octo.captcha.image.gimpy.wordtoimage.textpaster;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.text.AttributedString;

import com.octo.captcha.CaptchaException;

import junit.framework.TestCase;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class SimpleTextPasterTest extends TestCase {

    private SimpleTextPaster simpleTextPaster;
    private Integer minAcceptedWordLength = new Integer(10);
    private Integer maxAcceptedWordLength = new Integer(15);

    /**
     * Constructor for SimpleTextPasterTest.
     * @param name
     */
    public SimpleTextPasterTest(String name) {
        super(name);
    }

    public void setUp() {
        this.simpleTextPaster = new SimpleTextPaster(
            this.minAcceptedWordLength,this.maxAcceptedWordLength, Color.BLUE);
    }

    public void testPasteText() {
        BufferedImage testBufferedImage = new BufferedImage(100,50,BufferedImage.TYPE_INT_RGB);
        AttributedString testAttributedString = new AttributedString("test");
        BufferedImage test = null;
        try {
            test = this.simpleTextPaster.pasteText(testBufferedImage,testAttributedString);
        } catch (CaptchaException e) {
            assertNotNull(e);
        }
        assertNotNull(test);
        assertEquals(testBufferedImage.getHeight(),test.getHeight());
        assertEquals(testBufferedImage.getWidth(),test.getWidth());
    }

    public void testGetMaxAcceptedWordLenght() {
        assertEquals(this.maxAcceptedWordLength.intValue(),
            this.simpleTextPaster.getMaxAcceptedWordLenght());
    }

    public void testGetMinAcceptedWordLenght() {
        assertEquals(this.minAcceptedWordLength.intValue(),
            this.simpleTextPaster.getMinAcceptedWordLenght());
    }

}
