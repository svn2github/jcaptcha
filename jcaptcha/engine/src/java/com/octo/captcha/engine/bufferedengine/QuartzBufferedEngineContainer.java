/*
 * Created on 13 mai 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.octo.captcha.engine.bufferedengine;

import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.bufferedengine.buffer.CaptchaBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Quartz implmentation of the BufferedEngineContainer
 *
 * @author Benoit Doumas
 */
public class QuartzBufferedEngineContainer extends BufferedEngineContainer {

    private static final Log log = LogFactory.getLog(QuartzBufferedEngineContainer.class.getName());

    public QuartzBufferedEngineContainer(CaptchaEngine engine, CaptchaBuffer memoryBuffer,
                                         CaptchaBuffer diskBuffer, ContainerConfiguration containerConfiguration) {
        super(engine, memoryBuffer, diskBuffer, containerConfiguration);
    }

}
