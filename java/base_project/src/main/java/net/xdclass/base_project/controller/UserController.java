package net.xdclass.base_project.controller;


import java.util.Date;

import net.xdclass.base_project.domain.JsonData;
import net.xdclass.base_project.domain.User;
import net.xdclass.base_project.mapper.UserMapper;
import net.xdclass.base_project.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *@作者 小D课堂  小D
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;
	
	
	/**
	 * 功能描述: user 保存接口
	 * @return
	 */
	@GetMapping("add")
	public Object add(){
		
		User user = new User();
		user.setAge(11);
		user.setCreateTime(new Date());
		user.setName("xdclass");
		user.setPhone("10010000");
		int id = userService.add(user);
		
       return JsonData.buildSuccess(id);
	}
	
	
	
	/**
	 * 功能描述：查找全部用户
	 * @return
	 */
	@GetMapping("findAll")
	public Object findAll(){
		
       return JsonData.buildSuccess(userMapper.getAll());
	}
	
	
	
	@GetMapping("find_by_id")
	public Object findById(long id){
       return JsonData.buildSuccess(userMapper.findById(id));
	}
	
	
	@GetMapping("del_by_id")
	public Object delById(long id){
	userMapper.delete(id);
       return JsonData.buildSuccess();
	}
	
	@GetMapping("update")
	public Object update(String name,int id){
		User user = new User();
		user.setName(name);
		user.setId(id);
		userMapper.update(user);
	    return JsonData.buildSuccess();
	}
	
	
	
	
	
	
	//测试事务
	@GetMapping("add_account")
	public Object addAccount(){
		int id = userService.addAccount();
	    return JsonData.buildSuccess(id);
	}
	
	
	
	
}
