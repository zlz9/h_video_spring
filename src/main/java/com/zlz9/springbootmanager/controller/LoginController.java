package com.zlz9.springbootmanager.controller;

import com.zlz9.springbootmanager.dto.LoginParams;
import com.zlz9.springbootmanager.dto.RegisterParams;
import com.zlz9.springbootmanager.service.UserService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <h4>springboot-manager</h4>
 * <p>登录模块</p>
 *
 * @author : zlz
 * @date : 2022-12-30 21:28
 **/
@Api(tags = "登录模块")
@RestController
@RequestMapping("api")
public class LoginController {
    @Autowired
    UserService userService;
    @PostMapping("login")
    public ResponseResult login(LoginParams loginParams){
        return userService.login(loginParams);
    }
    @GetMapping("logout")
    public ResponseResult logout(){
        return userService.logout();
    }
    @PostMapping("register")
    public ResponseResult register(@RequestBody @Valid RegisterParams registerParams){
        return userService.register(registerParams);
    }

}
