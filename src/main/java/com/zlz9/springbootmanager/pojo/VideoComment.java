package com.zlz9.springbootmanager.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName h_video_comment
 */
@TableName(value ="h_video_comment")
@Data
public class VideoComment implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Long createTime;

    /**
     * 
     */
    private Long updateTime;

    /**
     * 
     */
    private Long videoId;

    /**
     * 
     */
    private Long authorId;

    /**
     * 
     */
    private Long toUid;

    /**
     * 
     */
    private Long parentId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}