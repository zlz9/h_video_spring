package com.zlz9.springbootmanager.dto;

import lombok.Data;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-02-05 12:28
 **/
@Data
public class UserDTO {
    /**
     * 密码
     */
    private String oldPwd;
   private  String  newPwd;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String avatar;



    private String selfIntroduction;
}
