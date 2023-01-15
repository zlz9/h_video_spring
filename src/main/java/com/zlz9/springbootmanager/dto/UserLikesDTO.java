package com.zlz9.springbootmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLikesDTO {
    private String infoId;
    private String likeUserId;
    private Integer status;
    private LocalDateTime updateTime;
}
