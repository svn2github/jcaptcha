/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 *
 */
package com.octo.utils;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * A class that provides methods to extract init parameters from a FilterConfig
 *
 * @version $Id$
 *
 * @author <a href="mailto:sebastien.brunot@club-internet.fr">Sebastien Brunot</a>
 */
public class FilterConfigUtils
{
    /**
     * Get a String init parameter from a FilterConfig
     * @param theFilterConfig the FilterConfig from wich the parameter should be
     * extracted
     * @param theInitParameterName the name of the init parameter
     * @param isMandatory a boolean indicating if the init parameter is
     * mandatory
     * @return the init parameter value as a string, or null if this init
     * parameter is not defined but not mandatory
     * @throws ServletException is the initParameter is undefined whereas
     * mandatory (a message is provided in the exception)
     */
    public static String getStringInitParameter(
        FilterConfig theFilterConfig,
        String theInitParameterName,
        boolean isMandatory)
        throws ServletException
    {
        String returnedValue =
            theFilterConfig.getInitParameter(theInitParameterName);
        if (isMandatory && (returnedValue == null))
        {
            throw new ServletException(
                theInitParameterName
                    + " parameter must be declared for "
                    + theFilterConfig.getFilterName()
                    + " in web.xml");
        }
        return returnedValue;
    }

    /**
     * Get an Integer init parameter from a FilterConfig
     * @param theFilterConfig the FilterConfig from wich the parameter should be
     * extracted
     * @param theInitParameterName the name of the init parameter
     * @param isMandatory a boolean indicating if the init parameter is
     * mandatory
     * @param theMinValue the minimum value the init parameter can have
     * @param theMaxValue the maximum value the init parameter can have
     * @return the init parameter value as an Integer, or null if this Integer
     * parameter is not defined but not mandatory
     * @throws ServletException if :
     * <UL>
     *  <LI>the initParameter is undefined whereas mandatory </LI>
     *  <LI>the initParameter is defined but is not an integer value </LI>
     *  <LI>the initParameter is < minValue or > maxValue </LI>
     * </UL>
     * (a message is provided in the exception for each case).
     */
    public static Integer getIntegerInitParameter(
        FilterConfig theFilterConfig,
        String theInitParameterName,
        boolean isMandatory,
        int theMinValue,
        int theMaxValue)
        throws ServletException
    {
        Integer returnedValue = null;
        String returnedValueAsString =
            theFilterConfig.getInitParameter(theInitParameterName);
        if (isMandatory && returnedValueAsString == null)
        {
            throw new ServletException(
                theInitParameterName
                    + " parameter must be declared for "
                    + theFilterConfig.getFilterName()
                    + " in web.xml");
        }
        try
        {
            returnedValue = new Integer(returnedValueAsString);
        }
        catch (NumberFormatException e)
        {
            throw new ServletException(
                theInitParameterName
                    + " parameter must be an integer value "
                    + theFilterConfig.getFilterName()
                    + " in web.xml");
        }
        if ((returnedValue.intValue() < theMinValue)
            || (returnedValue.intValue() > theMaxValue))
        {
            throw new ServletException(
                theInitParameterName
                    + " parameter for "
                    + theFilterConfig.getFilterName()
                    + " in web.xml must be >= "
                    + theMinValue
                    + " and <= "
                    + theMaxValue);
        }
        return returnedValue;
    }

    /**
     * Get a boolean init parameter from a FilterConfig
     * @param theFilterConfig the FilterConfig from wich the parameter should be
     * extracted
     * @param theInitParameterName the name of the init parameter
     * @param isMandatory a boolean indicating if the init parameter is
     * mandatory
     * @return the init parameter value as a boolean, or null if this init
     * parameter is not defined but not mandatory
     * @throws ServletException is the initParameter is undefined whereas
     * mandatory (a message is provided in the exception)
     */
    public static boolean getBooleanInitParameter(
        FilterConfig theFilterConfig,
        String theInitParameterName,
        boolean isMandatory)
        throws ServletException
    {
        String returnedValueAsString =
            theFilterConfig.getInitParameter(theInitParameterName);
        if (isMandatory && (returnedValueAsString == null))
        {
            throw new ServletException(
                theInitParameterName
                    + " parameter must be declared for "
                    + theFilterConfig.getFilterName()
                    + " in web.xml");
        }
        boolean returnedValue = false;
        if (returnedValueAsString != null)
        {
            returnedValue = new Boolean(returnedValueAsString).booleanValue();
        }
        return returnedValue;
    }

}
