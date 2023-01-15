package com.zlz9.springbootmanager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.pojo.VideoComment;
import com.zlz9.springbootmanager.service.VideoCommentService;
import com.zlz9.springbootmanager.mapper.VideoCommentMapper;
import org.springframework.stereotype.Service;

/**
* @author 23340
* @description 针对表【h_video_comment】的数据库操作Service实现
* @createDate 2023-01-14 17:24:56
*/
@Service
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment>
    implements VideoCommentService{

}




