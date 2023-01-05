package com.zlz9.springbootmanager.controller;

import com.zlz9.springbootmanager.service.VideoService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h4>springboot-manager</h4>
 * <p>热门视频模块</p>
 *
 * @author : zlz
 * @date : 2023-01-02 19:26
 **/
@RequestMapping("api")
@RestController
public class SwiperController {
    @Autowired
    VideoService videoService;
//    首页轮播图模块
    @GetMapping("swiper/hot")
    public ResponseResult hotSwiper(){
        return videoService.hotSwiper();
    }
//    热门轮播图模块
    @GetMapping("swiper/new")
    public ResponseResult newSwiper(){
        return videoService.newSwiper();
    }
}
