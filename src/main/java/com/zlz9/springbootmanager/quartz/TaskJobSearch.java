package com.zlz9.springbootmanager.quartz;

import com.zlz9.springbootmanager.service.DBService;
import com.zlz9.springbootmanager.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <h4>springboot-manager</h4>
 * <p></p>
 *
 * @author : zlz
 * @date : 2023-01-29 17:31
 **/
@Component
@EnableScheduling
public class TaskJobSearch {
    @Autowired
    RedisService redisService;
    public  void delSearch() throws InterruptedException {
        System.out.println("定时任务删除搜素榜单正在执行......"+new Date());
        redisService.delSearch();
        System.out.println("定时任务删除搜素榜单执行完毕......"+new Date());
    }
}
