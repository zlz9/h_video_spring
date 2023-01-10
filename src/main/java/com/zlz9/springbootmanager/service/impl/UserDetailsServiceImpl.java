package com.zlz9.springbootmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlz9.springbootmanager.mapper.MenuMapper;
import com.zlz9.springbootmanager.mapper.UserMapper;
import com.zlz9.springbootmanager.pojo.LoginUser;
import com.zlz9.springbootmanager.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-01-01 14:00
 **/
@Slf4j
@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new RuntimeException("为查找到用户");
        }
//TODO 根据用户查询权限信息 添加到LoginUser中
        //封装成UserDetails对象返回
//        List<String> permissionKeyList= menuMapper.selectPermsByUserId(user.getId());
//        测试写法
        List<String> list = new ArrayList<>(Arrays.asList("test"));
        return new LoginUser(user,list );
    }
}
