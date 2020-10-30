package com.duyi.orm.db;

import com.duyi.orm.pool.SqlConnectionPool;
import com.duyi.test.domain.User;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlSessionFactory {

    /**
     * 由于PrepareStatement的sql可以是带有参数的，所以需要将参数设置进去。
     * @param preparedStatement
     * @param params
     * @throws SQLException
     */
    private static void setParams(PreparedStatement preparedStatement, Object[] params) throws SQLException {
        if (params == null || params.length == 0) {
            return;
        }
        for (int i = 0 ; i < params.length ; i ++) {
            if (params[i] instanceof String) {
                preparedStatement.setString(i + 1, (String) params[i]);
            } else if (params[i] instanceof Integer) {
                preparedStatement.setInt(i + 1, (Integer) params[i]);
            } else if (params[i] instanceof Double) {
                preparedStatement.setDouble(i + 1, (Double) params[i]);
            } else if (params[i] instanceof Date) {
                preparedStatement.setDate(i + 1, (Date) params[i]);
            } else if (params[i] instanceof Boolean) {
                preparedStatement.setBoolean(i + 1, (Boolean) params[i]);
            }
        }
    }

    /**
     * 执行select操作
     * @param task
     * @throws SQLException
     */
    public static <T> T executeSelect(SqlTask task, Class returnType, Class<T> resultType) throws Exception {
        Collection<T> collection = executeSelectReturnList(task, Collection.class, resultType);
        if (collection.size() > 0) {
            return (T) collection.toArray()[0];
        } else {
            return null;
        }
    }

    /**
     * 执行sql的查询，并且返回一个结果的集合
     * @param task
     * @param returnType
     * @param resultType
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> Collection<T> executeSelectReturnList(SqlTask task, Class returnType, Class<T> resultType) throws Exception {
        SqlConnectionPool.ConnectionProxy connectionProxy = SqlConnectionPool.getConnection();
        PreparedStatement preparedStatement = connectionProxy.preparedStatement(task.getSql());
        setParams(preparedStatement, task.getParams());
        ResultSet resultSet = preparedStatement.executeQuery();
        Collection collection;
        if (returnType.isInterface()) {//要返回的类型是接口
            collection = createCollection(returnType);
        } else {//是个具体的集合类
            collection = (Collection) returnType.newInstance();
        }
        while (resultSet.next()) {
            T obj = construct(resultSet, resultType);//根据当前的resultSet构建对象
            collection.add(obj);
        }
        return collection;
    }

    /**
     * 执行insert操作，并返回id
     * @param task
     * @return
     * @throws SQLException
     */
    public static long executeInsert(SqlTask task) throws SQLException {
        try {
            SqlConnectionPool.ConnectionProxy connectionProxy = SqlConnectionPool.getConnection();
            PreparedStatement preparedStatement = connectionProxy.preparedStatementRGK(task.getSql());
            setParams(preparedStatement, task.getParams());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 执行update或delete操作
     * @param task
     * @return
     * @throws SQLException
     */
    public static int executeUpdate(SqlTask task) throws SQLException {
        SqlConnectionPool.ConnectionProxy connectionProxy = SqlConnectionPool.getConnection();
        PreparedStatement preparedStatement = connectionProxy.preparedStatement(task.getSql());
        setParams(preparedStatement, task.getParams());
        return preparedStatement.executeUpdate();
    }

    /**
     * 将下划线式命名转换为驼峰式命名
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     *
     * @param obj
     * @param attr
     * @param resultSet
     * @throws NoSuchFieldException
     * @throws SQLException
     * @throws IllegalAccessException
     */
    private static Object setAttribute(Object obj, String attr, ResultSet resultSet) throws NoSuchFieldException, SQLException, IllegalAccessException {
        String humpAttr = lineToHump(attr);
        if (obj == null) {//判断要返回的是否为基本属性
            obj = resultSet.getObject(attr);
            return obj;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (isNormalAttr(field.getType())) {//是普通类型
                if (field.getName().equals(humpAttr)) {
                    field.setAccessible(true);
                    Class fieldType = field.getType();
                    field.set(obj, resultSet.getObject(attr, fieldType));
                }
            } else {
                field.setAccessible(true);
                setAttribute(field.get(obj), attr, resultSet);
            }
        }
        return null;
    }

    /**
     * 根据resultSet构建对象
     * @param resultSet
     * @param resultType
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     * @throws NoSuchFieldException
     */
    private static <T> T construct(ResultSet resultSet, Class<T> resultType) throws IllegalAccessException, InstantiationException, SQLException, NoSuchFieldException {
        T obj = createInstance(resultType);
        for (int i = 1 ; i <= resultSet.getMetaData().getColumnCount() ; i ++) {
            String metaName = resultSet.getMetaData().getColumnName(i);
            Object result = setAttribute(obj, metaName, resultSet);
            if (obj == null && result != null) {
                return (T)result;
            }
        }
        return obj;
    }

    /**
     * 创建集合，根据需要返回的集合类型创建相应的集合
     * @param clazz
     * @return
     * @throws Exception
     */
    private static Collection createCollection (Class clazz) throws Exception {
        if (clazz == List.class) {
            return new ArrayList();
        } else if (clazz == Set.class) {
            return new HashSet();
        } else if (clazz == Collection.class) {
            return new ArrayList();
        }
        throw new Exception("不支持的集合类型:" + clazz.getName());
    }

    private static Field getField(Class clazz, String fieldName) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (isNormalAttr(field.getType())) {//是普通类型
                if (field.getName().equals(fieldName)) return field;
            } else {
                Field result = getField(field.getType(), fieldName);
                if (result != null) return result;
            }

        }
        return null;
    }

    private static boolean isNormalAttr(Class clazz) {
        if (clazz != Boolean.class
                && clazz != Integer.class
                && clazz != Double.class
                && clazz != String.class
                && clazz != Long.class
                && clazz != Byte.class
                && clazz != Short.class
                && clazz != Float.class
                && clazz != Date.class
                && clazz != long.class
                && clazz != byte.class
                && clazz != short.class
                && clazz != int.class
                && clazz != double.class
                && clazz != float.class) {
            return false;
        }
        return true;
    }

    /**
     * 用递归的方式来创建对象，如果对象的属性不是基本的属性，则递归创建。
     * @param clazz
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static <T> T createInstance(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        if (isNormalAttr(clazz)) {
            return null;
        }
        T t = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!isNormalAttr(field.getType())) {
                field.setAccessible(true);
                field.set(t, createInstance(field.getType()));
            }
        }
        return t;
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        User user = createInstance(User.class);
        System.out.println(user.toString());
    }
}
