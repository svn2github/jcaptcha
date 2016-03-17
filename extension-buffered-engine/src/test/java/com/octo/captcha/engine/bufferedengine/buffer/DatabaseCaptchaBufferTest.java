/*
 * JCaptcha, the open source java framework for captcha definition and integration
 * Copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

/*
 * jcaptcha, the open source java framework for captcha definition and integration
 * copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */
package com.octo.captcha.engine.bufferedengine.buffer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * <p><ul><li></li></ul></p>
 *
 * @author <a href="mailto:marc.antoine.garrigue@gmail.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class DatabaseCaptchaBufferTest extends CaptchaBufferTestAbstract {

    private DataSource datasource;
    private String CREATE = "CREATE TABLE jcaptcha_t ( timeMillis bigint NULL, hashCode bigint NULL,locale   	varchar(25) NULL,captcha OTHER NULL)";
    private String EMPTY = "DELETE from jcaptcha_t";


    public CaptchaBuffer getBuffer() {
        //just get initialize the database and create
        //get the datasource from spring conf
        this.datasource = (DataSource) (new XmlBeanFactory(new ClassPathResource("testDatabaseCaptchaBuffer.xml"))).getBean("dataSource");

        //drop and recreate the table
        Connection con = null;
        Statement ps = null;
        Statement ps2 = null;
        try {
            con = datasource.getConnection();
            ps = con.createStatement();
            try {
                ps.execute(CREATE);
            } catch (SQLException e) {
            }


            ps2 = con.createStatement();
            ps2.execute(EMPTY);

        } catch (SQLException e) {
                throw new RuntimeException(e);            
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (ps2 != null) {
                try {
                    ps2.close();
                } catch (SQLException e) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
        }
        DatabaseCaptchaBuffer buffer = new DatabaseCaptchaBuffer(datasource);
        return buffer;
    }


}
