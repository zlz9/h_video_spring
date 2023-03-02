package com.zlz9.springbootmanager.dto;

import lombok.Data;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-02-23 12:08
 **/
@Data
public class TagDTO {

    /**
     * 标签名
     */
    private String tagName;


    /**
     * 图标
     */
    private String icon;
}
