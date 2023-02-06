package com.zlz9.springbootmanager.service;

import com.zlz9.springbootmanager.dto.PageParams;
import com.zlz9.springbootmanager.dto.PublishVideoParams;
import com.zlz9.springbootmanager.dto.VideoHistoryParams;
import com.zlz9.springbootmanager.pojo.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlz9.springbootmanager.utils.ResponseResult;

import java.util.List;

/**
* @author 23340
* @description 针对表【h_video】的数据库操作Service
* @createDate 2023-01-01 14:20:42
*/
public interface VideoService extends IService<Video> {

    ResponseResult hotSwiper();

    ResponseResult newSwiper();

    ResponseResult recommend();

    ResponseResult lastVideo();

    ResponseResult hotVideo();

    ResponseResult funnyVideo();

    ResponseResult gameVideo();

    ResponseResult videoTop(PageParams pageParams);

    ResponseResult VideoCategory(PageParams pageParams);

    ResponseResult delVideoById(Long id);

    ResponseResult getVideoListByUserId(Long id);

    ResponseResult getHistoryVideoList(Long[] ids);

    ResponseResult searchVideoByTitle(String title);

    ResponseResult getSearchTop();

    ResponseResult publishVideo(PublishVideoParams publishVideoParams);

    ResponseResult getVideoById(Long id);
}
