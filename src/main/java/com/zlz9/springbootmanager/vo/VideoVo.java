package com.zlz9.springbootmanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <h4>springboot-manager</h4>
 * <p>视频vo</p>
 *
 * @author : zlz
 * @date : 2023-01-05 15:52
 **/
@Data
public class VideoVo {
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
     * 作者信息
     */
    private AuthorVo author;
    /**
     * 权重
     */
    private Integer weight;
    /**
     * 摘要
     */
    private String selfIntroduction;
    /**
     * 是否顶置
     */
    private Boolean isTop;
    /**
     * 标签
     */
    private List<TagVo> tags;
}
