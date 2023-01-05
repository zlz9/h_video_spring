package com.zlz9.springbootmanager.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.zlz9.springbootmanager.utils.RedisCache;
import com.zlz9.springbootmanager.utils.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <h4>springboot-manager</h4>
 * <p>生成验证码</p>
 *
 * @author : zlz
 * @date : 2023-01-02 18:05
 **/
@RequestMapping("api")
@RestController
public class CaptureController {
    @Autowired
    private RedisCache redisCache;

    /**
     * 验证码
     * @param response
     * @throws IOException
     */
    @GetMapping( "/captcha")
    public void getCaptcha(HttpServletResponse response) throws IOException {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(116, 30, 4, 10);
        String code = lineCaptcha.getCode();
        redisCache.setCacheObject("captcha", code);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            lineCaptcha.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
