package com.zlz9.springbootmanager.handler;

import com.zlz9.springbootmanager.utils.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * <h4>blog_admin</h4>
 * <p>统一处理异常信息</p>
 *
 * @author : zlz
 * @date : 2022-11-20 18:03
 **/
@ControllerAdvice //对加了controller注解的方法进行拦截处理 aop实现
public class AllExceptionHandler {
//    进行异常处理
    @ExceptionHandler(Exception.class)
    public ResponseResult doException(Exception e){
        e.printStackTrace();
        return new ResponseResult(-999, "系统异常");
    }
}
