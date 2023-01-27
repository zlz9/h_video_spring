package com.zlz9.springbootmanager.dto;

import lombok.Data;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-01-27 19:36
 **/
@Data
public class PageById {
    private Long id;
    private Integer page;
    private Integer pageSize;
}
