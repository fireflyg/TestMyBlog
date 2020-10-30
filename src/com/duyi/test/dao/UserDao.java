package com.duyi.test.dao;

import com.duyi.orm.util.DaoProxy;
import com.duyi.orm.util.SQL;
import com.duyi.orm.util.SQLEnums;
import com.duyi.test.domain.User;

import java.util.List;

public interface UserDao {

    public static final UserDao instance = (UserDao) DaoProxy.getInstance(UserDao.class);

    @SQL(sql = "select `uid`, `user_name`, `age`, `sex`, `tags`, `tid`, `tags_name`from `school`.`user` inner join `school`.`tags` on (`user`.`tags` = `tags`.`tid`);", type = SQLEnums.SELECT, resultType = User.class)
    public List<User> queryAllUser();

}
