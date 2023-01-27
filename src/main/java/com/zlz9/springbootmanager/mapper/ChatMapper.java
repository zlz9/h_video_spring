package com.zlz9.springbootmanager.mapper;

import com.zlz9.springbootmanager.pojo.Chat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 23340
* @description 针对表【h_chat】的数据库操作Mapper
* @createDate 2023-01-18 13:52:00
* @Entity com.zlz9.springbootmanager.pojo.Chat
*/
@Mapper
public interface ChatMapper extends BaseMapper<Chat> {

    String selectLastMsg(Long friendId,Long userId);

    Long selectMsgCreateTimeById(Long friendId);

    List<Chat> selectChatListById(Long toUserId,Long userId ,Integer page,Integer pageSize);
}




