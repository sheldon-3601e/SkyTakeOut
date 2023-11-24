package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName MyTask
 * @Author 26483
 * @Date 2023/11/24 8:27
 * @Version 1.0
 * @Description TODO
 */
@Component
@Slf4j
public class MyTask {

    @Scheduled(cron = "0/5 * * * * ? ")
    public void executeTask() {
      // log.info("定时任务");
    }

}
