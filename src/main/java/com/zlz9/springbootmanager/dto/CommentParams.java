package com.zlz9.springbootmanager.dto;

import lombok.Data;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-01-30 10:20
 **/
@Data
public class CommentParams {
    /**
     * 文章id
     */
    private Long videoId;
    /**
     * 评内容
     */
    private String content;
    /**
     * 父id
     */
    private Long parentId;
    /**
     * 回复谁的评论
     */
    private Long toUserId;
    /**
     * 层级
     */
    private Integer level;
}
