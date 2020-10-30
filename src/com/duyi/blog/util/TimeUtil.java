package com.duyi.blog.util;

public class TimeUtil {

    public static int getNow() {
        long timestamp = System.currentTimeMillis();
        return (int)(timestamp / 1000);
    }
}
