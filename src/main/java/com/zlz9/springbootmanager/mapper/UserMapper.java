package com.zlz9.springbootmanager.mapper;

import com.zlz9.springbootmanager.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 23340
* @description 针对表【h_user】的数据库操作Mapper
* @createDate 2023-01-01 14:20:42
* @Entity com.zlz9.springbootmanager.pojo.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




