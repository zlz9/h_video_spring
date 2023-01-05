package com.zlz9.springbootmanager.dto;

import lombok.Data;

/**
 * <h4>springboot-manager</h4>
 * <p>登录参数</p>
 *
 * @author : zlz
 * @date : 2022-12-30 21:33
 **/
@Data
public class LoginParams {
    private String userName;
    private String password;
    private String code;
}
