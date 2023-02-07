package com.zlz9.springbootmanager.controller;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.zlz9.springbootmanager.config.AliYunConfig;
import com.zlz9.springbootmanager.service.FileUploadService;
import com.zlz9.springbootmanager.utils.ResponseResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-02-02 11:21
 **/
@Slf4j
@RestController
@RequestMapping("api")
public class UploadController {
    @Autowired
    private FileUploadService fileUploadService;
   @Autowired
   private AliYunConfig aliYunConfig;
    @ApiOperation(value = "文件上传")
    @PostMapping("upload")
    public ResponseResult upload(MultipartFile file) {


        String returnFileUrl = null;
        if (file != null) {
            returnFileUrl = fileUploadService.upload(file);
            if (StringUtils.isEmpty(returnFileUrl)) {
//                返回路径为空表示异常
                return new ResponseResult<>(400,"url为空");
            }
            return new ResponseResult<>(200,returnFileUrl);
        } else {
            return new ResponseResult(500,"失败");
        }
    }

    @PostMapping("/down/{filename}")
    public ResponseResult DownLoad(@PathVariable String filename, HttpServletResponse response) {
        return null;

    }

    /**
     * 文件删除api
     */
    @DeleteMapping("video/del")
    public ResponseResult deleteFile(@RequestParam String url){
       return fileUploadService.delete(url);
    }
}
