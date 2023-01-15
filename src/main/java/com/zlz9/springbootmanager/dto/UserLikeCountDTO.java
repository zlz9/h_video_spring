package com.zlz9.springbootmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLikeCountDTO {
    private String infoId;
    private Integer likeCount;
}
