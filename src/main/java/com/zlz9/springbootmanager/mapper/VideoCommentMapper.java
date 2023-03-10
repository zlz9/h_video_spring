package com.zlz9.springbootmanager.mapper;

import com.zlz9.springbootmanager.pojo.VideoComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 23340
* @description 针对表【h_video_comment】的数据库操作Mapper
* @createDate 2023-01-14 17:24:56
* @Entity com.zlz9.springbootmanager.pojo.VideoComment
*/
@Mapper
public interface VideoCommentMapper extends BaseMapper<VideoComment> {

}




