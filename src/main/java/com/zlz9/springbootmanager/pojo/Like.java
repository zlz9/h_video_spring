package com.zlz9.springbootmanager.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName h_like
 */
@TableName(value ="h_like")
@Data
public class Like implements Serializable {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 点赞信息id
     */
    private String infoId;

    /**
     * 点赞用户id
     */
    private String likeUserId;

    /**
     * 点赞状态，0取消，1点赞
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}