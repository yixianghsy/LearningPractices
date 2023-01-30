package com.xdclass.couponapp;

import com.alibaba.fastjson.JSON;
import com.xdclass.couponapp.domain.TCouponExample;
import com.xdclass.couponapp.service.CouponService;
import com.xdclass.couponapp.domain.TCoupon;
import com.xdclass.couponapp.mapper.TCouponMapper;
import com.xdclass.couponserviceapi.dto.CouponDto;
import com.xdclass.couponserviceapi.dto.UserCouponDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CouponAppApplication.class)
public class CouponAppApplicationTests {


    @Resource
    private CouponService couponService;

    @Resource
    private TCouponMapper tCouponMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void contextLoads() {
       couponService.updateCoupon("1_1");
        couponService.updateCoupon("1_2");
        couponService.updateCoupon("1_4");
        couponService.updateCoupon("1_9");
        couponService.updateCoupon("1_200010");
        System.err.println("hello world");
    }

    @Test
    public void queryCouponNotice() {
        String ret = JSON.toJSONString(couponService.queryCouponNotice());
        System.err.println(ret);
    }   //本地maven ==》 nexus


    @Test
    public void contextLoads1() {
        System.err.println(JSON.toJSONString(couponService.queryCouponList()));
    }

    @Test
    public void insert(){
        for(int i=0;i<100000;i++){
            TCoupon tCoupon = new TCoupon();
            tCoupon.setAchieveAmount(500);
            tCoupon.setReduceAmount(20);
            //tCoupon.setCreatetime(new Date());
            tCoupon.setCode(UUID.randomUUID().toString());
            tCoupon.setPicUrl("1.jpg");
            tCoupon.setStatus(0);
            //tCoupon.setStock(100L);
            tCoupon.setTitle("测试coupon");
            tCouponMapper.insert(tCoupon);
        }
    }

    @Test
    public void delete(){
        tCouponMapper.deleteByPrimaryKey(7);
    }

    @Test
    public void update(){
        TCouponExample example = new TCouponExample();
        example.createCriteria().andStatusEqualTo(0)
                .andIdGreaterThan(100);
        List<TCoupon> tCoupons =  tCouponMapper.selectByExample(example);
        tCoupons.stream().forEach(tCoupon -> {
            Random rand = new Random();
            int MAX = 100000;
            int MIN = 0;
            long randNumber = rand.nextInt(MAX - MIN + 1) + MIN;
            tCoupon.setStartTime(new Date(1532083598000L+randNumber*1000));
            tCoupon.setEndTime(new Date(1532083598000L+100000*1000+randNumber*1000));
            tCouponMapper.updateByPrimaryKeySelective(tCoupon);
        });
    }


    @Test
    public void select(){
        // select * from t_coupon where code = "00415d96-49bd-4cce-83e3-08302b9aa084" and status=0 and achieve_amount between (100,1000) and title not like '%111%';
        TCouponExample example = new TCouponExample();
        example.createCriteria().andCodeEqualTo("00415d96-49bd-4cce-83e3-08302b9aa084").andStatusEqualTo(0)
                .andAchieveAmountBetween(100,1000).andTitleNotLike("111");
        List<TCoupon> tCoupon =  tCouponMapper.selectByExample(example);
        System.err.println(tCoupon);
    }

    @Test
    public void testquery(){
        List<CouponDto> tCoupon = couponService.getCouponList();
        System.out.println(tCoupon);
    }

    @Test
    public void testSaveUserCoupon(){
        UserCouponDto dto = new UserCouponDto();
        dto.setUserId(1234);
        dto.setCouponId(1);
        dto.setOrderId(10086);
        System.err.println(couponService.saveUserCoupon(dto));
    }


    @Test
    public void testUserCouponList(){
        System.err.println(JSON.toJSONString(couponService.userCouponList(1)));
    }


    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("name","daniel");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }

    @Test
    public void testSortSet(){
        redisTemplate.opsForZSet().add("mySet","one",1);
        redisTemplate.opsForZSet().add("mySet","two",2);
        redisTemplate.opsForZSet().add("mySet","three",3);
        redisTemplate.opsForZSet().add("mySet","four",4);
        redisTemplate.opsForZSet().remove("mySet","four");
        System.out.println(JSON.toJSONString(redisTemplate.opsForZSet().range("mySet",0,-1)));
    }
}
