package com.mall;

import com.mall.utils.FastDFSClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastDFSTest {

    @Test
    public void testFastDfsClient() throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient("E:\\tool\\IdeaProjects\\LearningPractices\\java\\e3-springboot\\e3-manager\\e3-manager-web\\src\\main\\resources\\conf\\fastdfs-client.conf");
        String file = fastDFSClient.uploadFile("C:\\Users\\27759\\Pictures\\Screenshots\\555.png");
        System.out.println(file);
    }

}
