/*
 * Created on 3 mai 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.octo.captcha.component.image.color;

import java.awt.Color;

import com.octo.captcha.CaptchaException;

/**
 * Simple color generator that always return the same color
 * 
 * @author Benoit Doumas
 * @author Christian Blavier
 */
public class SingleColorGenerator implements ColorGenerator
{
    /**
     * Unique color to be used
     */
    public Color color = null;

    /**
     * construct a simple color generator
     * 
     * @param color
     *                  Unique color to be used
     */
    public SingleColorGenerator(Color color)
    {
        if (color == null)
        {
            throw new CaptchaException("Color is null");
        }
        this.color = color;
    }

    /**
     * @see com.octo.captcha.component.image.color.ColorGenerator#getNextColor()
     */
    public Color getNextColor()
    {
        return color;
    }

}
