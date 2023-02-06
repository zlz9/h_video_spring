package com.zlz9.springbootmanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-02-04 11:44
 **/
@Data
public class PublishVideoParams {

   private List<Long> tagIds;
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
    private Long authorId;

    /**
     * 状态(0正常，1已删除)
     */
    private Boolean isDel;
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
