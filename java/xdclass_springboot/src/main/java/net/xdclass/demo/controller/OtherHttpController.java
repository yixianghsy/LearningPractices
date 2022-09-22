package net.xdclass.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

//测试http协议的post,del,put请求
@RestController
public class OtherHttpController {

	
	private Map<String,Object> params = new HashMap<>();
	
	
	
	
	/**
	 * 功能描述：测试PostMapping
	 * @param accessToken
	 * @param id
	 * @return
	 */
	@PostMapping("/v1/login")
	public Object login(String id, String pwd){
		params.clear();
		params.put("id", id);
		params.put("pwd", pwd);
		return params;	
	}
	
	
	@PutMapping("/v1/put")
	public Object put(String id){
		params.clear();
		params.put("id", id);
		return params;	
	}
	

	@DeleteMapping("/v1/del")
	public Object del(String id){
		params.clear();
		params.put("id", id);
		return params;	
	}
	
}
