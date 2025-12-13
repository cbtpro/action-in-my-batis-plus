package com.chenbitao.action_in_my_batis_plus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chenbitao.action_in_my_batis_plus.domain.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE age > #{minAge}")
    List<User> selectUsersByMinAge(@Param("minAge") Integer minAge);
}