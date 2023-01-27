package com.zlz9.springbootmanager.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-01-27 19:21
 **/
@Data
public class CommentVo {
    private Long id;
    private Long videoId;
    private AuthorVo user;
    private AuthorVo toUser;
    private Long createTime;
    private Long parentId;
    private String content;
    private Integer likeCount;
    private List<CommentVo> children;
}
