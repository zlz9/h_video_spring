package com.zlz9.springbootmanager.controller;

import com.zlz9.springbootmanager.dto.PageById;
import com.zlz9.springbootmanager.dto.PageParams;
import com.zlz9.springbootmanager.pojo.VideoComment;
import com.zlz9.springbootmanager.service.VideoCommentService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h4>springboot-manager</h4>
 * <p>点赞控制层</p>
 *
 * @author : zlz
 * @date : 2023-01-27 19:17
 **/
@RestController
@RequestMapping("api")
public class CommentController {
    @Autowired
    VideoCommentService videoCommentService;
    @GetMapping("/comment")
    public ResponseResult getCommentById(PageById pageById){
        return videoCommentService.getCommentById(pageById);
    }
    @GetMapping("/comment/count/{id}")
    public ResponseResult getCommentCountById(@PathVariable Long id){
        return videoCommentService.getCommentCountById(id);
    }
}
