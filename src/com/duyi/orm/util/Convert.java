package com.duyi.orm.util;

public interface Convert<T> {
    public T parse(String value);
}
