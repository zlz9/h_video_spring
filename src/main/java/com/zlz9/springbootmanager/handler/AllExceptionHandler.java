package com.zlz9.springbootmanager.handler;

import com.zlz9.springbootmanager.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.BindException;

/**
 * <h4>blog_admin</h4>
 * <p>统一处理异常信息</p>
 *
 * @author : zlz
 * @date : 2022-11-20 18:03
 **/
@Slf4j
@ControllerAdvice //对加了controller注解的方法进行拦截处理 aop实现
public class AllExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseResult validExceptionHandler(MethodArgumentNotValidException  ex){
        ex.printStackTrace();
        return new ResponseResult<>(400,ex.getBindingResult().getAllErrors());
    }
}
