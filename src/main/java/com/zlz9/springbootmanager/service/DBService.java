package com.zlz9.springbootmanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlz9.springbootmanager.pojo.Like;

/**
 * 负责将Redis缓存中的数据持久化到数据库中
 */
public interface DBService {
    /**
     * 保存点赞记录
     * @param
     * @return
     */
    Boolean save(Like like);
    /**
     * 更新点赞记录
     * @param
     * @return
     */
    Boolean update(Like like);
    /**
     * 根据内容的id查询点赞列表（即查询都谁给这个内容点赞过）
     * @param infoId 内容的id
     * @return
     */
    Page<Like> getLikedListByInfoId(String infoId, int pageNum, int pageSize);

    /**
     * 根据点赞人的id查询点赞列表（即查询这个人都给哪些内容点赞过）
     * @param likeUserId
     * @return
     */
    Page<Like> getLikedListByLikeUserId(String likeUserId, int pageNum, int pageSize);

    /**
     * 通过被点赞内容和点赞人id查询是否存在点赞记录
     * @param infoId
     * @param likeUserId
     * @return
     */
    Like getByInfoIdAndLikeUserId(String infoId, String likeUserId);

    /**
     * 将Redis里的点赞数据存入数据库中,True 表示还需要进一步持久化， False表示数据库中已存在该数据，无需进一步持久化
     */
    void transLikedFromRedis2DB();

    /**
     * 将Redis中的点赞数量数据存入数据库
     */
    void transLikedCountFromRedis2DB();
/**
 * 将redis的评论存入数据库中
 */
    void transChatFromRedis2DB();
}
