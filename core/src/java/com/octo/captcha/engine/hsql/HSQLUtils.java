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
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * Portions of this software are based upon public domain software
 * originally written at the National Center for Supercomputing Applications,
 * University of Illinois, Urbana-Champaign.
 */

package com.octo.captcha.engine.hsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;

/**
 * <p>Description: </p>
 * @author <a href="mailto:mag@octo.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public class HSQLUtils {
      Connection conn;    //our connnection to the db - presist for life of program
                        // we dont want this garbage collected until we are done

    public HSQLUtils(String db_file_name_prefix) throws Exception    // note more general exception
    {

        // Load the HSQL Database Engine JDBC driver
        // hsqldb.jar should be in the class path or made part of the current jar
        Class.forName("org.hsqldb.jdbcDriver");

        // connect to the database.   This will load the db files and start the
        // database if it is not alread running.
        // db_file_name_prefix is used to open or create files that hold the state
        // of the db.
        // It can contain directory names relative to the
        // current working directory
        conn = DriverManager.getConnection("jdbc:hsqldb:"
                                           + db_file_name_prefix,   // filenames
                                           "sa",                    // username
                                           "");                     // password
    }

    public void shutdown() throws SQLException {

        conn.close();   // if there are no other open connection
                        // db writes out to files and shuts down
                        // this happens anyway at garbage collection
                        // when program ends
    }

//use for SQL commands CREATE and SELECT

    public synchronized ResultSet query(String expression) throws SQLException {

        Statement st = null;
        ResultSet rs = null;

        st = conn.createStatement();            // statement objects can be reused with
                                                // repeated calls to execute but we
                                                // choose to make a new one each time
        rs = st.executeQuery(expression);       // run the query

        // do something with the result set.
        dump(rs);
        st.close();     // NOTE!! if you close a statement the associated ResultSet is
                        // closed too
                        // so you should copy the contents to some other object.
                        // the result set is invalidated also  if you recycle an Statement
                        // and try to execute some other query before the result set has been
                        // completely examined.
        return rs;
    }

//use for SQL commands DROP and INSERT and UPDATE
    public synchronized void update(String expression) throws SQLException {

        Statement st = null;

        st = conn.createStatement();                // statements

        int i = st.executeUpdate(expression);       // run the query

        if (i == -1) {
            System.out.println("db error : " + expression);
        }

        st.close();
    }    // void update()

    public synchronized void insertPixCaptcha(String sql, String question, String response, byte[] image) throws SQLException {

            PreparedStatement st = null;

            st = conn.prepareStatement(sql);                // statements
            st.setString(1,question);
            st.setString(2,response);
            st.setBytes(3,image);
            boolean i = st.execute();       // run the query

            if (i == false) {
                System.out.println("db error : " + sql);
            }

            st.close();
        }    // void update()


    public static void dump(ResultSet rs) throws SQLException {

        // the order of the rows in a cursor
        // are implementation dependent unless you use the SQL ORDER statement
        ResultSetMetaData meta   = rs.getMetaData();
        int               colmax = meta.getColumnCount();
        int               i;
        Object            o       = null;

        // the result set is a cursor into the data.  You can only
        // point to one row at a time
        // assume we are pointing to BEFORE the first row
        // rs.next() points to next row and returns true
        // or false if there is no next row, which breaks the loop
        for (;rs.next();) {
            for (i = 0; i < colmax; ++i) {
                o = rs.getObject(i + 1);    // Is SQL the first column is indexed
                                            // with 1 not 0
                System.out.print(o.toString() + " ");
            }

            System.out.println(" ");
        }
    }                                       //void dump( ResultSet rs )

    public static void main(String[] args) {

        HSQLUtils db = null;

        try {
            db = new HSQLUtils("db_file");
        } catch (Exception ex1) {
            ex1.printStackTrace();          // could not start db

            return;                         // bye bye
        }

        try {

            //make an empty table
            //
            // by declaring the id column IDENTITY, the db will automatically
            // generate unique values for new rows- useful for row keys
            db.query(
                "CREATE TABLE sample_table ( id INTEGER IDENTITY, str_col VARCHAR(256), num_col INTEGER)");
        } catch (SQLException ex2) {

            //ignore
            //ex2.printStackTrace();  // second time we run program
            //  should throw execption since table
            // already there
            //
            // this will have no effect on the db
        }

        try {

            // add some rows - will create duplicates if run more then once
            // the id column is automatically generated
            db.update(
                "INSERT INTO sample_table(str_col,num_col) VALUES('Ford', 100)");
            db.update(
                "INSERT INTO sample_table(str_col,num_col) VALUES('Toyota', 200)");
            db.update(
                "INSERT INTO sample_table(str_col,num_col) VALUES('Honda', 300)");
            db.update(
                "INSERT INTO sample_table(str_col,num_col) VALUES('GM', 400)");

            // do a query
            db.query("SELECT * FROM sample_table WHERE num_col < 250");

            // at end of program
            db.shutdown();
        } catch (SQLException ex3) {
            ex3.printStackTrace();
        }
    }    // main()
}
