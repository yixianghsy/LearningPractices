package net.xdclass.base_project.task;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 功能描述：定时任务业务类
 *
 * <p> 创建时间：Apr 30, 2018 10:21:48 AM </p> 
 *
 *@作者 小D课堂  小D
 */
@Component
public class TestTask {

//	
//	@Scheduled(fixedRateString="2000")//两秒执行一次
//	//@Scheduled(cron="*/2 * * * * *")
//	public void sum() throws InterruptedException{
//
//		Thread.sleep(4000L);
//		System.out.println("结束 当前时间:"+new Date());
//		
//	}
//	
//	
	
	
	//@Scheduled(cron="*/1 * * * * *")
	public void sum2(){
		System.out.println("cron 每秒 当前时间:"+new Date());
	}
	
	
	
	
	
}
