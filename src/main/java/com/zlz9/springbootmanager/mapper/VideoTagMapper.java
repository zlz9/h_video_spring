package com.zlz9.springbootmanager.mapper;

import com.zlz9.springbootmanager.pojo.VideoTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 23340
* @description 针对表【h_video_tag】的数据库操作Mapper
* @createDate 2023-01-01 14:20:42
* @Entity com.zlz9.springbootmanager.pojo.VideoTag
*/
@Mapper
public interface VideoTagMapper extends BaseMapper<VideoTag> {
    List<Long> selectVideoListById(Long tagId);

    Long selectTagByVideoTd(Long id);
}




