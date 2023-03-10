package com.zlz9.springbootmanager.service;


import com.zlz9.springbootmanager.dto.UserLikeCountDTO;
import com.zlz9.springbootmanager.dto.UserLikesDTO;
import com.zlz9.springbootmanager.lang.Const;
import com.zlz9.springbootmanager.pojo.Chat;

import java.util.List;

/**
 * 负责将数据写入Redis缓存
 */
public interface RedisService {
    /**
     * 获取点赞状态
     * @param infoId
     * @param likeUserId
     */
    Integer getLikeStatus(String infoId, String likeUserId);
    /**
     * 点赞。状态为1
     * @param infoId
     * @param likeUserId
     */
    void saveLiked2Redis(String infoId, String likeUserId);

    /**
     * 取消点赞。将状态改变为0
     * @param infoId
     * @param likeUserId
     */
    void unlikeFromRedis(String infoId, String likeUserId);

    /**
     * 从Redis中删除一条点赞数据
     * @param infoId
     * @param likeUserId
     */
    void deleteLikedFromRedis(String infoId, String likeUserId);

    /**
     * 该内容的点赞数变化Δdelta
     * @param infoId
     */
    void in_decrementLikedCount(String infoId, Integer delta);

    /**
     * 获取Redis中存储的所有点赞数据
     * @return
     */
    List<UserLikesDTO> getLikedDataFromRedis();

    /**
     * 获取Redis中存储的所有点赞数量
     * @return
     */
    List<UserLikeCountDTO> getLikedCountFromRedis();

    List<Chat> getChatFromRedis2D();

    void setSearchTop(String title);

    List<String> getSearchTop();

//    删除热搜
    void delSearch();
}
