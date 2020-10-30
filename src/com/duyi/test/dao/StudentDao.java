package com.duyi.test.dao;

import com.duyi.orm.util.DaoProxy;
import com.duyi.orm.util.Param;
import com.duyi.orm.util.SQL;
import com.duyi.orm.util.SQLEnums;
import com.duyi.test.domain.Student;
import java.util.List;

public interface StudentDao {

    public static final StudentDao instance = (StudentDao) DaoProxy.getInstance(StudentDao.class);

    @SQL(sql = "insert into student (`name`, `age`) values (?, ?)", type = SQLEnums.INSERT, resultType = Student.class)
    public Student insertStudent(@Param("name") String name, @Param("age") int age);

    @SQL(sql = "select * from student;", type = SQLEnums.SELECT, resultType = Student.class)
    public List<Student> queryAllStudent();



}
