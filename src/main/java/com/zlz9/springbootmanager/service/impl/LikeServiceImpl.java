package com.zlz9.springbootmanager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.lang.CONSTANT;
import com.zlz9.springbootmanager.pojo.Like;
import com.zlz9.springbootmanager.service.LikeService;
import com.zlz9.springbootmanager.mapper.LikeMapper;
import com.zlz9.springbootmanager.service.RedisService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 23340
* @description 针对表【h_like】的数据库操作Service实现
* @createDate 2023-01-14 17:34:53
*/
@Service
@Transactional
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like>
    implements LikeService{

    @Autowired
    private RedisService redisService;
    @Override
    public ResponseResult<Object> likeInfo(String infoId, String userId) {
        // 查询Redis是否已经存储为喜欢
        Integer status = redisService.getLikeStatus(infoId, userId);
        if (status == CONSTANT.LikedStatusEum.LIKE.getCode()){// 已经存在喜欢
//            return CommonResponse.createForSuccess("已经点赞该内容啦，请勿重复点赞！");
            return new ResponseResult<>(505, "已经点赞该内容啦，请勿重复点赞！");
        }
        // 不存在或者之前是取消喜欢
        try {
            redisService.saveLiked2Redis(infoId, userId);
            redisService.in_decrementLikedCount(infoId,1);
//            return CommonResponse.createForSuccess("喜欢内容写入redis缓存成功");
            return new ResponseResult<>(200, "喜欢内容写入redis缓存成功");
        }catch (Exception e){
            e.printStackTrace();
//            return CommonResponse.createForError("喜欢内容写入redis缓存失败，请稍后重试！");
            return new ResponseResult<>(505, "喜欢内容写入redis缓存失败，请稍后重试！");
        }
    }

    @Override
    public ResponseResult<Object> dislikeInfo(String infoId, String userId) {
        // 查询Redis是否已经存储为取消喜欢
        Integer status = redisService.getLikeStatus(infoId, userId);
        if (status == CONSTANT.LikedStatusEum.UNLIKE.getCode()){// 已经存在取消喜欢
//            return CommonResponse.createForSuccess("已经取消点赞该内容啦，请勿重复取消点赞！");
            return new ResponseResult<>(505, "喜欢内容写入redis缓存失败，请稍后重试！");
        }
        else if (status == CONSTANT.LikedStatusEum.NOT_EXIST.getCode()) {// 不存在取消喜欢，修改状态，增加0条
            redisService.unlikeFromRedis(infoId, userId);
            redisService.in_decrementLikedCount(infoId,0);
//            return CommonResponse.createForSuccess("取消喜欢内容写入redis缓存成功");
            return new ResponseResult<>(200, "取消喜欢内容写入redis缓存成功\"");
        }
        else{// 之前已经喜欢，则修改状态,并喜欢数-1
            try {
                redisService.unlikeFromRedis(infoId, userId);
                redisService.in_decrementLikedCount(infoId,-1);
//                return CommonResponse.createForSuccess("取消喜欢内容写入redis缓存成功");
                return new ResponseResult<>(200, "取消喜欢内容写入redis缓存成功");
            }catch (Exception e){
                e.printStackTrace();
//                return CommonResponse.createForError("取消喜欢内容写入redis缓存失败，请稍后重试！");
                return new ResponseResult<>(505, "取消喜欢内容写入redis缓存失败，请稍后重试！");
            }
        }
    }
}




