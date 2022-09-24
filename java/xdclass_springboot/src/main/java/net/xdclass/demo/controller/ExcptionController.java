package net.xdclass.demo.controller;

import java.util.Date;

import net.xdclass.demo.domain.User;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述：异常测试
 *
 * <p> 创建时间：Apr 22, 2018 11:22:29 PM </p> 
 * <p> 作者：小D课堂</p>
 */
@RestController

public class ExcptionController {

	 
	
	@RequestMapping(value = "/api/v1/test_ext")  
	public Object index() {
		int i= 1/0;
		return new User(11, "sfsfds", "1000000", new Date());
	}

	
	
}
