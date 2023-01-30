package com.mengxuegu;

import com.mengxuegu.web.entities.SysPermission;
import com.mengxuegu.web.entities.SysRole;
import com.mengxuegu.web.entities.SysUser;
import com.mengxuegu.web.service.SysPermissionService;
import com.mengxuegu.web.service.SysRoleService;
import com.mengxuegu.web.service.SysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestWebApplication {

    @Autowired
    SysUserService sysUserService;

    @Test
    public void testSysUser() {
//        List<SysUser> list = sysUserService.list();
//        System.out.println("list:" + list);

        SysUser user = sysUserService.findByUsername("admin");
        System.out.println("user: " + user);
    }


    @Autowired
    SysRoleService sysRoleService;

    @Test
    public void testSysRole(){
        SysRole role = sysRoleService.getById(9);
        System.out.println("role:" + role);
    }

    @Autowired
    SysPermissionService sysPermissionService;

    @Test
    public void testSysPer(){
//        SysPermission permission = sysPermissionService.getById(18);
//        System.out.println(permission);
        List<SysPermission> permissions = sysPermissionService.findByUserId(9L);
        System.out.println("permissions:" + permissions.size());
    }


}
