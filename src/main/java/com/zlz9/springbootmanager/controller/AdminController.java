package com.zlz9.springbootmanager.controller;

import com.zlz9.springbootmanager.dto.PageParams;
import com.zlz9.springbootmanager.dto.TagDTO;
import com.zlz9.springbootmanager.service.TagService;
import com.zlz9.springbootmanager.service.UserService;
import com.zlz9.springbootmanager.service.VideoService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <h4>springboot-manager</h4>
 * <p>管理模块</p>
 *
 * @author : zlz
 * @date : 2023-02-23 10:00
 **/
@RestController
@RequestMapping("api")
public class AdminController {
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;
    @Autowired
    private VideoService videoService;
    @GetMapping("all/user")
    public ResponseResult getAllUser(){
        return userService.getAllUser();
    }
    @GetMapping("all/video")
    public ResponseResult getAllVideo(PageParams pageParams){
        return videoService.getAllVideo(pageParams);
    }
    @GetMapping("admin/del/video/{id}")
    public ResponseResult delVideo(@PathVariable Long id){
        return videoService.delVideo(id);
    }
    @GetMapping("disable/user/{id}")
    public ResponseResult disableById(@PathVariable Long id){
        return userService.disableById(id);
    }
    @GetMapping("enable/user/{id}")
    public ResponseResult enableUserById(@PathVariable Long id){
        return userService.enableUserById(id);
    }
    @GetMapping("del/tag/{id}")
    public ResponseResult delTagById(@PathVariable Long id){
        return tagService.delTagById(id);
    }
    @PostMapping("update/tag")
    public ResponseResult uploadTag(@RequestBody TagDTO tagDTO){
        return tagService.uploadTag(tagDTO);
    }
    @GetMapping("reset/password/{id}")
    public ResponseResult resetPassword(@PathVariable Long id){
        return userService.resetPassword(id);
    }

}
