package com.zlz9.springbootmanager.service;

import com.zlz9.springbootmanager.pojo.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlz9.springbootmanager.utils.ResponseResult;

/**
* @author 23340
* @description 针对表【h_tag】的数据库操作Service
* @createDate 2023-01-01 14:20:42
*/
public interface TagService extends IService<Tag> {

    ResponseResult getTag();

    ResponseResult getVideoByTag(Long id);

    Tag getTagById(Integer tagId);
}
