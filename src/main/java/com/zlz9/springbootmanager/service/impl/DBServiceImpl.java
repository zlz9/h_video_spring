package com.zlz9.springbootmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zlz9.springbootmanager.dto.UserLikeCountDTO;
import com.zlz9.springbootmanager.dto.UserLikesDTO;
import com.zlz9.springbootmanager.lang.CONSTANT;
import com.zlz9.springbootmanager.mapper.ChatMapper;
import com.zlz9.springbootmanager.mapper.LikeMapper;
import com.zlz9.springbootmanager.mapper.VideoCommentMapper;
import com.zlz9.springbootmanager.pojo.Chat;
import com.zlz9.springbootmanager.pojo.Like;
import com.zlz9.springbootmanager.pojo.VideoComment;
import com.zlz9.springbootmanager.service.DBService;
import com.zlz9.springbootmanager.service.RedisService;
import com.zlz9.springbootmanager.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-01-14 19:12
 **/
@Slf4j
@Service
@Transactional
public class DBServiceImpl implements DBService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private VideoCommentMapper videoCommentMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private ChatMapper chatMapper;
//UserLikes 转 Like
@Override
public Boolean save(Like userLike) {
    int rows = likeMapper.insert(userLike);
    return rows > 0;
}

    @Override
    public Boolean update(Like userLike) {
        UpdateWrapper<Like> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", userLike.getStatus());
        updateWrapper.set("update_time",userLike.getUpdateTime());
        updateWrapper.eq("id",userLike.getId());

        int rows = likeMapper.update(userLike,updateWrapper);
        return rows > 0;
    }

    @Override
    public Page<Like> getLikedListByInfoId(String infoId, int pageNum, int pageSize) {
        // 分页获取喜欢列表信息
        Page<Like> result = new Page<>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        // 获取内容的id查询点赞列表
        QueryWrapper<Like> queryWrapper = new QueryWrapper();
        queryWrapper.eq("info_id",infoId);
        result = likeMapper.selectPage(result, queryWrapper);
        log.info("获得内容的id查询点赞列表（即查询都有谁给这个内容点赞过）");
        return result;
    }

    @Override
    public Page<Like> getLikedListByLikeUserId (String likeUserId, int pageNum, int pageSize) {
        // 分页获取喜欢列表信息
        Page<Like> result = new Page<>();
        result.setCurrent(pageNum);
        result.setSize(pageSize);

        // 获取用户的id查询点赞列表
        QueryWrapper<Like> queryWrapper = new QueryWrapper();
        queryWrapper.eq("like_user_id", likeUserId);
        result = likeMapper.selectPage(result, queryWrapper);
        log.info("获取点赞人的id查询点赞列表（即查询这个人都给哪些内容点赞过）");
        return result;
    }

    @Override
    public Like getByInfoIdAndLikeUserId (String infoId, String likeUserId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("info_id",infoId);
        map.put("like_user_id",likeUserId);
        try{
            Like like = likeMapper.selectByMap(map).get(0);
            log.info("通过被点赞人和点赞人id查询是否存在点赞记录");
            return like;
        }catch (Exception e){
            log.info("当前查询的被点赞人和点赞人id不存在点赞记录");
            return null;
        }
    }

    @Override
    public void transLikedFromRedis2DB() {
        // 批量获取缓存中的点赞数据
        List<UserLikesDTO> list = redisService.getLikedDataFromRedis();
        if (CollectionUtils.isEmpty(list))// 为空，不写入
        {
            return;
        }
        for (UserLikesDTO item: list){
            Like like = getByInfoIdAndLikeUserId(item.getInfoId(), item.getLikeUserId());// 在数据库中查询
            if (like == null) {// 无记录，新增
                if(!save(userLikesDTOtoUserLikes(item))){
                    log.info("新增点赞数据失败！");
                    return;

                }
            }else {// 有记录，更新
                // 判断数据库中点赞状态与缓存中点赞状态一致性
                if (like.getStatus().equals(item.getStatus())){// 一致，无需持久化，点赞数量-1
                    redisService.in_decrementLikedCount(item.getInfoId(), -1);
                }else{// 不一致
                    if (like.getStatus()== CONSTANT.LikedStatusEum.LIKE.getCode()){// 在数据库中已经是点赞，则取消点赞，同时记得redis中的count-1
                        // 之前是点赞，现在改为取消点赞 1.设置更改status 2. redis中的count要-1（消除在数据库中自己的记录）
                        like.setStatus(CONSTANT.LikedStatusEum.UNLIKE.getCode());
                        redisService.in_decrementLikedCount(item.getInfoId(), -1);
                    } else if (like.getStatus()== CONSTANT.LikedStatusEum.UNLIKE.getCode()) {// 未点赞，则点赞，修改点赞状态和点赞数据+1
                        like.setStatus(CONSTANT.LikedStatusEum.LIKE.getCode());
                        redisService.in_decrementLikedCount(item.getInfoId(), 1);
                    }
                    like.setUpdateTime(System.currentTimeMillis());
                    if(!update(like)){// 更新点赞数据
                        log.info("更新点赞数据失败！");
                        return;
                        // System.out.println("缓存记录更新数据库失败！请重试");
                    }
                }
            }
        }
    }

    @Override
    public void transLikedCountFromRedis2DB() {
// 获取缓存中内容的点赞数列表
        List<UserLikeCountDTO> list = redisService.getLikedCountFromRedis();
        if (CollectionUtils.isEmpty(list))// 为空，不写入
        {
            return;
        }
        for (UserLikeCountDTO item: list){
            VideoComment videoComment = videoCommentMapper.selectById(item.getInfoId());
            if (videoComment != null) {// 新增点赞数
                Integer likeCount = videoComment.getLikeCount() + item.getLikeCount();
                log.info("内容id不为空，更新内容点赞数量");
                log.info("likeCount的数量==>",likeCount);
                videoComment.setLikeCount(likeCount);

                UpdateWrapper<VideoComment> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("like_count", videoComment.getLikeCount());
                updateWrapper.eq("id", videoComment.getId());
                int rows = videoCommentMapper.update(videoComment, updateWrapper);
                if (rows > 0) {
                    System.out.println("成功更新内容点赞数！");
                    return;
                }
            }
            log.info("内容id不存在，无法将缓存数据持久化！");
        }
    }

    @Override
    public void transChatFromRedis2DB() {
     List<Chat> chatList= redisService.getChatFromRedis2D();
        if (CollectionUtils.isEmpty(chatList)) {
            log.info("聊天列表为空");
            return;
        }
        for (Chat chat : chatList) {
            chatMapper.insert(chat);
            log.info("新增聊天数:{}",chatList.size());

        }
    }

    private Like userLikesDTOtoUserLikes(UserLikesDTO userLikesDTO){
        Like like = new Like();
//        userLikes.setId(sid.nextShort());
        BeanUtils.copyProperties(userLikesDTO,like);
        like.setCreateTime(System.currentTimeMillis());
        like.setUpdateTime(System.currentTimeMillis());
        return like;
    }
}
