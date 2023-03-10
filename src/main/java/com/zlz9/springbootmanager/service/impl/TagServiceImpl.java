package com.zlz9.springbootmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.dto.TagDTO;
import com.zlz9.springbootmanager.pojo.LoginUser;
import com.zlz9.springbootmanager.pojo.Tag;
import com.zlz9.springbootmanager.pojo.Video;
import com.zlz9.springbootmanager.service.TagService;
import com.zlz9.springbootmanager.mapper.TagMapper;
import com.zlz9.springbootmanager.service.UserService;
import com.zlz9.springbootmanager.service.VideoTagService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import com.zlz9.springbootmanager.vo.TagVo;
import com.zlz9.springbootmanager.vo.VideoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
* @author 23340
* @description 针对表【h_tag】的数据库操作Service实现
* @createDate 2023-01-01 14:20:42
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{
     @Autowired
     TagMapper tagMapper;
     @Autowired
     UserService userService;
     @Autowired
    VideoTagService videoTagService;

    /**
     * 获取视频标签
     * @return
     */
    @Override
    public ResponseResult getTag() {
        List<Tag> tags = tagMapper.selectList(null);
        if (CollectionUtils.isEmpty(tags)) {
            return new ResponseResult<>(404,"未找到");
        }
        return new ResponseResult(200, tags);
    }

    /**
     * 通过标签id获取视频
     * @param id
     * @return
     */
    @Override
    public ResponseResult getVideoByTag(Long id) {
       List<Video> videoList= tagMapper.selectVideoByTagId(id);
       List<VideoVo> videoVoList = copyList(videoList);
        if (CollectionUtils.isEmpty(videoList)) {
            return new ResponseResult(404, "未找到");
        }
        return new ResponseResult<>(200,videoVoList);
    }

    /**
     * 根据id查询tag
     * @param tagId
     * @return
     */
    @Override
    public Tag getTagById(Integer tagId) {
        Tag tag = tagMapper.selectById(tagId);
        return tag;
    }

    /**
     * 上传id
     * @param tagDTO
     * @return
     */
    @Override
    public ResponseResult uploadTag(TagDTO tagDTO) {
        Tag tag = new Tag();
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tag.setTagName(tagDTO.getTagName());
        tag.setCreateTime(System.currentTimeMillis());
        tag.setAuthorId(loginUser.getUser().getId());
        tag.setCreateTime(System.currentTimeMillis());
        tag.setIcon(tagDTO.getIcon());
        tagMapper.insert(tag);
        return new ResponseResult(200, "插入成功");
    }
    /**
     * 根据id删除tag
     * @param id
     * @return
     */
    @Override
    public ResponseResult delTagById(Long id) {
        tagMapper.deleteById(id);
        return new ResponseResult<>(200,"删除成功");
    }

    /**
     * 根据id查询标签列表
     * @param id
     * @return
     */
    @Override
    public List<TagVo> getTagsById(Long id) {
        List<TagVo> tagVoList = tagMapper.getTagsByVideoId(id);
        return tagVoList;
    }


    private List<VideoVo> copyList(List<Video> videoList) {
        List<VideoVo> videoVoList = new ArrayList<>();
        for (Video video : videoList) {
            videoVoList.add(copy(video));
        }
        return videoVoList;
    }

    private VideoVo copy(Video video) {
        VideoVo videoVo = new VideoVo();
        BeanUtils.copyProperties(video, videoVo);
        videoVo.setAuthor(userService.selectAuthorById(video.getAuthorId()));
        return videoVo;
    }
}




