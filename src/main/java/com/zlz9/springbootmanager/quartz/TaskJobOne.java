package com.zlz9.springbootmanager.quartz;

import com.zlz9.springbootmanager.service.DBService;
import com.zlz9.springbootmanager.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.xml.ws.Action;
import java.util.Date;

import static java.lang.Thread.sleep;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-01-19 12:11
 * 持久化聊天信息
 **/
@Component
@Slf4j
@EnableScheduling
public class TaskJobOne {
    @Autowired
    DBService dbService;
    @Autowired
    RedisService redisService;
    public  void testJobOneMethod() throws InterruptedException {
        log.info("1 聊天列表持久化 定时任务1正在执行......"+new Date());
        dbService.transChatFromRedis2DB();
        log.info("1 聊天列表持久化 定时任务1业务代码执行完毕......"+new Date());
    }

}
