package com.zlz9.springbootmanager.dto;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * <h4>springboot-manager</h4>
 * <p>分页参数</p>
 *
 * @author : zlz
 * @date : 2023-01-06 20:06
 **/
@Data
public class PageParams {
    private Integer tagId;
    private Integer page = 1;
    private Integer pageSize = 10;
}
