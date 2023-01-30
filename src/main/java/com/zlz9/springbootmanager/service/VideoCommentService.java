package com.zlz9.springbootmanager.service;

import com.zlz9.springbootmanager.dto.CommentParams;
import com.zlz9.springbootmanager.dto.PageById;
import com.zlz9.springbootmanager.pojo.VideoComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlz9.springbootmanager.utils.ResponseResult;

/**
* @author 23340
* @description 针对表【h_video_comment】的数据库操作Service
* @createDate 2023-01-14 17:24:56
*/
public interface VideoCommentService extends IService<VideoComment> {

    ResponseResult getCommentById(PageById pageById);

    ResponseResult getCommentCountById(Long id);

    ResponseResult publishComment(CommentParams commentParams);
}
