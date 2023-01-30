package com.zlz9.springbootmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.dto.CommentParams;
import com.zlz9.springbootmanager.dto.PageById;
import com.zlz9.springbootmanager.pojo.LoginUser;
import com.zlz9.springbootmanager.pojo.User;
import com.zlz9.springbootmanager.pojo.Video;
import com.zlz9.springbootmanager.pojo.VideoComment;
import com.zlz9.springbootmanager.service.UserService;
import com.zlz9.springbootmanager.service.VideoCommentService;
import com.zlz9.springbootmanager.mapper.VideoCommentMapper;
import com.zlz9.springbootmanager.utils.ResponseResult;
import com.zlz9.springbootmanager.vo.AuthorVo;
import com.zlz9.springbootmanager.vo.CommentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
* @author 23340
* @description 针对表【h_video_comment】的数据库操作Service实现
* @createDate 2023-01-14 17:24:56
*/
@Transactional
@Service
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment>
    implements VideoCommentService{
    @Autowired
    VideoCommentMapper videoCommentMapper;
    @Autowired
    UserService userService;
    /**
     * 根据id分页查询数据
     * @param pageById
     * @return
     */
    @Override
    public ResponseResult getCommentById(PageById pageById) {
        LambdaQueryWrapper<VideoComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VideoComment::getVideoId, pageById.getId());
        queryWrapper.eq(VideoComment::getLevel, 1);
        queryWrapper.orderByDesc(VideoComment::getCreateTime );
        Page<VideoComment> page = new Page<>(pageById.getPage(), pageById.getPageSize());
        Page<VideoComment> commentPage = videoCommentMapper.selectPage(page, queryWrapper);
        List<VideoComment> records = commentPage.getRecords();
        List<CommentVo> commentVoList = copyList(records);
        return new ResponseResult(200, commentVoList);
    }

    /**
     * 根据评论id获取文章点赞总数
     * @param id
     * @return
     */
    @Override
    public ResponseResult getCommentCountById(Long id) {
        VideoComment videoComment = videoCommentMapper.selectById(id);
        if (videoComment == null) {
            return new ResponseResult<>(200,0);
        }
        return new ResponseResult(200, videoComment.getLikeCount());
    }

    /**
     * 发布评论
     * @param commentParams
     * @return
     */
    @Override
    public ResponseResult publishComment(CommentParams commentParams) {
        /**
         * 1.获取当前登录信息
         */
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = loginUser.getUser();
        VideoComment videoComment = new VideoComment();
        videoComment.setVideoId(commentParams.getVideoId());
        videoComment.setContent(commentParams.getContent());
        videoComment.setLikeCount(0);
        videoComment.setParentId(commentParams.getParentId());
        videoComment.setCreateTime(System.currentTimeMillis());
        videoComment.setAuthorId(user.getId());
        Long parent = commentParams.getParentId();
        if (parent == null || parent==0) {
            videoComment.setLevel(1);
        }else {
            videoComment.setLevel(2);
        }
        videoComment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParams.getToUserId();
        videoComment.setToUid(toUserId == null ? 0 : toUserId );
        this.videoCommentMapper.insert(videoComment);
        return new ResponseResult<>(200,"评论成功  ");
    }


    private List<CommentVo> copyList(List<VideoComment> records) {
       List<CommentVo> commentVoList = new ArrayList<>();
        for (VideoComment record : records) {
            commentVoList.add(copy(record));
        }
        return commentVoList;
    }

    private CommentVo copy(VideoComment record) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(record, commentVo);
        commentVo.setUser(userService.selectAuthorById(record.getAuthorId()));
        commentVo.setCreateTime(System.currentTimeMillis());
        commentVo.setToUser(userService.selectAuthorById(record.getToUid()));
        Integer level = record.getLevel();
        if (1 == level) {
            Long id = record.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(id);
            if (CollectionUtils.isEmpty(commentVoList)) {
                commentVo.setChildren(null);
            }
            int size = commentVoList.size();
            commentVo.setChildren(commentVoList);
        }
//        to user 给谁评论
        if (level > 1) {
            Long toUid = record.getToUid();
            AuthorVo toUser = this.userService.selectAuthorById(toUid);
            commentVo.setToUser(toUser);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<VideoComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(VideoComment::getParentId, id);
        queryWrapper.eq(VideoComment::getLevel, 2);
        List<VideoComment> videoComments = videoCommentMapper.selectList(queryWrapper);
        return copyList(videoCommentMapper.selectList(queryWrapper));
    }
}




