package com.zlz9.springbootmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.zlz9.springbootmanager.ws.WebSocketServer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h4>websocket-springboot-demo</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-01-13 09:55
 **/
@RestController
@RequestMapping("/ws")
public class WebSocketController {
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 消息发送
     */
    @GetMapping("/send/{userId}/{msg}")
    public void send(@PathVariable String msg, @PathVariable String userId){
        webSocketServer.sendMessage(JSONObject.toJSONString(msg), Long.valueOf(String.valueOf(userId)));
    }

    /**
     * 群发消息测试(给当前连接用户发送)
     */
    @GetMapping("/sendMassMessage")
    public void sendMassMessage(){
        WebsocketResponse response = new WebsocketResponse();
        response.setTitle("群发主题");
        webSocketServer.sendMassMessage(JSONObject.toJSONString(response));
    }

    @Data
    @Accessors(chain = true)
    public static class WebsocketResponse {
        private String title;
        private String userId;
        private String userName;
        private int age;
    }

}
