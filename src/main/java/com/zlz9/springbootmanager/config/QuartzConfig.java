package com.zlz9.springbootmanager.config;

import com.zlz9.springbootmanager.quartz.TaskJobOne;
import com.zlz9.springbootmanager.quartz.TaskJobSearch;
import com.zlz9.springbootmanager.utils.CronUtil;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.util.Date;


/**
 * 开启定时任务持久化存储到数据库
 */
@Configuration
public class QuartzConfig {
    private static final String LIKE_TASK_IDENTITY = "LikeTaskQuartz";
    // 配置定时任务1的任务实例
    @Bean(name = "firstJobDetail")
    public MethodInvokingJobDetailFactoryBean firstJobDetail(TaskJobOne taskJobOne) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(taskJobOne);
        // 需要执行的方法
        jobDetail.setTargetMethod("testJobOneMethod");
        return jobDetail;
    }
    // 配置触发器1
    @Bean(name = "firstTrigger")
    public CronTriggerFactoryBean firstTrigger(JobDetail firstJobDetail) {
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setJobDetail(firstJobDetail);
        // 设置任务启动延迟
        trigger.setStartDelay(0);
        // 设置定时任务启动时间
        trigger.setStartTime(new Date());
        // 每5秒执行一次
//        trigger.setRepeatInterval(5000);
        trigger.setCronExpression("0 0 3 1 * ?");
//        trigger.setCronExpression("0 0/1 * * * ?");
//        trigger.setCronExpression("* 1 * * * ? *");
        return trigger;
    }


    // 配置定时任务删除搜素榜单的任务实例
    @Bean(name = "searchTopJobDetail")
    public MethodInvokingJobDetailFactoryBean delSearch(TaskJobSearch taskJobSearch) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        // 是否并发执行
        jobDetail.setConcurrent(false);
        // 为需要执行的实体类对应的对象
        jobDetail.setTargetObject(taskJobSearch);
        // 需要执行的方法
        jobDetail.setTargetMethod("delSearch");
        return jobDetail;
    }
    // 配置触发器1
    @Bean(name = "searchTopJobDetail")
    public CronTriggerFactoryBean delTrigger(JobDetail searchTopJobDetail) {
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setJobDetail(searchTopJobDetail);
        // 设置任务启动延迟
        trigger.setStartDelay(0);
        // 设置定时任务启动时间
        trigger.setStartTime(new Date());
        // 每5秒执行一次
//        trigger.setRepeatInterval(5000);
        trigger.setCronExpression("0 0 3 1 * ?");
//        trigger.setCronExpression("0 0/1 * * * ?");
//        trigger.setCronExpression("* 1 * * * ? *");
        return trigger;
    }



    @Bean
    public JobDetail quartzDetail(){
        return JobBuilder.newJob(CronUtil.class).withIdentity(LIKE_TASK_IDENTITY).storeDurably().build();
    }

    @Bean
    public Trigger quartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(20)  //设置时间周期单位秒
                .withIntervalInHours(2)  //两个小时执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzDetail())
                .withIdentity(LIKE_TASK_IDENTITY)
                .withSchedule(scheduleBuilder)
                .build();
    }
}