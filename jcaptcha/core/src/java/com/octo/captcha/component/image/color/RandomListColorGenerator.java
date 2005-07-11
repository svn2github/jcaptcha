/*
 * Created on 3 mai 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.octo.captcha.component.image.color;

import java.awt.Color;
import java.util.Random;

import com.octo.captcha.CaptchaException;

/**
 * A RandomListColor returns a random color have been picked from a user defined colors list.
 * 
 * @author Benoit Doumas
 * @author Chrsitian Blavier
 */
public class RandomListColorGenerator implements ColorGenerator
{
    /**
     * List of colors that can be selected
     */
    private Color[] colorsList = null;

    /**
     * Use for random color selection
     */
    private Random random = new Random();

    /**
     * Constructor that take an array of Color
     * @param colorsList the array of color
     */
    public RandomListColorGenerator(Color[] colorsList)
    {
        if (colorsList == null) 
        {
            throw new CaptchaException("Color list cannot be null");
        }
        for (int i = 0; i < colorsList.length; i++)
        {
            if (colorsList[i] == null)
            {
                throw new CaptchaException("One or several color is null");
            }
        }
        this.colorsList = colorsList;
    }

    /**
     * @see com.octo.captcha.component.image.color.ColorGenerator#getNextColor()
     */
    public Color getNextColor()
    {
        int index = random.nextInt(this.colorsList.length);
        return this.colorsList[index];
    }

}
