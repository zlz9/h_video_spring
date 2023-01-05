package com.zlz9.springbootmanager.filter;


import com.zlz9.springbootmanager.exception.CaptchaException;
import com.zlz9.springbootmanager.handler.LoginFailureHandler;
import com.zlz9.springbootmanager.utils.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <h4>blog_admin</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2022-08-27 00:51
 **/
@Component
public class CaptchaFilter extends OncePerRequestFilter {
    @Autowired
    LoginFailureHandler loginFailureHandler;
    @Autowired
    RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURI();
        if ("/api/login".equals(url) && request.getMethod().equalsIgnoreCase("POST")) {
            try {
                validate(request);
            } catch (CaptchaException e) {
                 loginFailureHandler.onAuthenticationFailure(request, response, e);
            }

        }
        filterChain.doFilter(request, response);
    }
    // 校验验证码逻辑
    private void validate(HttpServletRequest httpServletRequest) throws CaptchaException {

        String code = httpServletRequest.getParameter("code");
        if (StringUtils.isBlank(code)) {
            throw new CaptchaException("验证码错误");
        }
        String  captcha = redisCache.getCacheObject("captcha");
        if (!code.equals(captcha)) {
            throw new CaptchaException("验证码错误");
        }

        // 一次性使用
        redisCache.deleteObject("captcha");
    }
}

