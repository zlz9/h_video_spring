package com.zlz9.springbootmanager.controller;

import com.zlz9.springbootmanager.service.TagService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h4>springboot-manager</h4>
 * <p>视频标签模块</p>
 *
 * @author : zlz
 * @date : 2023-01-05 15:33
 **/
@RestController
@RequestMapping("api")
public class TagController {
    @Autowired
    TagService tagService;
    @GetMapping("tag/all")
    public ResponseResult getTag(){
        return tagService.getTag();
    }
    @GetMapping("tag/{id}")
    public  ResponseResult getVideoByTag(@PathVariable Long id){
        return tagService.getVideoByTag(id);
    }
}
