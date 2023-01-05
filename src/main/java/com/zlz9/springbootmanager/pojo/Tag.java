package com.zlz9.springbootmanager.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName h_tag
 */
@TableName(value ="h_tag")
@Data
public class Tag implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 作者id
     */
    private Long authorId;

    /**
     * 状态（0正常，1已删除）
     */
    private Boolean isDel;
    /**
     * 图标
     */
    private String icon;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}