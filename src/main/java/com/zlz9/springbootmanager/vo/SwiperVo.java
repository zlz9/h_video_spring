package com.zlz9.springbootmanager.vo;

import lombok.Data;

/**
 * <h4>springboot-manager</h4>
 * <p>轮播图vo</p>
 *
 * @author : zlz
 * @date : 2023-01-02 20:15
 **/
@Data
public class SwiperVo {
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
}
