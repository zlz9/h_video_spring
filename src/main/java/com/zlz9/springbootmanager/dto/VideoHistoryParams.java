package com.zlz9.springbootmanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-01-27 13:51
 **/
@Data
public class VideoHistoryParams {
    private List<Long> ids;
}
