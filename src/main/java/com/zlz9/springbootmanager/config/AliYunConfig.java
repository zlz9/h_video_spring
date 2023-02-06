package com.zlz9.springbootmanager.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-02-02 11:03
 **/
@Configuration
@Data
public class AliYunConfig {
    private String endPoint="oss-cn-chengdu.aliyuncs.com";// 地域节点
    private String accessKeyId="LTAI5tRYcqdhbYmhS1FhjMGy";
    private String accessKeySecret="U9b75FNlGdWSw1Fc9GuuQrNXjAr93f";
    private String bucketName="h-video";// OSS的Bucket名称
    private String urlPrefix="h-video.oss-cn-chengdu.aliyuncs.com";// Bucket 域名
    private String fileHost="fileTest";// 目标文件夹
    @Bean
    public OSS OSSClient(){
        return new OSSClient(endPoint,accessKeyId,accessKeySecret);
    }
}
