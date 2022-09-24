package xdclass_springboot.demo;

import net.xdclass.demo.XdclassApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;



/**
 * 功能描述：测试mockmvc类
 *
 * <p> 创建时间：Apr 24, 2018 10:01:12 PM </p> 
 * <p> 作者：小D课堂</p>
 */
@RunWith(SpringRunner.class)  //底层用junit  SpringJUnit4ClassRunner
@SpringBootTest(classes={XdclassApplication.class}) //启动整个springboot工程
@AutoConfigureMockMvc 
public class MockMvcTestDemo {

	
	@Autowired
	private MockMvc mockMvc;
	
	
	
	@Test
	public void apiTest() throws Exception {
		
		MvcResult mvcResult =  mockMvc.perform( MockMvcRequestBuilders.get("/test/home_xxx") ).
				andExpect( MockMvcResultMatchers.status().isOk() ).andReturn();
		int status = mvcResult.getResponse().getStatus();
		System.out.println(status);
		
	}
	
}
