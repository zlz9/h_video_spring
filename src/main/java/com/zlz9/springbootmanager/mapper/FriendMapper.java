package com.zlz9.springbootmanager.mapper;

import com.zlz9.springbootmanager.pojo.Friend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 23340
* @description 针对表【h_friend】的数据库操作Mapper
* @createDate 2023-01-18 13:52:00
* @Entity com.zlz9.springbootmanager.pojo.Friend
*/
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {

}




