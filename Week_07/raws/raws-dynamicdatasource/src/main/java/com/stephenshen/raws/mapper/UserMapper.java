package com.stephenshen.raws.mapper;

import com.stephenshen.raws.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @InterfaceName : UserMapper  //接口名
 * @Description :   //描述
 * @Author : StephenShen  //作者
 * @Date: 2020-12-02 12:46  //时间
 */
@Mapper//指定这是一个操作数据库的mapper
public interface UserMapper {

    String INSERT_SQL = "insert into user " +
            "(user_id, nick_name, mobile_phone, create_time, update_time) " +
            "values " +
            "(#{userId}, #{nickName}, #{mobilePhone}, #{createTime}, #{updateTime})";

    String DELETE_SQL = "delete from user";

    String SELECT_SQL = "select user_id, nick_name, mobile_phone, create_time, update_time from user";

    String UPDATE_SQL = "update user set " +
            "nick_name = #{nickName}, mobile_phone = #{mobilePhone}, update_time = #{updateTime}";

    String WHERE_USER_ID = " where user_id = #{userId}";

    @Insert(INSERT_SQL)
    void insert(User user);

    @Delete(DELETE_SQL + WHERE_USER_ID)
    void deleteByUserId(@Param("userId") Long userId);

    @Update(UPDATE_SQL + WHERE_USER_ID)
    void updateByUserId(User user);

    @Select(SELECT_SQL)
    List<User> selectAll();

    @Select(SELECT_SQL + WHERE_USER_ID)
    User selectByUserId(@Param("userId") Long userId);
}
