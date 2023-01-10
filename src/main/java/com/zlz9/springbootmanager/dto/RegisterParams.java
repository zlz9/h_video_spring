package com.zlz9.springbootmanager.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * <h4>springboot-manager</h4>
 * <p>用户注册参数</p>
 *
 * @author : zlz
 * @date : 2023-01-10 14:42
 **/
@Data
public class RegisterParams {
//    @NotBlank(message = "⽤户名不能为空!")
    private String userName;
//    @NotBlank(message = "密码不能为空!")
    private String Password;
//    @Email
    private String email;
    //    短信验证
//    @Length(min = 4, max = 6, message = "验证码4-6位")
    private String code;
}
