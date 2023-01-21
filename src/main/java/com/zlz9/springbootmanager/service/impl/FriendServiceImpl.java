package com.zlz9.springbootmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.pojo.Friend;
import com.zlz9.springbootmanager.pojo.LoginUser;
import com.zlz9.springbootmanager.service.ChatService;
import com.zlz9.springbootmanager.service.FriendService;
import com.zlz9.springbootmanager.mapper.FriendMapper;
import com.zlz9.springbootmanager.service.UserService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import com.zlz9.springbootmanager.vo.FriendVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 23340
 * @description 针对表【h_friend】的数据库操作Service实现
 * @createDate 2023-01-18 13:52:00
 */
@Service
@Transactional
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
        implements FriendService {
    @Autowired
    FriendMapper friendMapper;
   @Autowired
   UserService userService;
   @Autowired
    ChatService chatService;
    @Override
    public ResponseResult makeFriend(Long friendId) {
        /**
         * 将朋友添加到朋友的列表
         */
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id = loginUser.getUser().getId();
        LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<>();
        Friend friend = new Friend();
//        插入之前先查询是是否已经插入数据
        wrapper.eq(Friend::getFriendId,friendId);
        wrapper.eq(Friend::getUserId, id);
        Friend hasFriend = friendMapper.selectOne(wrapper);
        if (!ObjectUtils.isEmpty(hasFriend)) {
            return new ResponseResult<>(303,"已经添加好友，请勿重复添加");
        }
        friend.setFriendId(friendId);
        friend.setCreateTime(System.currentTimeMillis());
        friend.setUserId(id);
        int insert = friendMapper.insert(friend);
        return new ResponseResult<>(200, "已成功添加好友");
    }

    /**
     * 根据id拉黑朋友
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult blackFriend(Long id) {
        Friend friend = friendMapper.selectById(id);
        friend.setIsBlack(true);
        return new ResponseResult<>(200, "已成功拉黑");
    }

    /**
     * 根据id删除朋友
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult delFriend(Long id) {
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getFriendId, id);
        friendMapper.delete(queryWrapper);
        return new ResponseResult<>(200, "删除好友成功");
    }

    @Override
    public Friend selectFriendById(Long toUserId) {
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getFriendId, toUserId);
        return friendMapper.selectOne(queryWrapper);
    }

    /**
     * 获取朋友列表和最新的一条消息
     * @return
     */
    @Override
    public ResponseResult getFriendListAndLastMsg() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Friend::getFriendId,loginUser.getUser().getId());
        List<Friend> friends = friendMapper.selectList(queryWrapper);
        List<FriendVo> friendVoList = new ArrayList<>();
        for (Friend friend : friends) {
            FriendVo friendVo = new FriendVo();
            friendVo.setFriend(userService.selectAuthorById(friend.getFriendId()));
            friendVo.setLastMsg(chatService.selectLastMgsById(friend.getFriendId(),loginUser.getUser().getId()));
            friendVo.setCreateTime(chatService.selectMsgCreateTimeById(friend.getFriendId()));
            friendVoList.add(friendVo);
        }
        return new ResponseResult<>(200,friendVoList);
    }
}




