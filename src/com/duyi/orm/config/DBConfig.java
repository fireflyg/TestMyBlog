package com.duyi.orm.config;

import com.duyi.orm.util.Convert;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class DBConfig {

    private static Properties dbConfig;

    static {
        try {
            dbConfig = new Properties();
            InputStream in = new FileInputStream("D://gitproject//TestMyBlog//dbconfig.properties");
            dbConfig.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getConfig(String key) {
        return dbConfig.getProperty(key, "");
    }

    public static String getConfig(String key, String defaultValue) {
        return dbConfig.getProperty(key, defaultValue);
    }

    public static Integer getIntagerValue(String key) {
        return Integer.parseInt(dbConfig.getProperty(key, ""));
    }

    public static Integer getIntagerValue(String key, String defaultValue) {
        return Integer.parseInt(dbConfig.getProperty(key, defaultValue));
    }

    public static Double getDoubleValue(String key) {
        return Double.parseDouble(dbConfig.getProperty(key, ""));
    }

    public static Double getDoubleValue(String key, String defaultValue) {
        return Double.parseDouble(dbConfig.getProperty(key, defaultValue));
    }


    public static <T> T getConfigObject(String key, Convert<T> convert) {
        return convert.parse(dbConfig.getProperty(key, ""));
    }

    public static <T> T getConfigObject(String key, Convert<T> convert, String defaultValue) {
        return convert.parse(dbConfig.getProperty(key, defaultValue));
    }

}
