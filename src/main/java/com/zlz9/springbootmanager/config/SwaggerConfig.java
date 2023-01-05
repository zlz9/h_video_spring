package com.zlz9.springbootmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 1. swagger配置类
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zlz9.springbootmanager.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("热门小视频推荐系统文档")
                .description("热门小视频推荐系统文档restful API")
                .version("0.0.1")
                .contact(contact())
                .build();
    }

    /**
     * 添加token
     * @return
     */
//    private List<Parameter> jwtToken() {
//
//        String jwt = "Bearer {jwt}";
//
//        ParameterBuilder tokenPar = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<>();
//        // 声明 key
//        tokenPar.name("Authorization")
//                // 文字说明
//                .description("jwt令牌")
//                // 类型为字符串
//                .modelRef(new ModelRef("string"))
//                // 参数形式为 header 参数
//                .parameterType("header")
//                // 默认值
//                .defaultValue(jwt)
//                // 是否必须
//                .required(false);
//        pars.add(tokenPar.build());
//        return pars;
//    }
    private Contact contact(){
        return new Contact("周利贞", "暂无", "2334094446@qq.com");
    }
}

