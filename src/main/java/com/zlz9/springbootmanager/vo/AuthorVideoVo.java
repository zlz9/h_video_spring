package com.zlz9.springbootmanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <h4>springboot-manager</h4>
 * <p>用户信息和用户的视频vo</p>
 *
 * @author : zlz
 * @date : 2023-01-15 16:39
 **/
@Data
public class AuthorVideoVo {
    private AuthorVo author;
    private List<VideoVo> videoList;
}
