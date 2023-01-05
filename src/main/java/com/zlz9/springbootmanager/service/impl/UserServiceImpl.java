package com.zlz9.springbootmanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.dto.LoginParams;
import com.zlz9.springbootmanager.pojo.LoginUser;
import com.zlz9.springbootmanager.pojo.User;
import com.zlz9.springbootmanager.service.UserService;
import com.zlz9.springbootmanager.mapper.UserMapper;
import com.zlz9.springbootmanager.utils.JwtUtil;
import com.zlz9.springbootmanager.utils.RedisCache;
import com.zlz9.springbootmanager.utils.ResponseResult;
import com.zlz9.springbootmanager.vo.AuthorVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Objects;

/**
* @author 23340
* @description 针对表【h_user】的数据库操作Service实现
* @createDate 2023-01-01 14:20:42
*/
@Service
@Transactional
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisCache redisCache;
    @Autowired
    UserMapper userMapper;
    @Override
    public ResponseResult login(LoginParams loginParams) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginParams.getUserName(), loginParams.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
//            使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        log.info("userId:"+userId);
        String jwt = JwtUtil.createJWT(userId);

        //authenticate存入redis
        redisCache.setCacheObject("login:"+userId, loginUser);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return new ResponseResult(200,"登陆成功",map);
    }

    /**
     * 根据id查找作者信息
     * @param authorId
     * @return
     */
    @Override
    public AuthorVo selectAuthorById(Long authorId) {
        User user = userMapper.selectById(authorId);
        AuthorVo authorVo = new AuthorVo();
        BeanUtils.copyProperties(user,authorVo);
        return authorVo;
    }

    @Override
    public ResponseResult logout() {
        //        获取SecurityContextHolder的用户id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
//        删除redis的值
        redisCache.deleteObject("login:"+userid);
        return new ResponseResult(200, "注销成功");
    }
}




