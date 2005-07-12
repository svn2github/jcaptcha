package com.octo.captcha.engine.bufferedengine.buffer;


public class MemoryCaptchaBufferTest extends CaptchaBufferTestAbstract
{

    /**
     * @see com.octo.captcha.engine.bufferedengine.buffer.CaptchaBufferTestAbstract#getBuffer()
     */
    public CaptchaBuffer getBuffer()
    {
        return new MemoryCaptchaBuffer();
    }

}
