package com.zlz9.springbootmanager.service;

import com.zlz9.springbootmanager.pojo.Chat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlz9.springbootmanager.utils.ResponseResult;

/**
* @author 23340
* @description 针对表【h_chat】的数据库操作Service
* @createDate 2023-01-18 13:52:00
*/
public interface ChatService extends IService<Chat> {

    ResponseResult sendMessage(Long userId, String msg);

    ResponseResult getChatListById(Long toUserId);

    String selectLastMgsById(Long friendId,Long userId);

    Long selectMsgCreateTimeById(Long friendId);
}
