package com.zlz9.springbootmanager.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.config.AliYunConfig;
import com.zlz9.springbootmanager.controller.UploadController;
import com.zlz9.springbootmanager.dto.PageParams;
import com.zlz9.springbootmanager.dto.PublishVideoParams;
import com.zlz9.springbootmanager.dto.VideoHistoryParams;
import com.zlz9.springbootmanager.mapper.UserMapper;
import com.zlz9.springbootmanager.mapper.VideoMapper;
import com.zlz9.springbootmanager.mapper.VideoTagMapper;
import com.zlz9.springbootmanager.pojo.LoginUser;
import com.zlz9.springbootmanager.pojo.Tag;
import com.zlz9.springbootmanager.pojo.Video;
import com.zlz9.springbootmanager.pojo.VideoTag;
import com.zlz9.springbootmanager.service.*;
import com.zlz9.springbootmanager.utils.RedisCache;
import com.zlz9.springbootmanager.utils.ResponseResult;
import com.zlz9.springbootmanager.vo.SwiperVo;
import com.zlz9.springbootmanager.vo.VideoCategoryVo;
import com.zlz9.springbootmanager.vo.VideoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author 23340
 * @description 针对表【h_video】的数据库操作Service实现
 * @createDate 2023-01-01 14:20:42
 */
@Transactional
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
        implements VideoService {
    @Autowired
    VideoMapper videoMapper;
    @Autowired
    UserService userService;
    @Autowired
    TagService tagService;
    @Autowired
    RedisService redisService;
    @Autowired
    VideoTagMapper videoTagMapper;
    @Autowired
    VideoTagService videoTagService;
    @Autowired FileUploadService fileUploadService;


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

    /**
     * 根据id删除视频
     * @return
     */
    @Override
    public ResponseResult delVideoById(Long id) {
       LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = loginUser.getUser().getId();
//        先查再删
        Video video = videoMapper.selectById(id);
        if (video.getId() == null) {
            return new ResponseResult<>(404,"资源不存在");
        }
        if (!Objects.equals(userId, video.getAuthorId())) {
            return new ResponseResult<>(505,"权限不够");
        }
        // 删除oss对象上的资源
        fileUploadService.delete(video.getUrl());
        videoMapper.deleteById(id);
        return new ResponseResult<>(200,"操作成功");
    }

    @Override
    public ResponseResult getVideoListByUserId(Long id) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Video::getAuthorId, id);
        List<Video> videoList = videoMapper.selectList(queryWrapper);
        List<VideoVo> videoVoList = copyVideoList(videoList);
        return new ResponseResult(200, videoVoList);
    }

    /**
     * 根据id查询视频列表
     * @param
     * @return
     */
    @Override
    public ResponseResult getHistoryVideoList(Long[] ids) {
        if (ArrayUtil.isEmpty(ids)) {
           return new ResponseResult<>(400,"未找到历史记录");
        }
        List<Video> videoList = videoMapper.selectBatchIds(Arrays.asList(ids));
        List<VideoVo> videoVoList = copyVideoList(videoList);
        return new ResponseResult(200, videoVoList);
    }

    /**
     * 根据标题模糊查询视频
     * @param title
     * @return
     */
    @Override
    public ResponseResult searchVideoByTitle(String title) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Video::getName, title);
        List<Video> videoList = videoMapper.selectList(queryWrapper);
        if (videoList.size()==0) {
            return new ResponseResult(400, "未找到相关视频");
        }
//        找到之后将title存到redis中
        redisService.setSearchTop(title);
        List<VideoVo> videoVoList = copyVideoList(videoList);
        return new ResponseResult<>(200,videoVoList);
    }

    /**
     * 获取搜索榜单
     * @return
     */
    @Override
    public ResponseResult getSearchTop() {
       List<String> searchList =  redisService.getSearchTop();
        return new ResponseResult(200, searchList);
    }

    /**
     * 发不视频
     * @param publishVideoParams
     * @return
     */
    @Override
    public ResponseResult publishVideo(PublishVideoParams publishVideoParams) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long id = loginUser.getUser().getId();
        Video video = new Video();
        video.setCover(publishVideoParams.getUrl()+"?x-oss-process=video/snapshot,t_1000,m_fast");
        video.setAuthorId(id);
        video.setCreateTime(System.currentTimeMillis());
        video.setIsTop(publishVideoParams.getIsTop());
        video.setSelfIntroduction(publishVideoParams.getSelfIntroduction());
        video.setName(publishVideoParams.getName());
        video.setUrl(publishVideoParams.getUrl());
        video.setWeight(0);
        videoMapper.insert(video);
//        插入后获取视频id，然后做分类


        for (Long tagId : publishVideoParams.getTagIds()) {
            VideoTag videoTag = new VideoTag();
            videoTag.setTagId(tagId);
            videoTag.setVideoId(video.getId());
            videoTagMapper.insert(videoTag);
        }

        return new ResponseResult<>(200,"发布成功");
    }

    /**
     * 根据id查询视频
     * @param id
     * @return
     */
    @Override
    public ResponseResult getVideoById(Long id) {
        Video video = videoMapper.selectById(id);
        if (video == null) {
            return new ResponseResult(404, "未找到");
        }
        VideoVo videoVo = new VideoVo();
        video.setWeight(video.getWeight()+1);
        BeanUtils.copyProperties(video, videoVo);
        videoVo.setAuthor(userService.selectAuthorById(video.getAuthorId()));
        if (video== null) {
            return new ResponseResult<>(400,"未找到");
        }
        return new ResponseResult(200, videoVo);
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @Override
    public ResponseResult delVideo(Long id) {
        Video video = videoMapper.selectById(id);
        videoMapper.deleteById(id);
//        远程oss删除
        UploadController uploadController = new UploadController();
        uploadController.deleteFile(video.getUrl());
        return new ResponseResult<>(200,"删除成功");
    }

    /**
     * 根据id查询相似视频
     * @param id
     * @return
     */

    @Override
    public ResponseResult getVideoSimilarById(Long id) {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        Video video = videoMapper.selectById(id);
        if (video == null) {
            return new ResponseResult(404, "未找到");
        }
        //根据videoId查找标签id
       Long tagId= videoTagMapper.selectTagByVideoTd(id);
        List<Long> ids= videoTagMapper.selectVideoListById(tagId);
        List<Video> videoList = videoMapper.selectBatchIds(ids);
        List<VideoVo> videoVoList = copyVideoList(videoList);
        return new ResponseResult<>(200,videoVoList);
    }

    @Override
    public ResponseResult getAllVideo(PageParams pageParams) {
        Page<Video> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        Page<Video> videoPage = videoMapper.selectPage(page, null);
        List<Video> records = videoPage.getRecords();
        HashMap<String, Object> map = new HashMap<>();
        List<VideoVo> videoVoList = copyVideoList(records);
        map.put("videoList",videoVoList);
        return new ResponseResult<>(200,map);
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
        videoVo.setTags(tagService.getTagsById(video.getId()));
        return videoVo;
    }
}




