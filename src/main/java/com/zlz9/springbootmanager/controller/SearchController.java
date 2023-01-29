package com.zlz9.springbootmanager.controller;

import com.zlz9.springbootmanager.service.VideoService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h4>springboot-manager</h4>
 * <p>搜索模块</p>
 *
 * @author : zlz
 * @date : 2023-01-29 17:03
 **/
@RestController
@RequestMapping("api")
public class SearchController {
    @Autowired
    VideoService videoService;
    @GetMapping("search/top")
    public ResponseResult getSearchTop(){
        return videoService.getSearchTop();
    }
}
