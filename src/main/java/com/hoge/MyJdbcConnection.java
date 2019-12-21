package com.hoge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyJdbcConnection {
    // connection database
    static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
    static final String JDBC_URL = "jdbc:oracle:thin:@";
    static final String DB_HOST = System.getProperty("DB_HOST", "localhost"); // oracle on EC2
    static final String DB_PORT = System.getProperty("DB_PORT", "1521");
    static final String DB_SID = System.getProperty("DB_SID", "xe");
    static final String DB_USR = System.getProperty("DB_USR", "usr1");
    static final String DB_PASS = System.getProperty("DB_PASS", "pass1");

    static private MyJdbcConnection instance;

    private Connection con;

    private MyJdbcConnection() {
        try {
            Class.forName(MyJdbcConnection.JDBC_DRIVER);
            con = DriverManager.getConnection(MyJdbcConnection.JDBC_URL + MyJdbcConnection.DB_HOST + ":" + MyJdbcConnection.DB_PORT + ":" + MyJdbcConnection.DB_SID, MyJdbcConnection.DB_USR, MyJdbcConnection.DB_PASS);
            // logger.info("connect OK");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public MyJdbcConnection getInstance() {
        if (instance == null) {
            instance = new MyJdbcConnection();
        }
        return instance;
    }
    public Connection getCon() {
        return con;
    }
}
