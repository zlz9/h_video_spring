package com.zlz9.springbootmanager.vo;

import lombok.Data;

/**
 * <h4>springboot-manager</h4>
 * <p>用户信息vo</p>
 *
 * @author : zlz
 * @date : 2023-01-05 16:03
 **/
@Data
public class AuthorVo {
    private Long id;
    private String nickName;
    private String avatar;
    private String selfIntroduction;
}
