package com.duyi.test;

import com.duyi.blog.dao.BlogDao;
import com.duyi.blog.domain.Blog;
import com.duyi.test.dao.StudentDao;
import com.duyi.test.dao.UserDao;
import com.duyi.test.domain.Student;
import com.duyi.test.domain.User;

import java.util.List;

public class Test3 {
    public static void main(String[] args) {
//        List<Student> studentList = StudentDao.instance.queryAllStudent();
//
//        for (Student student : studentList) {
//            System.out.println(student);
//        }
//
//        List<User> userList = UserDao.instance.queryAllUser();
//        for (User user : userList) {
//            System.out.println(user);
//        }


        List<Blog> blogList = BlogDao.instance.queryAllBlog();
        System.out.println(blogList);
    }
}
