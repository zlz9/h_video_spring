package com.zlz9.springbootmanager.service.impl;

import com.zlz9.springbootmanager.dto.UserLikeCountDTO;
import com.zlz9.springbootmanager.dto.UserLikesDTO;
import com.zlz9.springbootmanager.lang.CONSTANT;
import com.zlz9.springbootmanager.lang.Const;
import com.zlz9.springbootmanager.pojo.Chat;
import com.zlz9.springbootmanager.service.RedisService;
import com.zlz9.springbootmanager.utils.LocalDateTimeConvertUtil;
import com.zlz9.springbootmanager.utils.RedisCache;
import com.zlz9.springbootmanager.utils.RedisKeyUtils;
import com.zlz9.springbootmanager.vo.ChatVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("redisService")
@Slf4j
@Transactional
public class RedisServiceImpl implements RedisService {

//    @Autowired 不行
    @Resource
    private HashOperations<String, String, Object> redisHash;// Redis Hash
    @Autowired
    RedisCache redisCache;
    @Override
    public Integer getLikeStatus(String infoId, String likeUserId) {
        if (redisHash.hasKey(RedisKeyUtils.MAP_KEY_USER_LIKED, RedisKeyUtils.getLikedKey(infoId, likeUserId))){
            HashMap<String, Object> map = (HashMap<String, Object>) redisHash.get(RedisKeyUtils.MAP_KEY_USER_LIKED, RedisKeyUtils.getLikedKey(infoId, likeUserId));
            return (Integer) map.get("status");
        }
        return CONSTANT.LikedStatusEum.NOT_EXIST.getCode();
    }

    @Override
    public void saveLiked2Redis(String infoId, String likeUserId) {
        // 生成key
        String key = RedisKeyUtils.getLikedKey(infoId, likeUserId);
        // 封装value 喜欢状态 更新时间
        HashMap<String,Object> map = new HashMap<>();
        map.put("status",CONSTANT.LikedStatusEum.LIKE.getCode());
        map.put("updateTime", System.currentTimeMillis());

        redisHash.put(RedisKeyUtils.MAP_KEY_USER_LIKED, key, map);
    }

    @Override
    public void unlikeFromRedis(String infoId, String likeUserId) {
        // 生成key
        String key = RedisKeyUtils.getLikedKey(infoId, likeUserId);
        // 封装value 喜欢状态 更新时间
        HashMap<String,Object> map = new HashMap<>();
        map.put("status", CONSTANT.LikedStatusEum.UNLIKE.getCode());
        map.put("updateTime", System.currentTimeMillis());// 存入当前时间戳

        redisHash.put(RedisKeyUtils.MAP_KEY_USER_LIKED, key, map);
    }

    @Override
    public void deleteLikedFromRedis(String infoId, String likeUserId) {
        String key = RedisKeyUtils.getLikedKey(infoId, likeUserId);
        redisHash.delete(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
    }

    @Override
    public void in_decrementLikedCount(String infoId, Integer delta) {
        redisHash.increment(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, infoId, delta);
    }

    @Override
    public List<UserLikesDTO> getLikedDataFromRedis() {
        // scan 读取数据，比key匹配优雅
        Cursor<Map.Entry<String, Object>> cursor = redisHash.scan(RedisKeyUtils.MAP_KEY_USER_LIKED, ScanOptions.NONE);

        List<UserLikesDTO> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<String, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            //分离出 infoId，likeUserId, 解析value
            String[] split = key.split("::");
            String infoId = split[0];
            String likeUserId = split[1];
            HashMap<String, Object> map = (HashMap<String, Object>) entry.getValue();
            Integer status = (Integer) map.get("status");
            long updateTimeStamp = (long) map.get("updateTime");
            LocalDateTime updateTime = LocalDateTimeConvertUtil.getDateTimeOfTimestamp(updateTimeStamp);// 时间戳转为LocalDateTime

            //组装成 UserLike 对象
            UserLikesDTO userLikesDTO = new UserLikesDTO(infoId, likeUserId, status, updateTime);
            list.add(userLikesDTO);

            //存到 list 后从 Redis 中清理缓存
            redisHash.delete(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
        }
        return list;
    }

    @Override
    public List<UserLikeCountDTO> getLikedCountFromRedis() {
        // scan 读取数据，比key匹配优雅
        Cursor<Map.Entry<String, Object>> cursor = redisHash.scan(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
        List<UserLikeCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<String, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String)map.getKey();
            UserLikeCountDTO dto = new UserLikeCountDTO(key, (Integer) map.getValue());
            list.add(dto);
            //从Redis中删除这条记录
            redisHash.delete(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key);
        }
        return list;
    }

    @Override
    public List<Chat> getChatFromRedis2D() {
        List<Chat> chat_list = redisCache.getCacheList("CHAT_LIST");
        redisCache.deleteObject("CHAT_LIST");
        return chat_list;
    }

    @Override
    public void setSearchTop(String title) {
        List<String> searchList = new ArrayList<>();
        searchList.add(title);
        redisCache.setCacheList(Const.SEARCHKEY, searchList);
        searchList.clear();
    }

    /**
     * 获取搜素榜单
     * @return
     */
    @Override
    public List<String> getSearchTop() {
        List<String> seachList = redisCache.getCacheList(Const.SEARCHKEY);
        return seachList;
    }

    /**
     * 删除热搜的key
     */
    @Override
    public void delSearch() {
        redisCache.deleteObject(Const.SEARCHKEY);
    }

}
