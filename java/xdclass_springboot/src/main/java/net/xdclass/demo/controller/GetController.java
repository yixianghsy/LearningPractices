package net.xdclass.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.xdclass.demo.domain.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//测试http协议的get请求
@RestController
public class GetController {

	
	private Map<String,Object> params = new HashMap<>();
	
	/**
	 * 功能描述：测试restful协议，从路径中获取字段
	 * @param cityId
	 * @param userId
	 * @return
	 */
//	@RequestMapping(path = "/{city_id}/{user_id}", method = RequestMethod.GET)
//	public Object findUser(@PathVariable("city_id") String cityId,
//			@PathVariable("user_id") String userId ){
//		params.clear();
//		
//		params.put("cityId", cityId);
//		params.put("userId", userId);
//		
//		return params;
//		
//	}
	
	
	/**
	 * 功能描述：测试GetMapping
	 * @param from
	 * @param size
	 * @return
	 */
	@GetMapping(value="/v1/page_user1")
	public Object pageUser(int  from, int size ){
		params.clear();
		params.put("from", from);
		params.put("size", size);
		
		return params;
		
	}
	
	
	
	/**
	 * 功能描述：默认值，是否必须的参数
	 * @param from
	 * @param size
	 * @return
	 */
	@GetMapping(value="/v1/page_user2")
	public Object pageUserV2(@RequestParam(defaultValue="0",name="page") int  from, int size ){
		
		params.clear();
		params.put("from", from);
		params.put("size", size);
		
		return params;
		
	}
	
	
	
	
	/**
	 * 功能描述：bean对象传参
	 * 注意：1、注意需要指定http头为 content-type为application/json
	 * 		2、使用body传输数据
	 * @param user
	 * @return
	 */
	@RequestMapping("/v1/save_user")
	public Object saveUser(@RequestBody User user){
		params.clear();
		params.put("user", user);
		return params;	
	}
	
	
	
	
	/**
	 * 功能描述：测试获取http头信息
	 * @param accessToken
	 * @param id
	 * @return
	 */
	@GetMapping("/v1/get_header")
	public Object getHeader(@RequestHeader("access_token") String accessToken, String id){
		params.clear();
		params.put("access_token", accessToken);
		params.put("id", id);
		return params;	
	}
	
	
	
	
	@GetMapping("/v1/test_request")
	public Object testRequest(HttpServletRequest request){
		params.clear();
		String id = request.getParameter("id");
		params.put("id", id);
		return params;	
	}
	
	
}
