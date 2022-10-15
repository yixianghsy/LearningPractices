package net.xdclass.base_project.task;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

/**
 * 功能描述：异步任务业务类
 *
 * <p> 创建时间：Apr 30, 2018 11:25:15 PM </p> 
 *
 *@作者 小D课堂  小D
 */
@Component
//@Async
public class AsyncTask {

	
	public void task1() throws InterruptedException{
		long begin = System.currentTimeMillis();
		Thread.sleep(1000L);
		long end = System.currentTimeMillis();
		System.out.println("任务1耗时="+(end-begin));
	}
	
	
	public void task2() throws InterruptedException{
		long begin = System.currentTimeMillis();
		Thread.sleep(2000L);
		long end = System.currentTimeMillis();
		System.out.println("任务2耗时="+(end-begin));
	}
	
	
	public void task3() throws InterruptedException{
		long begin = System.currentTimeMillis();
		Thread.sleep(3000L);
		long end = System.currentTimeMillis();
		System.out.println("任务3耗时="+(end-begin));
	}
	
	
	//获取异步结果
	
	
	public Future<String> task4() throws InterruptedException{
		long begin = System.currentTimeMillis();
		Thread.sleep(2000L);
		long end = System.currentTimeMillis();
		System.out.println("任务4耗时="+(end-begin));
		return new AsyncResult<String>("任务4");
	}
	
	
	public Future<String> task5() throws InterruptedException{
		long begin = System.currentTimeMillis();
		Thread.sleep(3000L);
		long end = System.currentTimeMillis();
		System.out.println("任务5耗时="+(end-begin));
		return new AsyncResult<String>("任务5");
	}
	
	public Future<String> task6() throws InterruptedException{
		long begin = System.currentTimeMillis();
		Thread.sleep(1000L);
		long end = System.currentTimeMillis();
		System.out.println("任务6耗时="+(end-begin));
		return new AsyncResult<String>("任务6");
	}
	
	
	
	
	
}
