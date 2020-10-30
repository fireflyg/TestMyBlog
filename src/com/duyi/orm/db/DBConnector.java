package com.duyi.orm.db;

import com.duyi.orm.config.DBConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    static {
        try {
            Class.forName(DBConfig.getConfig("driver"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
                DBConfig.getConfig("url"),
                DBConfig.getConfig("userName"),
                DBConfig.getConfig("password"));
    }

}
