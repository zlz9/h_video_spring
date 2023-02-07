package com.zlz9.springbootmanager.service;

import cn.hutool.log.Log;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
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

    public ResponseResult delete(String url) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = aliYunConfig.getEndPoint();
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = aliYunConfig.getAccessKeyId();
        String accessKeySecret = aliYunConfig.getAccessKeySecret();
        // 填写Bucket名称，例如examplebucket。
        String bucketName = aliYunConfig.getBucketName();
        // 填写文件完整路径。文件完整路径中不能包含Bucket名称。
        //解析出url的真实
        /**
         * 文件路径：fileTest/095ea2d4-64f7-4856-8341-29219027b486.mp4
         * url：https://h-video.oss-cn-chengdu.aliyuncs.com/fileTest/095ea2d4-64f7-4856-8341-29219027b486.mp4
         */
        String prefix = "http://h-video.oss-cn-chengdu.aliyuncs.com/";
        String objectName = url.replaceAll(prefix, "");
        log.info("url路径=>{}",url);
        log.info("文件路径=>{}",objectName);

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, objectName);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return new ResponseResult<>(200,"删除成功");
    }
}
