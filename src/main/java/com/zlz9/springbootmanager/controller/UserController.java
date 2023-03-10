package com.zlz9.springbootmanager.controller;

import com.zlz9.springbootmanager.dto.UserDTO;
import com.zlz9.springbootmanager.service.UserService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <h4>springboot-manager</h4>
 * <p>用户模块</p>
 *
 * @author : zlz
 * @date : 2023-01-15 16:27
 **/
@RestController
@RequestMapping("api")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("user/{id}")
    public ResponseResult getAuthorInfoById(@PathVariable Long id){
        return userService.getAuthorInfoById(id);
    }
    @GetMapping("userinfo/current")
    public ResponseResult getCurrentUser(){
        return userService.getCurrentUser();
    }
    @PostMapping("user/update")
    public ResponseResult updateUserInfo(@RequestBody UserDTO userDTO){
        return userService.updateUserInfo(userDTO);
    }

}
