package com.mall.sso;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest
class SsoServiceApplicationTests {

    @Test
    void contextLoads() {
        String  password = "123456";
        String s = DigestUtils.md5DigestAsHex(password.getBytes());
        System.out.println(s);
    }

}
