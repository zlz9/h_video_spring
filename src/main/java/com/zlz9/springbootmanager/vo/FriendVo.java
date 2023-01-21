package com.zlz9.springbootmanager.vo;

import lombok.Data;

/**
 * <h4>springboot-manager</h4>
 * <p>朋友聊天列表vo</p>
 *
 * @author : zlz
 * @date : 2023-01-19 15:04
 **/
@Data
public class FriendVo {
    private AuthorVo friend;
//    最新一条消息
    private String lastMsg;
    private Long createTime;
}
