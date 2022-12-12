package com.tulingxueyuan.mall;

import com.tulingxueyuan.mall.common.exception.Asserts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class TulingPortalApplicationTests {
	@Autowired
	PasswordEncoder passwordEncoder;
	@Test
	void contextLoads() {

	}
	@Test
	void  loginTest(){
		System.out.println(passwordEncoder.matches("123456","$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG"));
	}
}
