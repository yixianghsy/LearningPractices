package com.example.redistest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
class RedisTestApplicationTests {

    @Test
    void contextLoads() {
        // 连接Redis服务器
        Jedis jedis = new Jedis("localhost", 6379);
        // 权限认证，也就是我们redis-cli设置的密码
        jedis.auth("root");
        // 测试是否连接成功
        // CentOS下查看防火墙运行访问的端口号：firewall-cmd --list-ports
        // 开启6379端口号：firewall-cmd --zone=public --add-port=6379/tcp --permanent
        // 重启防火墙：firewall-cmd --reload
        System.out.println(jedis.ping());
    }

    /*
     * 测试String，redis中有哪些命令，jedis中就有哪些方法
     */
    @Test
    public void test1() {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.auth("root");
        jedis.set("strName", "cmy");
        String strName = jedis.get("strName");
        System.out.println(strName);

        jedis.close();
    }

    /*
     * redis是为了减轻数据库的访问压力
     * 如果某key存在，就在redis中查询，否则就去数据库中查询
     * 并将查询出的数据存入到redis
     */
    @Test
    public void test2() {
        Jedis jedis = new Jedis("192.168.222.130", 6379);
        jedis.auth("root");

        String key = "appName"; // key的名称
        if (jedis.exists(key)) {
            String s = jedis.get(key);
            System.out.println("从redis中查询到的");
        } else {
            String s = "应用名";
            jedis.set(key, s);
            System.out.println("从数据库中查询的");
        }

        jedis.close();
    }

}
