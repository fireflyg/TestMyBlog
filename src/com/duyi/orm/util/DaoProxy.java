package com.duyi.orm.util;

import com.duyi.orm.db.SqlSessionFactory;
import com.duyi.orm.db.SqlTask;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class DaoProxy {

    /**
     * 创建动态代理的方法，需要传入一个Class
     * java的动态代理规定，传入的class必须是以下两种情况之一
     * 1. 接口
     * 2. 类实现了某个接口
     * @param clazz
     * @return
     */
    public static Object getInstance(Class<?> clazz) {

        MethodProxy invocationHandler = new MethodProxy();

        // JDK提供的创建动态代理的方法。
        // 需要三个参数：
        // 1. 当前传入类的加载器。
        // 2. 传入实现接口的class
        // 3. 传入代理方法。(当调用原对象的某个方法的时候，系统会自动调用这个对象的invoke方法来替代原方法。)
        Object newProxyInstance = Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[] { clazz },
                invocationHandler);

        return (Object)newProxyInstance;
    }
}

class MethodProxy implements InvocationHandler {

    /**
     * 当系统调用某个method的时候，系统会自动调用这个方法。
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)  throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {//传进来的是实例
            return method.invoke(this, args);
        } else {//传进来的是接口
            SQL sql = method.getAnnotation(SQL.class);//传进来的是某个接口，获取方法上的SQL注解。
            return execute(method, args, sql);
        }
    }

    /**
     * 执行某个方法的时候，要根据SQL注解的类型来判断进行什么操作
     * @param method
     * @param args
     * @param sql
     * @return
     * @throws Exception
     */
    public Object execute(Method method, Object[] args, SQL sql) throws Exception {
        if (sql != null) {
            switch (sql.type()) {
                case INSERT: return executeInsert(sql.sql(), args, sql.resultType());
                case SELECT: {//因为数据库的查询可能会返回一个集合，也可能会返回一个单独的对象。
                    if (isCollection(method.getReturnType())) {//返回结果是一个集合的情况
                        return executeSelectReturnCollection(sql.sql(), args, method.getReturnType(), sql.resultType());
                    } else {//返回结果是一个单独对象的情况
                        return executeSelect(sql.sql(), args, method.getReturnType(), sql.resultType());
                    }
                }
                case UPDATE: return executeUpdate(sql.sql(), args, sql.resultType());
                case DELETE: return executeDelete(sql.sql(), args, sql.resultType());
            }
        }

        return new ArrayList<>();
    }

    private Object executeInsert(String sql, Object[] args, Class resultType) throws SQLException {
        SqlTask task = new SqlTask(sql, args);
        return SqlSessionFactory.executeInsert(task);
    }

    private <T> T executeSelect(String sql, Object[] args, Class returnType, Class<T> resultType) throws Exception {
        SqlTask task = new SqlTask(sql, args);
        if (resultType != returnType) {
            throw new Exception("返回类型与指定类型不符");
        }
        return SqlSessionFactory.executeSelect(task, returnType, resultType);
    }
    private <T> Collection<T> executeSelectReturnCollection(String sql, Object[] args, Class returnType, Class<T> resultType) throws Exception {
        SqlTask task = new SqlTask(sql, args);
        return SqlSessionFactory.executeSelectReturnList(task, returnType, resultType);
    }

    private Integer executeUpdate(String sql, Object[] args, Class resultType) throws SQLException {
        SqlTask task = new SqlTask(sql, args);
        return SqlSessionFactory.executeUpdate(task);
    }

    private Integer executeDelete(String sql, Object[] args, Class resultType) throws SQLException {
        SqlTask task = new SqlTask(sql, args);
        return SqlSessionFactory.executeUpdate(task);
    }

    private static boolean isCollection(Class clazz) {
        if (clazz == Collection.class) {
            return true;
        }
        Class[] classList = clazz.getInterfaces();
        for (Class temp : classList) {
            boolean flag = isCollection(temp);
            if (flag) {
                return true;
            }
        }
        return false;
    }

}
