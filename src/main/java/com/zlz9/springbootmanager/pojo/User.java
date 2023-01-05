package com.zlz9.springbootmanager.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName h_user
 */
@TableName(value ="h_user")
@Data
public class User implements Serializable {
    /**
     * 用户 id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别（0男，1女）
     */
    private Boolean sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 注册时间
     */
    private Long createTime;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态（1正常，0不正常锁定）
     */
    private Boolean status;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 删除（0正常，1删除）
     */
    private Boolean delFlag;

    /**
     * 座右铭
     */
    private String selfIntroduction;

    /**
     * 出生日期
     */
    private Long birthday;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}