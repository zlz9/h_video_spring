package com.zlz9.springbootmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.pojo.Video;
import com.zlz9.springbootmanager.service.VideoService;
import com.zlz9.springbootmanager.mapper.VideoMapper;
import com.zlz9.springbootmanager.utils.ResponseResult;
import com.zlz9.springbootmanager.vo.SwiperVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 23340
* @description 针对表【h_video】的数据库操作Service实现
* @createDate 2023-01-01 14:20:42
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{
    @Autowired
    VideoMapper videoMapper;
    /**
     * 获取首页的轮播图
     * 获取最热门,最新的前八张图片
     * @return
     */
    @Override
    public ResponseResult hotSwiper() {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Video::getCreateTime,Video::getWeight);
         queryWrapper.last("limit 8");
        List<Video> videos = videoMapper.selectList(queryWrapper);
        List<SwiperVo> swiperVoList = copyList(videos);
        return new ResponseResult<>(200, swiperVoList);
    }

    /**
     * 获取最新的八张轮播图
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

    private List<SwiperVo> copyList(List<Video> videos) {
      List<SwiperVo> swiperVoList =   new ArrayList<SwiperVo>();
        for (Video video : videos) {
            swiperVoList.add(copy(video));
        }
        return swiperVoList;
    }

    private SwiperVo copy(Video video) {
        SwiperVo swiperVo = new SwiperVo();
        BeanUtils.copyProperties(video,swiperVo);
        return swiperVo;
    }
}




