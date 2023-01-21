package com.zlz9.springbootmanager.controller;

import com.zlz9.springbootmanager.service.FriendService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-01-18 14:44
 **/
@RestController
@RequestMapping("api")
public class FriendController {
    @Autowired
    FriendService friendService;
    @PostMapping("chat/friend")
    public ResponseResult chatFriend(Long friendId){
        return friendService.makeFriend(friendId);
    }
    @PostMapping("black/friend")
    public ResponseResult blackFriend(Long id){
        return friendService.blackFriend(id);
    }
    @PostMapping("del/friend")
    public ResponseResult delFriend(Long id){
        return friendService.delFriend(id);
    }
    @GetMapping("friend/list")
    public ResponseResult getFriendListAndLastMsg(){
        return friendService.getFriendListAndLastMsg();
    }
}
