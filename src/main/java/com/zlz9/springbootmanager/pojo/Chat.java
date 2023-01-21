package com.zlz9.springbootmanager.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName h_chat
 */
@TableName(value ="h_chat")
@Data
public class Chat implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
     * 发送用户id
     */
    private Long userId;

    /**
     * 接收用户id
     */
    private Long toUserId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}