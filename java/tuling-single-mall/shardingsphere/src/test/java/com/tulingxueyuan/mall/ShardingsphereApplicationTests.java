package com.tulingxueyuan.mall;

import com.tulingxueyuan.mall.modules.ums.model.UmsMenu;
import com.tulingxueyuan.mall.modules.ums.model.UmsRole;
import com.tulingxueyuan.mall.modules.ums.service.UmsAdminService;
import com.tulingxueyuan.mall.modules.ums.service.UmsRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ShardingsphereApplicationTests {
	@Autowired
	private UmsAdminService adminService;
	@Autowired
	UmsRoleService umsRoleService;

	@Test
	void contextLoads() {
		List<UmsRole> umsRoles = adminService.getRoleList(8L);
		umsRoles.forEach((srt)->{
			System.out.println(srt);
		});
	}
	@Test
	void  umsRoleService(){
		List<UmsMenu> list = umsRoleService.getMenuList(8L);
		list.forEach((srt)->{
			System.out.println(srt);
		});
	}
}
