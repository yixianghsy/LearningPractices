package com.xdclass.couponapp.service.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class HelloSchedule {

    //@Scheduled(cron = "* 0/1 * * * ?") 秒分时日月年
    //@Scheduled(cron = "* * 0/1 * * ?")   linux unix  rsync crontab
     @Scheduled(cron = "0/5 * * * * ?")
     public void hello(){
         System.out.println("enter hello job!");
     }

}
