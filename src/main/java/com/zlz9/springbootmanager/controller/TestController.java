package com.zlz9.springbootmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyuncs.exceptions.ClientException;
import com.zlz9.springbootmanager.config.AliYunConfig;
import com.zlz9.springbootmanager.service.FileUploadService;
import com.zlz9.springbootmanager.utils.OssUtil;
import com.zlz9.springbootmanager.utils.ResponseResult;
import io.swagger.annotations.ApiOperation;
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

@RestController
@RequestMapping("api")
public class TestController {
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
        // 创建OSSClient实例（OSS客户端实例）
        OSS ossClient = new OSSClientBuilder().build(aliYunConfig.getEndPoint(),
                aliYunConfig.getAccessKeyId(), aliYunConfig.getAccessKeySecret());
        //文件名（服务器上的文件路径）
        String host = "http://" + aliYunConfig.getBucketName() + "." + aliYunConfig.getEndPoint() + "/";
        String objectName = url.substring(host.length());

        // 判断是否删除成功
        boolean deleteFileSucceed = false;
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(aliYunConfig.getBucketName(), objectName);

            deleteFileSucceed = true;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        if (deleteFileSucceed) {
           return new ResponseResult(200, "删除成功");
        } else {
            return new ResponseResult(400, "删除失败");
        }

    }
}
