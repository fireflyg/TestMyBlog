package com.duyi.test;

import com.duyi.blog.dao.BlogDao;
import com.duyi.blog.domain.Blog;
import com.duyi.orm.config.DBConfig;
import com.duyi.orm.db.DBConnector;

import java.sql.*;
import java.util.List;

public class JDBCConnectionTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
    /*    String URL="jdbc:mysql://127.0.0.1:3306/myblog?useUnicode=true&amp;characterEncoding=utf-8";
       String USER="root";
        String PASSWORD="root";
      //1.加载驱动程序
       Class.forName("com.mysql.jdbc.Driver");*/
        String URL= DBConfig.getConfig("url");
        String USER=DBConfig.getConfig("userName");
        String PASSWORD= DBConfig.getConfig("password");
        try {
            Class.forName(DBConfig.getConfig("driver"));
        } catch (Exception e) {
            e.printStackTrace();
        }
      //2.获得数据库链接
       Connection conn= DriverManager.getConnection(URL, USER, PASSWORD);
        Connection conn2= DBConnector.createConnection();


        List<Blog> blogList = BlogDao.instance.queryAllBlog();
        System.out.println(blogList);




        //3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
          Statement st=conn2.createStatement();
           ResultSet rs=st.executeQuery("select * from blog");
              //4.处理数据库的返回结果(使用ResultSet类)
                while(rs.next()){
                      System.out.println(rs.getString("title")+" "
                                         +rs.getString("author"));
                  }

            //关闭资源
              rs.close();
               st.close();
              conn.close();
         }
}
