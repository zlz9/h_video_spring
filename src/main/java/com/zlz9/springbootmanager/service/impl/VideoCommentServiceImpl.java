package com.zlz9.springbootmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.dto.PageById;
import com.zlz9.springbootmanager.pojo.VideoComment;
import com.zlz9.springbootmanager.service.UserService;
import com.zlz9.springbootmanager.service.VideoCommentService;
import com.zlz9.springbootmanager.mapper.VideoCommentMapper;
import com.zlz9.springbootmanager.utils.ResponseResult;
import com.zlz9.springbootmanager.vo.AuthorVo;
import com.zlz9.springbootmanager.vo.CommentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.xml.stream.events.Comment;
import java.util.ArrayList;
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
            commentVo.setChildren(commentVoList);
        }
//        to user 给谁评论
//        if (level > 1) {
//            Long toUid = record.getToUid();
//            AuthorVo toUser = this.userService.selectAuthorById(toUid);
//            commentVo.setToUser(toUser);
//        }
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




