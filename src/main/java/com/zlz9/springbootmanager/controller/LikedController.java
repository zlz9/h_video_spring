package com.zlz9.springbootmanager.controller;

import com.zlz9.springbootmanager.service.LikeService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-01-14 17:41
 **/
@RestController
@RequestMapping("api")
public class LikedController {
    @Autowired
    private LikeService likeService;

    @PostMapping("comment/like")
    public ResponseResult<Object> likeInfo(
            String infoId,
            String userId) {
        return likeService.likeInfo(infoId, userId);
    }

    @PostMapping("comment/dislike")
    public ResponseResult<Object> dislikeInfo(
            String infoId,
            String userId) {
        return likeService.dislikeInfo(infoId, userId);
    }
}