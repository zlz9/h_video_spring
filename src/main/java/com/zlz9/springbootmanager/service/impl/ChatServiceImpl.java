package com.zlz9.springbootmanager.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.mapper.ChatMapper;
import com.zlz9.springbootmanager.pojo.Chat;
import com.zlz9.springbootmanager.pojo.Friend;
import com.zlz9.springbootmanager.pojo.LoginUser;
import com.zlz9.springbootmanager.service.ChatService;
import com.zlz9.springbootmanager.service.FriendService;
import com.zlz9.springbootmanager.service.UserService;
import com.zlz9.springbootmanager.utils.RedisCache;
import com.zlz9.springbootmanager.utils.ResponseResult;
import com.zlz9.springbootmanager.vo.ChatVo;
import com.zlz9.springbootmanager.ws.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 23340
 * @description 针对表【h_chat】的数据库操作Service实现
 * @createDate 2023-01-18 13:52:00
 */
@Service
@Transactional
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat>
        implements ChatService {
    @Autowired
    WebSocketServer webSocketServer;
    @Autowired
    ChatMapper chatMapper;
    @Lazy
    @Autowired
    FriendService friendService;
    @Autowired
    UserService userService;
    @Autowired
    RedisCache redisCache;

    /**
     * 发送消息
     *
     * @param toUserId
     * @param msg
     * @return
     */
//   定义一个集合储存聊天记录
    public static List<Chat> chatList = new ArrayList<>();

    @Override
    public ResponseResult sendMessage(Long toUserId, String msg) {
        // 1.调用websocket 发送消息
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 2.验证关系是否有效
        Friend friend = friendService.selectFriendById(toUserId);
        if (friend == null) {
            return new ResponseResult<>(400, "请先添加好友");
        }
        if (!friend.getIsBlack()) {
            return new ResponseResult<>(505, "发送消息失败");
        }
        //3.将用户信息存储到数据库中
        Chat chat = new Chat();
        chat.setMessage(msg);
        chat.setCreateTime(System.currentTimeMillis());
        chat.setStatus(false);
        chat.setUserId(loginUser.getUser().getId());
        chat.setToUserId(toUserId);
        chatList.add(chat);
//     redis的key
        String CHAT_ID = "CHAT_LIST";
//        添加到redis缓存中
        redisCache.setCacheList(CHAT_ID, chatList);
//        添加到列表后清空列表
        chatList.clear();
//        包装成vo类
        ChatVo chatVo = new ChatVo();
        BeanUtils.copyProperties(chat, chatVo);
        chatVo.setSendUser(userService.selectAuthorById(loginUser.getUser().getId()));
        chatVo.setReceiveUser(userService.selectAuthorById(toUserId));
        chatVo.setCreateTime(System.currentTimeMillis());
//        websocket发送给消息
        webSocketServer.sendMessage(JSON.toJSONString(chatVo), toUserId);
        return new ResponseResult<>(200, "发送成功");
    }



    /**
     * 根据id获取
     * @param friendId
     * @return
     */
    @Override
    public String selectLastMgsById(Long friendId,Long userId) {
       return chatMapper.selectLastMsg(friendId,userId);
    }

    @Override
    public Long selectMsgCreateTimeById(Long friendId) {
       return chatMapper.selectMsgCreateTimeById(friendId);
    }

    /**
     * 查询最新的消息
     * @param toUserId
     * @return
     */
    @Override
    public ResponseResult getNewChatListById(Long toUserId) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id = loginUser.getUser().getId();
        List<Chat> chatList = redisCache.getCacheList("CHAT_LIST");
        List<ChatVo> chatVoList = new ArrayList<>();
//        读取聊天列表后进行过滤
        for (Chat chat : chatList) {
            if(chat.getToUserId().equals(toUserId) && chat.getUserId().equals(id) ||chat.getToUserId().equals(id) && chat.getUserId().equals(toUserId)){
//                添加到聊天列表
                chatVoList.add(copy(chat));
            }
        }
        if (CollectionUtils.isEmpty(chatVoList)) {
            return new ResponseResult<>(400,"未找到最新聊天记录");
        }
        return new ResponseResult(200, chatVoList);
    }

    /**
     * 获取更多的聊天信息
     * 分页查询
     * @param toUserId
     * @return
     */
    @Override
    public ResponseResult getMoreChatListById(Long toUserId ,Integer page,Integer pageSize) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getUser().getId();
        page = (page-1)*pageSize;
        List<Chat> chatList= chatMapper.selectChatListById(userId,toUserId,page,pageSize);
        List<ChatVo> chatVoList = copyChatList(chatList);

        return new ResponseResult<>(200,chatVoList);
    }

    private List<ChatVo> copyChatList(List<Chat> chatList) {
        List<ChatVo> chatVoList = new ArrayList<ChatVo>();
        for (Chat chat : chatList) {
            chatVoList.add(copy(chat));
        }
        return chatVoList;
    }

    private ChatVo copy(Chat chat) {
        ChatVo chatVo = new ChatVo();
        BeanUtils.copyProperties(chat, chatVo);
        chatVo.setSendUser(userService.selectAuthorById(chat.getUserId()));
        chatVo.setReceiveUser(userService.selectAuthorById(chat.getToUserId()));
        chatVo.setCreateTime(System.currentTimeMillis());
        return chatVo;
    }
}




