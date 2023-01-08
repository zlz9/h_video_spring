package com.zlz9.springbootmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.dto.PageParams;
import com.zlz9.springbootmanager.pojo.Tag;
import com.zlz9.springbootmanager.pojo.Video;
import com.zlz9.springbootmanager.service.TagService;
import com.zlz9.springbootmanager.service.UserService;
import com.zlz9.springbootmanager.service.VideoService;
import com.zlz9.springbootmanager.mapper.VideoMapper;
import com.zlz9.springbootmanager.utils.ResponseResult;
import com.zlz9.springbootmanager.vo.SwiperVo;
import com.zlz9.springbootmanager.vo.VideoCategoryVo;
import com.zlz9.springbootmanager.vo.VideoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 23340
 * @description 针对表【h_video】的数据库操作Service实现
 * @createDate 2023-01-01 14:20:42
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
        implements VideoService {
    @Autowired
    VideoMapper videoMapper;
    @Autowired
    UserService userService;
    @Autowired
    TagService tagService;

    /**
     * 获取首页的轮播图
     * 获取最热门,最新的前八张图片
     *
     * @return
     */
    @Override
    public ResponseResult hotSwiper() {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Video::getCreateTime, Video::getWeight);
        queryWrapper.last("limit 8");
        List<Video> videos = videoMapper.selectList(queryWrapper);
        List<SwiperVo> swiperVoList = copyList(videos);
        return new ResponseResult<>(200, swiperVoList);
    }

    /**
     * 获取最新的八张轮播图
     *
     * @return
     */
    @Override
    public ResponseResult newSwiper() {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Video::getCreateTime);
        queryWrapper.last("limit 8");
        List<Video> videos = videoMapper.selectList(queryWrapper);
        List<SwiperVo> swiperVoList = copyList(videos);
        return new ResponseResult<>(200, swiperVoList);
    }

    /**
     * 获取推荐视频
     * 通过 顶置，权重降序排序
     *
     * @return
     */
    @Override
    public ResponseResult recommend() {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Video::getWeight, Video::getIsTop);
        List<Video> videos = videoMapper.selectList(queryWrapper);
        List<VideoVo> videoVoList = copyVideoList(videos);
        if (CollectionUtils.isEmpty(videoVoList)) {
            return new ResponseResult<>(404, "未找到");
        }
        return new ResponseResult<>(200, videoVoList);
    }


    /**
     * 获取最新视频
     *
     * @return
     */
    @Override
    public ResponseResult lastVideo() {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Video::getCreateTime);
        List<Video> videos = videoMapper.selectList(queryWrapper);
        List<VideoVo> videoVoList = copyVideoList(videos);
        if (CollectionUtils.isEmpty(videoVoList)) {
            return new ResponseResult<>(404, "未找到");
        }
        return new ResponseResult<>(200, videoVoList);
    }

    /**
     * 获取热门视频
     * 根据权重倒叙排列
     *
     * @return
     */
    @Override
    public ResponseResult hotVideo() {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Video::getWeight,Video::getIsTop);
        List<Video> videos = videoMapper.selectList(queryWrapper);
        List<VideoVo> videoVoList = copyVideoList(videos);
        if (CollectionUtils.isEmpty(videoVoList)) {
            return new ResponseResult<>(404, "未找到");
        }
        return new ResponseResult<>(200, videoVoList);
    }

    /**
     * 获取搞笑视频
     *
     * @return
     */
    @Override
    public ResponseResult funnyVideo() {
        List<Video> videos = videoMapper.selectFunnyVideo();
        List<VideoVo> videoVoList = copyVideoList(videos);
        if (CollectionUtils.isEmpty(videoVoList)) {
            return new ResponseResult<>(404, "未找到");
        }
        return new ResponseResult<>(200, videoVoList);
    }

    /**
     * 获取游戏视频
     *
     * @return
     */
    @Override
    public ResponseResult gameVideo() {
        List<Video> videos = videoMapper.selectGameVideo();
        List<VideoVo> videoVoList = copyVideoList(videos);
        if (CollectionUtils.isEmpty(videoVoList)) {
            return new ResponseResult<>(404, "未找到");
        }
        return new ResponseResult<>(200, videoVoList);
    }

    /**
     * 分页查询热门数据
     * @param pageParams
     * @return
     */
    @Override
    public ResponseResult videoTop(PageParams pageParams) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Video::getWeight,Video::getIsTop);
        Page<Video> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        Page<Video> videoPage = videoMapper.selectPage(page, queryWrapper);
        List<Video> records = videoPage.getRecords();
        List<VideoVo> videoVoList = copyVideoList(records);
        return new ResponseResult<>(200,videoVoList);
    }

    /**
     * 根据标签id查询视频
     * @param pageParams
     * @return
     */
    @Override
    public ResponseResult VideoCategory(PageParams pageParams) {
        Page<Video> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
//         根据标签id查询标签信息
       Tag tag= tagService.getTagById(pageParams.getTagId());
//       根据标签类型查找视频
        List<Video> videoList = videoMapper.selectVideoByTag(tag.getId());
        List<VideoCategoryVo> videoCategoryVoList = copyVideoByTagList(videoList);
        return new ResponseResult(200, videoCategoryVoList);
    }

    private List<VideoCategoryVo> copyVideoByTagList(List<Video> videoList) {
        List<VideoCategoryVo> videoByTagVos = new ArrayList<>();
        for (Video video : videoList) {
            videoByTagVos.add(copyVideoByTag(video));
        }
        return videoByTagVos;
    }

    private VideoCategoryVo copyVideoByTag(Video video) {
        VideoCategoryVo videoCategoryVo = new VideoCategoryVo();
        videoCategoryVo.setAuthor(userService.selectAuthorById(video.getAuthorId()));
        BeanUtils.copyProperties(video, videoCategoryVo);
        return videoCategoryVo;
    }


    private List<SwiperVo> copyList(List<Video> videos) {
        List<SwiperVo> swiperVoList = new ArrayList<SwiperVo>();
        for (Video video : videos) {
            swiperVoList.add(copy(video));
        }
        return swiperVoList;
    }

    private SwiperVo copy(Video video) {
        SwiperVo swiperVo = new SwiperVo();
        BeanUtils.copyProperties(video, swiperVo);
        return swiperVo;
    }

    private List<VideoVo> copyVideoList(List<Video> videoList) {
        List<VideoVo> videoVoList = new ArrayList<>();
        for (Video video : videoList) {
            videoVoList.add(copyVideo(video));
        }
        return videoVoList;
    }

    private VideoVo copyVideo(Video video) {
        VideoVo videoVo = new VideoVo();
        BeanUtils.copyProperties(video, videoVo);
        videoVo.setAuthor(userService.selectAuthorById(video.getAuthorId()));
        return videoVo;
    }
}




