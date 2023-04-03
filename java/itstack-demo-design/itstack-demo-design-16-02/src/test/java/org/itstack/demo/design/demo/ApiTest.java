package org.itstack.demo.design.demo;

import com.alibaba.fastjson.JSON;
import org.itstack.demo.design.mediator.Resources;
import org.itstack.demo.design.mediator.SqlSession;
import org.itstack.demo.design.mediator.SqlSessionFactory;
import org.itstack.demo.design.mediator.SqlSessionFactoryBuilder;
import org.itstack.demo.design.po.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.List;

/**
 * 中介者模式要解决的就是复杂功能应⽤之间的᯿复调⽤，在这中间添加⼀层中介者包装服务，对外提供
 * 简单、通⽤、易扩展的服务能⼒
 */
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_queryUserInfoById() {
        String resource = "mybatis-config-datasource.xml";
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);

            SqlSession session = sqlMapper.openSession();
            try {
                User user = session.selectOne("org.itstack.demo.design.dao.IUserDao.queryUserInfoById", 1L);
                logger.info("测试结果：{}", JSON.toJSONString(user));
            } finally {
                session.close();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test_queryUserList() {
        String resource = "mybatis-config-datasource.xml";
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);

            SqlSession session = sqlMapper.openSession();
            try {
                User req = new User();
                req.setAge(18);
                List<User> userList = session.selectList("org.itstack.demo.design.dao.IUserDao.queryUserList", req);
                logger.info("测试结果：{}", JSON.toJSONString(userList));
            } finally {
                session.close();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
