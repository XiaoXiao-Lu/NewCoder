package com.kinnon;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Kinnon
 * @create 2022-09-11 17:24
 */
@SpringBootTest
@Slf4j
public class ThreadPoolTest {

    //spring普通线程池
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    //spring普通任务线程池
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    //jdk普通线程池
    private ExecutorService excutorService = Executors.newFixedThreadPool(5);

    //jdk可执行任务线程池
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);



    private void sleep(long m){
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void excutorTest(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.warn("this is debug");
            }
        };

        for (int i = 0; i < 10; i++) {
            excutorService.submit(task);
            System.out.println("www");
        }

        sleep(5000);
    }


    @Autowired
    private Scheduler scheduler;

    @Test
    public void delete(){
        try {
            scheduler.deleteJob(new JobKey("alphaJob","alphaJobGroup"));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }



}
