/*
 * Created on 3 mai 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.octo.captcha.component.image.color;

import java.awt.Color;

/**
 * ColorGenerator is an interface used by TextPaster to generate color for rendering each character
 * with specific color.
 * 
 * @author Benoit Doumas
 * @author Christian Blavier
 */
public interface ColorGenerator
{
    /**
     * This return a new color, from a finite set.
     * 
     * @return the next color
     */
    Color getNextColor();

}
