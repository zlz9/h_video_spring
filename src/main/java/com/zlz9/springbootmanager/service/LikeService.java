package com.zlz9.springbootmanager.service;

import com.zlz9.springbootmanager.pojo.Like;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlz9.springbootmanager.utils.ResponseResult;

/**
* @author 23340
* @description 针对表【h_like】的数据库操作Service
* @createDate 2023-01-14 17:34:53
*/
public interface LikeService extends IService<Like> {
    /**
     * 喜欢数据到缓存
     */
    ResponseResult<Object> likeInfo(String infoId, String userId);

    /**
     * 取消喜欢数据到缓存
     */
    ResponseResult<Object> dislikeInfo(String infoId,String userId);
}
