package com.zlz9.springbootmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * <h4>websocket-springboot-demo</h4>
 * <p>websocket配置</p>
 *
 * @author : zlz
 * @date : 2023-01-11 19:56
 **/
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}
