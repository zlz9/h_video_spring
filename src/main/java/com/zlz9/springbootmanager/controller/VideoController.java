package com.zlz9.springbootmanager.controller;

import com.zlz9.springbootmanager.dto.PageParams;
import com.zlz9.springbootmanager.dto.VideoHistoryParams;
import com.zlz9.springbootmanager.service.VideoService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.PublicKey;
import java.util.List;

/**
 * <h4>springboot-manager</h4>
 * <p>视频模块</p>
 *
 * @author : zlz
 * @date : 2023-01-05 18:59
 **/
@RestController
@RequestMapping("api")
public class VideoController {
    @Autowired
    VideoService videoService;
    @GetMapping("video/recommend")
    public ResponseResult recommend(){
        return videoService.recommend();
    }
//    最新视频
    @GetMapping("video/new")
    public ResponseResult lastVideo(){
        return videoService.lastVideo();
    }
//    最热视频
    @GetMapping("video/hot")
    public ResponseResult hotVideo(){
        return videoService.hotVideo();
    }
    @GetMapping("video/funny")
    public ResponseResult funnyVideo(){
        return videoService.funnyVideo();
    }
    @GetMapping("video/game")
    public ResponseResult gameVideo(){
        return videoService.gameVideo();
    }
//    分页查询热门视频
    @GetMapping("top/hot")
    public ResponseResult videoTop(PageParams pageParams){
        return videoService.videoTop(pageParams);
    }
    @GetMapping("video/category")
    public ResponseResult VideoCategory(PageParams pageParams){
        return videoService.VideoCategory(pageParams);
    }
    @GetMapping("video/user/{id}")
    public ResponseResult getVideoListByUserId(@PathVariable Long id){
        return videoService.getVideoListByUserId(id);
    }
    @GetMapping("video/history/ids={ids}")
    public ResponseResult getHistoryVideoList(@PathVariable Long[] ids){
        return videoService.getHistoryVideoList(ids);
    }
    @GetMapping("video/search/{title}")
    public ResponseResult searchVideoByTitle(@PathVariable String title){
        return videoService.searchVideoByTitle(title);
    }
}
