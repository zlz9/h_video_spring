package com.zlz9.springbootmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


/**
 * <h4>blog_admin</h4>
 * <p>线程池配置</p>
 *
 * @author : zlz
 * @date : 2022-10-02 08:13
 **/
@Configuration
@EnableAsync //开启多线程
public class ThreadPoolConfig {
    @Bean("taskExecutor")
    public Executor asyncServiceExecutor(){

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        设置核心线程数
        executor.setCorePoolSize(5);
//        设置线程活跃事件
        executor.setKeepAliveSeconds(60);
//        设置最大线程数
        executor.setMaxPoolSize(20);
//        设置默认线程名称
        executor.setThreadNamePrefix("博客平台");
//        配置队列大小
        executor.setQueueCapacity(Integer.MAX_VALUE);
//        等待所有线程结束在关闭线程
        executor.setWaitForTasksToCompleteOnShutdown(true);
//        执行初始化
        executor.initialize();
        return executor;
    }

}
