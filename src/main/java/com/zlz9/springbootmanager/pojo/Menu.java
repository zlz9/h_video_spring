package com.zlz9.springbootmanager.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 菜单表
 * @TableName h_menu
 */
@TableName(value ="h_menu")
@Data
public class Menu implements Serializable {
    /**
     * 路由id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 菜单名
     */
    private String title;

    /**
     * 路由名
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 是否删除（0未删除 1已删除）
     */
    private Integer delFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 路由重定项
     */
    private String redirect;

    /**
     * 父id
     */
    private Integer parentid;

    /**
     * 是否显示
     */
    private String isShow;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}