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
 */
package com.octo.captcha.utils;

import java.awt.Toolkit;

import java.util.Properties;

/**
 * <p>Description: This Factory is used in order to switch from the java.awt.Toolkit
 * component to other implementation like <a href="http://www.eteks.com/pja/en/">PJA Toolkit</a>.
 * By default this factory return the java.awt.Toolkit object.
 * But if the the parameter toolkit.implementation is fixed as a parameter of
 * the virtual machine with the value of the class name of another implementatio
 * of Toolkit, this factory return an implementation of this class.
 * For exemple if you set to your virtual machine 
 * -Dtoolkit.implementation=com.eteks.awt.PJAToolkit, the factory
 * returns an implementation of com.eteks.awt.PJAToolkit
 * </p>
 * @author <a href="mailto:mga@octo.com">Mathieu Gandin</a>
 * @version 1.0
 */
public class ToolkitFactory {
    
    private static String TOOLKIT_IMPL = "toolkit.implementation";
    private static String toolkitClass;
    
    public static Toolkit getToolkit() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        
        Properties props = System.getProperties();
        
        try {
            
            if(props.containsKey(TOOLKIT_IMPL)) {
                toolkitClass = props.getProperty(TOOLKIT_IMPL);
            }
            if(toolkitClass != null) {
                defaultToolkit = (Toolkit)Class.forName(toolkitClass).newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultToolkit;
    }

}
