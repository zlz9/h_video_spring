package com.zlz9.springbootmanager.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlz9.springbootmanager.controller.UploadController;
import com.zlz9.springbootmanager.dto.LoginParams;
import com.zlz9.springbootmanager.dto.RegisterParams;
import com.zlz9.springbootmanager.dto.UserDTO;
import com.zlz9.springbootmanager.lang.Const;
import com.zlz9.springbootmanager.mapper.VideoMapper;
import com.zlz9.springbootmanager.pojo.LoginUser;
import com.zlz9.springbootmanager.pojo.User;
import com.zlz9.springbootmanager.pojo.Video;
import com.zlz9.springbootmanager.service.FileUploadService;
import com.zlz9.springbootmanager.service.UserService;
import com.zlz9.springbootmanager.mapper.UserMapper;
import com.zlz9.springbootmanager.service.VideoService;
import com.zlz9.springbootmanager.utils.JwtUtil;
import com.zlz9.springbootmanager.utils.RedisCache;
import com.zlz9.springbootmanager.utils.ResponseResult;
import com.zlz9.springbootmanager.utils.UserNameUtils;
import com.zlz9.springbootmanager.vo.AuthorVideoVo;
import com.zlz9.springbootmanager.vo.AuthorVo;
import com.zlz9.springbootmanager.vo.UserInfoVo;
import com.zlz9.springbootmanager.vo.VideoVo;
import com.zlz9.springbootmanager.ws.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * @author 23340
 * @description ????????????h_user?????????????????????Service??????
 * @createDate 2023-01-01 14:20:42
 */
@Service
@Transactional
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisCache redisCache;
    @Autowired
    UserMapper userMapper;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    FileUploadService fileUploadService;


    @Override
    public ResponseResult login(LoginParams loginParams) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginParams.getUserName(), loginParams.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("????????????????????????");
        }
//            ??????userid??????token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        log.info("userId:" + userId);
        String jwt = JwtUtil.createJWT(userId);

        //authenticate??????redis
        redisCache.setCacheObject("login:" + userId, loginUser);
        //???token???????????????
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return new ResponseResult(200, "????????????", map);
    }

    /**
     * ??????id??????????????????
     *
     * @param authorId
     * @return
     */
    @Override
    public AuthorVo selectAuthorById(Long authorId) {
        User user = userMapper.selectById(authorId);
        if (ObjectUtil.isNotNull(user)) {
            AuthorVo authorVo = new AuthorVo();
            BeanUtils.copyProperties(user, authorVo);
            return authorVo;
        }else {
            return null;
        }
    }

    /**
     * ????????????
     *
     * @param registerParams
     * @return
     */
    @Override
    public ResponseResult register(RegisterParams registerParams) {
        //1.??????LoginUser???
        User user = new User();
        user.setAge(22);
        user.setAvatar("https://v.qq.com/x/cover/mzc00200bznlvdt/e3330e2vrs7.html");
        user.setUserName(registerParams.getUserName());
        user.setEmail(registerParams.getEmail());
        user.setCreateTime(System.currentTimeMillis());
        user.setPhoneNumber(Const.UNKNOWN);
        user.setNickName(UserNameUtils.getRandomJianHan(4));

        user.setPassword(passwordEncoder.encode(registerParams.getPassword()));
//        ???????????????
        String code = registerParams.getCode();
        String checkCode = redisCache.getCacheObject("code");
        if (checkCode == null) {
            return new ResponseResult(400, "???????????????");
        }
        if (!code.equals(checkCode)) {
            return new ResponseResult(400, "???????????????");
        }
        userMapper.insert(user);
        return new ResponseResult(200, "???????????????");
    }

    /**
     * ??????id????????????id
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult getAuthorInfoById(Long id) {
        User user = userMapper.selectById(id);
        AuthorVo authorVo = new AuthorVo();
        BeanUtils.copyProperties(user, authorVo);
        return new ResponseResult<>(200,authorVo);
    }

    /**
     * ????????????????????????
     * @return
     */
    @Override
    public ResponseResult getCurrentUser() {
       LoginUser  loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userMapper.selectById(loginUser.getUser().getId());
        AuthorVo authorVo = new AuthorVo();
        BeanUtils.copyProperties(user, authorVo);
        return new ResponseResult<>(200,authorVo);
    }

    /**
     * ??????????????????
     * @param userDTO
     * @return
     */
    @Override
    public ResponseResult updateUserInfo(UserDTO userDTO) {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        User currentUser = userMapper.selectById(loginUser.getUser().getId());
        boolean matches = passwordEncoder.matches(userDTO.getOldPwd(), currentUser.getPassword());
        if (!matches) {
            return new ResponseResult(500, "???????????????");
        }else {
            //            ??????????????????
           fileUploadService.delete(currentUser.getAvatar());
            User user = new User();
            user.setNickName(userDTO.getNickName());
            user.setPassword(passwordEncoder.encode(userDTO.getNewPwd()));
            user.setAvatar(userDTO.getAvatar());
            user.setSelfIntroduction(userDTO.getSelfIntroduction());
            user.setId(loginUser.getUser().getId());
            userMapper.updateById(user);
        }
        return new ResponseResult<>(200,"????????????");
    }

    /**
     * ???????????????user??????
     * @return
     */
    @Override
    public ResponseResult getAllUser() {
        List<User> users = userMapper.selectList(null);
        List<UserInfoVo> userInfoVoList = copyUserList(users);
        return new ResponseResult(200, userInfoVoList);
    }

    private List<UserInfoVo> copyUserList(List<User> users) {
        List<UserInfoVo> userInfoVoList = new ArrayList<>();
        for (User user : users) {
            userInfoVoList.add(copyUser(user));
        }
        return userInfoVoList;
    }

    private UserInfoVo copyUser(User user) {
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        return userInfoVo;
    }

    /**
     * ????????????
     * @param id
     * @return
     */
    @Override
    public ResponseResult disableById(Long id) {
        User user = userMapper.selectById(id);
        user.setStatus(false);
        userMapper.updateById(user);
        return new ResponseResult<>(200,"??????????????????");
    }

    @Override
    public ResponseResult enableUserById(Long id) {
        User user = userMapper.selectById(id);
        user.setStatus(true);
        userMapper.updateById(user);
        return new ResponseResult<>(200,"????????????");
    }

    /**
     * ????????????id????????????
     * @param id
     * @return
     */
    @Override
    public ResponseResult resetPassword(Long id) {
        String encode = passwordEncoder.encode(Const.rawPasswod);
        User user = userMapper.selectById(id);
        user.setPassword(encode);
        return new ResponseResult<>(200,"?????????????????????"+Const.rawPasswod+",?????????????????????");
    }

    private VideoVo copy(Video video) {
        VideoVo videoVo = new VideoVo();
        BeanUtils.copyProperties(video, videoVo);
        return videoVo;
    }

    @Override
    public ResponseResult logout() {
        //        ??????SecurityContextHolder?????????id
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
//        ??????redis??????
        redisCache.deleteObject("login:" + userid);
        return new ResponseResult(200, "????????????");
    }
}

