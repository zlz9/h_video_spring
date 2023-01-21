package com.zlz9.springbootmanager.service;

import com.zlz9.springbootmanager.pojo.Friend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlz9.springbootmanager.utils.ResponseResult;

/**
* @author 23340
* @description 针对表【h_friend】的数据库操作Service
* @createDate 2023-01-18 13:52:00
*/
public interface FriendService extends IService<Friend> {

    ResponseResult makeFriend(Long friendId);

    ResponseResult blackFriend(Long id);

    ResponseResult delFriend(Long id);

    Friend selectFriendById(Long toUserId);

    ResponseResult getFriendListAndLastMsg();
}
