package com.zlz9.springbootmanager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.zlz9.springbootmanager.mapper")
@EnableSwagger2
@EnableCaching
@EnableTransactionManagement
public class SpringbootManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootManagerApplication.class, args);
    }
}
