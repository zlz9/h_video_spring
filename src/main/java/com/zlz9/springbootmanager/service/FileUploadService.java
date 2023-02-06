package com.zlz9.springbootmanager.service;

import cn.hutool.log.Log;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyuncs.exceptions.ClientException;
import com.zlz9.springbootmanager.config.AliYunConfig;
import com.zlz9.springbootmanager.lang.Const;
import com.zlz9.springbootmanager.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.aliyun.oss.OSSClientBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-02-02 11:22
 **/
@Slf4j
@Service
public class FileUploadService {
    //设置允许上传文件格式
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".png", ".gif", ".mp3", ".mp4", ".mkv"};
    @Autowired
    private OSS ossClient;
    @Autowired
    private AliYunConfig aliYunConfig;

    public String upload(MultipartFile file) {
        String bucketNanme = aliYunConfig.getBucketName();
        String endPoint = aliYunConfig.getEndPoint();
        String accessKeyId = aliYunConfig.getAccessKeyId();
        String accessKeySecret = aliYunConfig.getAccessKeySecret();
        String fileHost = aliYunConfig.getFileHost();
        //返回的Url
        String returnUrl = "";
        //审核上传文件是否符合规定格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }
        if (!isLegal) {
//            如果不正确返回错误状态码
            return Const.ERROR;
        }
        //获取文件的名称
        String originalFilename = file.getOriginalFilename();
        //截取文件类型
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
//        最终保存文件名称
        String newFileName = UUID.randomUUID().toString() + fileType;
//        文件上传文件的路径
        String uploadUrl = fileHost  + "/" + newFileName;
//        获取文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //文件上传到阿里云oss
//        ossClient.put
        ossClient.putObject(bucketNanme, uploadUrl, inputStream);//,meta
        returnUrl = "http://" + bucketNanme + "." + endPoint + "/" + uploadUrl;
        return returnUrl;
    }

    public ResponseResult delete(String fileName) {
        String endPoint = aliYunConfig.getEndPoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = aliYunConfig.getAccessKeyId();
        String accessKeySecret = aliYunConfig.getAccessKeySecret();
        // 填写Bucket名称，例如examplebucket
        String bucketName = aliYunConfig.getBucketName();
        String objectName = fileName;
        boolean flag = false;
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
        ossClient.deleteObject(bucketName, fileName);
        log.info("删除文件路径:{}",fileName);
       // 关闭OSSClient。
        ossClient.shutdown();
        return new ResponseResult<>(200,"删除成功");
    }
}
