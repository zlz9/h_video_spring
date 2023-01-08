package com.zlz9.springbootmanager.vo;

import lombok.Data;

/**
 * <h4>springboot-manager</h4>
 * <p>根据标签查视频的vo</p>
 *
 * @author : zlz
 * @date : 2023-01-07 11:26
 **/
@Data
public class VideoCategoryVo {
    private Long id;

    /**
     * 封面
     */
    private String cover;

    /**
     * 资源路径
     */
    private String url;

    /**
     * 视频名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 跟新时间
     */
    private Long updateTime;

    /**
     * 作者id
     */
    private AuthorVo author;

    /**
     * 权重
     */
    private Integer weight;
    /**
     * 是否置顶
     */
    private Boolean isTop;
    /**
     * 视频摘要
     */
    private String selfIntroduction;
}
