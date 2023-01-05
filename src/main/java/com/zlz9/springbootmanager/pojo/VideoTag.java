package com.zlz9.springbootmanager.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName h_video_tag
 */
@TableName(value ="h_video_tag")
@Data
public class VideoTag implements Serializable {
    /**
     * 
     */
    private Long videoId;

    /**
     * 
     */
    private Long tagId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}