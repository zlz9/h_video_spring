package com.zlz9.springbootmanager.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName h_video
 */
@TableName(value ="h_video")
@Data
public class Video implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
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
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}