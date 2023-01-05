package com.zlz9.springbootmanager.mapper;

import com.zlz9.springbootmanager.pojo.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlz9.springbootmanager.pojo.Video;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 23340
* @description 针对表【h_tag】的数据库操作Mapper
* @createDate 2023-01-01 14:20:42
* @Entity com.zlz9.springbootmanager.pojo.Tag
*/
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    List<Video> selectVideoByTagId(Long id);
}




