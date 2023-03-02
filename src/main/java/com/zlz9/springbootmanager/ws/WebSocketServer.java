package com.zlz9.springbootmanager.ws;

import com.zlz9.springbootmanager.pojo.LoginUser;
import com.zlz9.springbootmanager.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-01-17 19:07
 **/
@Slf4j
@Component
@ServerEndpoint("/websocket/{code}")
//@ServerEndpoint("/websocket")
public class WebSocketServer {

    //    在线人数
    private static int onlineCount;
    //    当前会话
    private Session session;
    //    用户唯一标识
    private String id;
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    public static ConcurrentHashMap<String, WebSocketServer> getWebSocketMap() {
        return webSocketMap;
    }

    public static void setWebSocketMap(ConcurrentHashMap<String, WebSocketServer> webSocketMap) {
        WebSocketServer.webSocketMap = webSocketMap;
    }

    /**
     * concurrent包的线程安全set，用来存放每个客户端对应的MyWebSocket对象
     */
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap();

    /**
     * 为了保存在线用户信息，在方法中新建一个list存储一下【实际项目依据复杂度，可以存储到数据库或者缓存】
     */
    private final static List<Session> SESSIONS = Collections.synchronizedList(new ArrayList<>());

    /**
     * @methodName: onOpen
     * @description: 建立连接
     * @Author LiuTao
     * @updateTime 2022/8/19 19:31
     * @return void
     * @throws
     **/
    @OnOpen
    public void onOpen(Session session, @PathParam("code") String code) throws Exception {
        Claims claims = JwtUtil.parseJWT(code);
//        解析出id
        String id = claims.getSubject();
        this.session = session;
        this.id = id;
        //设置超时，同httpSession
        session.setMaxIdleTimeout(3600000);
        webSocketSet.add(this);
        SESSIONS.add(session);
        if (webSocketMap.containsKey(id)) {
            webSocketMap.remove(id);
            webSocketMap.put(id,this);
        } else {
            webSocketMap.put(id,this);
            addOnlineCount();
        }
        log.info("[连接ID:{}] 建立连接, 当前连接数:{}", this.id, getOnlineCount());
    }

    /**
     * @methodName: onClose
     * @description: 断开连接
     * @Author LiuTao
     * @updateTime 2022/8/19 19:31
     * @return void
     * @throws
     **/
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        if (webSocketMap.containsKey(id)) {
            webSocketMap.remove(id);
            subOnlineCount();
        }
        log.info("[连接ID:{}] 断开连接, 当前连接数:{}", id, getOnlineCount());
    }

    /**
     * @methodName: onError
     * @description: 发送错误
     * @Author LiuTao
     * @updateTime 2022/8/19 19:32
     * @return void
     * @throws
     **/
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("[连接ID:{}] 错误原因:{}", this.id, error.getMessage());
        error.printStackTrace();
    }

    /**
     * @methodName: onMessage
     * @description: 收到消息
     * @Author LiuTao
     * @updateTime 2022/8/19 19:32
     * @return void
     * @throws
     **/
    @OnMessage
    public void onMessage(String message) {
        log.info("[连接ID:{}] 收到消息:{}", this.id, message);
    }
    /**
     * @methodName: sendMessage
     * @description: 发送消息
     * @Author LiuTao
     * @updateTime 2022/8/19 19:32
     * @return void
     * @throws
     **/
    public void sendMessage(String message,Long userId) {
        WebSocketServer webSocketServer = webSocketMap.get(String.valueOf(userId));
        if (webSocketServer!=null){
            log.info("【websocket消息】推送消息,[toUser]userId={},message={}", userId,message);
            try {
                webSocketServer.session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("[连接ID:{}] 发送消息失败, 消息:{}", this.id, message, e);
            }
        }
    }

    /**
     * @methodName: sendMassMessage
     * @description: 群发消息
     * @Author LiuTao
     * @updateTime 2022/8/19 19:33
     * @return void
     * @throws
     **/
    public void sendMassMessage(String message) {
        try {
            for (Session session : SESSIONS) {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(message);
                    log.info("[连接ID:{}] 发送消息:{}",session.getRequestParameterMap().get("userId"),message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前连接数
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 当前连接数加一
     */
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    /**
     * 当前连接数减一
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
