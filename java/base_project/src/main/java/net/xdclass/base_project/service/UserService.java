package net.xdclass.base_project.service;

import net.xdclass.base_project.domain.User;

public interface UserService {

	public int add(User user);
	
	
	
	/**
	 * 功能描述：测试事务
	 * @return
	 */
	public int addAccount();
	
}
