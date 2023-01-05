package com.zlz9.springbootmanager.mapper;

import com.zlz9.springbootmanager.pojo.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 23340
* @description 针对表【h_user_role】的数据库操作Mapper
* @createDate 2023-01-01 14:20:42
* @Entity com.zlz9.springbootmanager.pojo.UserRole
*/
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}




