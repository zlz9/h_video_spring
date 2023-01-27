package com.zlz9.springbootmanager.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <h4>springboot-manager</h4>
 * <p>聊天vo</p>
 *
 * @author : zlz
 * @date : 2023-01-18 19:58
 **/
@Data
public class ChatVo{
    private  Long id;

    /**
     * 消息内容
     */
    private String message;

    /**
     * （0接收，1未接收）
     */
    private Boolean status;

    /**
     * 创建事件
     */
    private Long createTime;

    /**
     * 发送者
     */
    private AuthorVo sendUser;

    /**
     * 接收者
     */
    private AuthorVo receiveUser;
}
