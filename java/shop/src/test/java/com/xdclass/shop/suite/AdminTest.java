package com.xdclass.shop.suite;

import com.xdclass.shop.model.Admin;
import com.xdclass.shop.service.AdminService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author daniel
 * @version 2020/03/14
 */
public class AdminTest extends BaseTest {

    @Autowired
    AdminService adminService;

    @Test
    public void testAddAdmin() {
        Admin admin = new Admin();
        admin.setUsername("xdclass");
        admin.setPassword("123456");
        adminService.save(admin);
    }

}
