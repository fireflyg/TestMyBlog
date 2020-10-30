package com.duyi.orm.pool;

import com.duyi.orm.config.DBConfig;
import com.duyi.orm.db.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlConnectionPool {

    /**
     * 数据库连接的集合，默认的长度为用户配置的最小连接池长度。
     * 如果用户没有配置默认的数据库连接长度，则默认数据库连接池最小长度为1
     */
    private static ConnectionProxy[] connectionProxies = new ConnectionProxy[DBConfig.getIntagerValue("minPoolSize", "1")];
    /**
     * 数据库连接集合的位图，位图用来描述数据库连接是否存在、是否空闲。
     * 其中-1为标识该位置Connection对象为null；0标识对应的Connection处于空闲状态，1标识对应的Connection处于忙碌状态。
     */
    private static byte[] connectionProxyBitMap = new byte[DBConfig.getIntagerValue("minPoolSize", "1")];
    private static int total = 0;//当前线程池中的线程总数

    private static final byte BUSY_VALUE = 1;//常量值1，表示线程正在繁忙
    private static final byte FREE_VALUE = 0;//常量值0，表示线程正在空闲
    private static final byte NULL_VALUE = -1;//常量值-1，表示该位置当前没有Connection实例。

    /**
     * 初始化线程池位图，将所有位置标记为-1，因为在最开始的时候，并没有创建出任何Connection
     */
    static {
        for (int i = 0 ; i < connectionProxyBitMap.length ; i ++) {
            connectionProxyBitMap[i] = -1;
        }
    }

    /**
     * 获取空闲的Connection的位置，如果没有空闲的则返回-1
     * @return
     */
    private static int getFreeIndex() {
        for (int i = 0 ; i < connectionProxyBitMap.length ; i ++) {
            if (connectionProxyBitMap[i] == FREE_VALUE) return i;
        }
        return -1;
    }

    /**
     * 获取null位的索引，如果没有则返回-1
     * @return
     */
    private static int getNullIndex() {
        for (int i = 0 ; i < connectionProxyBitMap.length ; i ++) {
            if (connectionProxyBitMap[i] == NULL_VALUE) return i;
        }
        return -1;
    }

    /**
     * 连接数组扩容与位图数组扩容。
     * 当连接数组满时，将数组扩容一倍
     * 返回扩容后第一个空的位置
     * @return
     */
    private static int grow() {
        ConnectionProxy[] newConnections = new ConnectionProxy[connectionProxies.length * 2];//创建新的连接数组，长度为当前数组长度的2倍
        byte[] newConnectionBitMap = new byte[connectionProxyBitMap.length * 2];//创建新的位图，长度为当前数组长度的2倍
        System.arraycopy(connectionProxies, 0, newConnections, 0, connectionProxies.length);//将当前的连接数组拷贝到新创建的数组
        System.arraycopy(connectionProxyBitMap, 0, newConnectionBitMap, 0, connectionProxyBitMap.length);//将当前的位图数组拷贝到新创建的位图数组
        int firstNullIndex = connectionProxies.length;//扩容之后，当前数组长度的位置就是第一个空位。
        connectionProxies = newConnections;//将新创建的数组替代连接池数组
        connectionProxyBitMap = newConnectionBitMap;//将新创建的位图数组，替换当前的位图数组
        for (int i = firstNullIndex ; i < connectionProxyBitMap.length ; i ++) {//新创建的位图数组空位默认为0，需要初始化为-1
            connectionProxyBitMap[i] = -1;
        }
        return firstNullIndex;//将新的位图数组中第一个Null位置的索引返回
    }

    /**
     * 为指定位置分配Connection，如果该位置没有Connection，则创建Connection。
     * 如果这个位置已经有Connection，则直接
     * @param index
     * @return
     * @throws SQLException
     */
    private static ConnectionProxy distribute (int index) throws SQLException {
        if (connectionProxyBitMap[index] == BUSY_VALUE) {//已经被分配，不能再次分配
            return null;
        }
        ConnectionProxy connectionProxy= null;
        if (connectionProxyBitMap[index] == NULL_VALUE) {//当前位置内容为空，需要创建连接
            connectionProxy = new ConnectionProxy(DBConnector.createConnection(), index);
            connectionProxies[index] = connectionProxy;
        } else if (connectionProxyBitMap[index] == FREE_VALUE) {//当前位置的Connection处于空闲状态
            connectionProxy = connectionProxies[index];
        }
        connectionProxyBitMap[index] = BUSY_VALUE;
        return connectionProxy;
    }

    /**
     * 从连接池中获取一个空闲的连接，如果连接池中没有空闲的连接则创建一个连接。
     * @return
     */
    public static synchronized ConnectionProxy getConnection() throws SQLException {
        int freeIndex = getFreeIndex();
        if (freeIndex > -1) {//如果存在空闲的线程，则返回空闲的线程
            return distribute(freeIndex);
        } else if (total < DBConfig.getIntagerValue("maxPoolSize", "1")) {//如果不存在空闲的连接，但是连接数没有达到最大连接数，则创建一个连接。
            int nullIndex = getNullIndex();
            if (nullIndex == -1) {//数组长度不够，需要扩容
                nullIndex = grow();
            }
            return distribute(nullIndex);
        }
        return null;
    }

    /**
     * 将连接返还，返还时，将位图的响应位置标为0.
     * @param connectionProxy
     */
    protected static synchronized void giveBack(ConnectionProxy connectionProxy) {
        connectionProxyBitMap[connectionProxy.index] = 0;
    }

    public static class ConnectionProxy {

        private Connection instance;
        private int index;

        protected ConnectionProxy(Connection connection, int index) {
            this.instance = connection;
            this.index = index;
        }

        public void close() throws SQLException {
            this.instance.close();
            SqlConnectionPool.giveBack(this);
        }

        public PreparedStatement preparedStatement(String sql) throws SQLException {
            return this.instance.prepareStatement(sql);
        }

        public PreparedStatement preparedStatementRGK(String sql) throws SQLException {
            return this.instance.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        }
    }

}
