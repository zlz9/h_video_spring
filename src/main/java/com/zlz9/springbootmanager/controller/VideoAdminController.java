package com.zlz9.springbootmanager.controller;

import com.zlz9.springbootmanager.service.VideoService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h4>springboot-manager</h4>
 * <p>管理短视频控制层</p>
 *
 * @author : zlz
 * @date : 2023-01-11 17:42
 **/
@RestController
@RequestMapping("api/admin")
public class VideoAdminController {
    @Autowired
    VideoService videoService;
    @PostMapping("delete/{id}")
    public ResponseResult delVideoById(@PathVariable Long id){
        return videoService.delVideoById(id);
    }
}
