package com.stephenshen.raws.mapper;

import com.stephenshen.raws.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @InterfaceName : UserMapper  //接口名
 * @Description :   //描述
 * @Author : StephenShen  //作者
 * @Date: 2020-12-02 12:46  //时间
 */
@Mapper//指定这是一个操作数据库的mapper
public interface UserMapper {

    @Select("SELECT * FROM user")
    List<User> selectAll();

    @Select("SELECT * FROM user where user_id = #{userId}")
    User selectByUserId(@Param("userId") Integer userId);
}
