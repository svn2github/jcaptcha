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

package com.octo.captcha.engine.pix;

import com.octo.captcha.engine.FactoryInitializer;
import com.octo.captcha.engine.hsql.HSQLUtils;
import com.octo.captcha.pix.PixCaptcha;
import com.octo.captcha.pix.PixCaptchaFactory;
import com.octo.captcha.utils.ImageToFile;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class HSQLPixCaptchaEngine {

    private String filename = "CAPTCHAS_DB";
    private HSQLUtils db;
    private PixCaptchaFactory factory;

    private static final String TABLE_NAME="PixCaptchas";
    private static final String CREATE_TABLE="create table "+TABLE_NAME+" (question VARCHAR,response VARCHAR,pix BINARY)";
    public static final String GET_COUNT="SELECT COUNT(*) FROM "+ TABLE_NAME;
    public static final String INSERT="INSERT into "+TABLE_NAME +" values (?,?,?) ";
    public static final String SELECT = "SELECT * FROM "+TABLE_NAME;
    public HSQLPixCaptchaEngine(PixCaptchaFactory factory, String filename) {

        this.filename = filename!=null?filename:this.filename;
    }

    public void init() throws Exception{
       db = new HSQLUtils(filename);
      db.query(CREATE_TABLE);
        db.query(GET_COUNT);
       for(int i=0;i<100;i++){
           ImageToFile encoder = new ImageToFile();
           PixCaptcha capthca =this.factory.getPixCaptcha();
           ByteArrayOutputStream stream = new ByteArrayOutputStream();
           encoder.encodeJPG(stream, capthca.getPixChallenge());
           db.insertPixCaptcha(INSERT,capthca.getQuestion(),capthca.getResponse(),stream.toByteArray());

       }
               
        db.shutdown();
    }


    public Collection getCaptchas(Integer size) {
        return null;
    }

    public static void main(String[] args)  throws Exception{
        HSQLPixCaptchaEngine engine = new HSQLPixCaptchaEngine(FactoryInitializer.getPixCaptchaFactory(),null );
        engine.init();
    }
}
