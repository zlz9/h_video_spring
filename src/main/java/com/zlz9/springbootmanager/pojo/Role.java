package com.zlz9.springbootmanager.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName h_role
 */
@TableName(value ="h_role")
@Data
public class Role implements Serializable {
    /**
     * 角色id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 状态（1正常，0异常）
     */
    private Boolean status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除（0正常，1删除）
     */
    private Boolean delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}