package com.zlz9.springbootmanager.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName h_friend
 */
@TableName(value ="h_friend")
@Data
public class Friend implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 朋友id
     */
    private Long friendId;

    /**
     * 创建事件
     */
    private Long createTime;

    /**
     * 更新事件
     */
    private Long updateTime;

    /**
     * （0拉黑，1正常）
     */
    private Boolean isBlack;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}