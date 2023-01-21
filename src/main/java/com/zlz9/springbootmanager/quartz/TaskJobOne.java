package com.zlz9.springbootmanager.quartz;

import com.zlz9.springbootmanager.service.DBService;
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
@EnableScheduling
public class TaskJobOne {
    @Autowired
    DBService dbService;
    public  void testJobOneMethod() throws InterruptedException {
        System.out.println("1  定时任务1正在执行......"+new Date());
        dbService.transChatFromRedis2DB();
        System.out.println("1  定时任务1业务代码执行完毕......"+new Date());
    }

}
