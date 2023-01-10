package com.zlz9.springbootmanager.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.zlz9.springbootmanager.utils.RedisCache;
import com.zlz9.springbootmanager.utils.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
   @Resource
   private JavaMailSender javaMailSender;
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

    //读取yml文件中username的值并赋值给form
    @Value("${spring.mail.username}")
    private String from;

    @GetMapping("sendEmail")
    public String sendSimpleMail(@RequestParam(value = "emailReceiver") String emailReceiver) {
        // 构建一个邮件对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置邮件发送者
        message.setFrom(from);
        // 设置邮件接收者
        message.setTo(emailReceiver);
        // 设置邮件的主题
        message.setSubject("登录验证码");
        // 设置邮件的正文
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int r = random.nextInt(10);
            code.append(r);
        }
        /**
         * 把code存入redis
         */
        redisCache.setCacheObject("code", code, 5, TimeUnit.MINUTES);
        String text = "您的验证码为：" + code + ",请勿泄露给他人。";
        message.setText(text);
        // 发送邮件
        try {
            javaMailSender.send(message);
            return "发送成功";
        } catch (MailException e) {
            e.printStackTrace();
        }
        return "发送失败";
    }
}
