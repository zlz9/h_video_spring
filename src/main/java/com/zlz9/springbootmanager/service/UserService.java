package com.zlz9.springbootmanager.service;

import com.zlz9.springbootmanager.dto.LoginParams;
import com.zlz9.springbootmanager.dto.RegisterParams;
import com.zlz9.springbootmanager.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import com.zlz9.springbootmanager.vo.AuthorVo;

/**
* @author 23340
* @description 针对表【h_user】的数据库操作Service
* @createDate 2023-01-01 14:20:42
*/
public interface UserService extends IService<User> {

    ResponseResult logout();

    ResponseResult login(LoginParams loginParams);

    AuthorVo selectAuthorById(Long authorId);

    ResponseResult register(RegisterParams registerParams);

    ResponseResult getAuthorInfoById(Long id);
}
