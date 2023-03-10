package com.zlz9.springbootmanager.controller;

import com.zlz9.springbootmanager.service.ChatService;
import com.zlz9.springbootmanager.service.FriendService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <h4>springboot-manager</h4>
 * <p>通信模块</p>
 *
 * @author : zlz
 * @date : 2023-01-18 12:41
 **/
@RestController
@RequestMapping("api")
public class ChatController {
    @Autowired
    ChatService chatService;
    @GetMapping("/send/{userId}/{msg}")
    public ResponseResult sendMessage(@PathVariable Long userId ,@PathVariable String msg ){
        return chatService.sendMessage(userId,msg);
    }
    @GetMapping("chat/new/{toUserId}")
    public ResponseResult getNewChatListById(@PathVariable Long toUserId){
      return chatService.getNewChatListById(toUserId);
    };
    @GetMapping("chat/more/{toUserId}/{page}/{pageSize}")
    public ResponseResult getMoreChatListById(@PathVariable Long toUserId,@PathVariable Integer page,@PathVariable Integer pageSize){
        return chatService.getMoreChatListById(toUserId,page,pageSize);
    }
}
