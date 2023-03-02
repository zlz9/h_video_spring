package com.zlz9.springbootmanager.vo;

import lombok.Data;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-02-23 11:30
 **/
@Data
public class UserInfoVo {
    private Long id;

    /**
     * 用户名
     */
    private String userName;


    /**
     * 昵称
     */
    private String nickName;


    /**
     * 邮箱
     */
    private String email;

    /**
     * 注册时间
     */
    private Long createTime;


    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态（1正常，0不正常锁定）
     */
    private Boolean status;
    /**
     * 座右铭
     */
    private String selfIntroduction;


}
