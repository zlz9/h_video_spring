package com.zlz9.springbootmanager.vo;

import lombok.Data;

/**
 * <h4>springboot-manager</h4>
 * <p>标签vo层</p>
 *
 * @author : zlz
 * @date : 2023-01-07 11:32
 **/
@Data
public class TagVo {
    private Long id;

    /**
     * 标签名
     */
    private String tagName;
    private String icon;

}
