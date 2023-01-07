package com.xii.shardingjdbc.mapper;

import com.xii.shardingjdbc.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Insert("insert t_user(nickname,password,age,sex) value(#{nickname},#{password},#{age},#{sex})")
    void addUser(User user);

    @Select("select * from t_user")
    List<User> getUserList();
}

