package net.xdclass.db;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.DemoApplication;
import net.xdclass.mapper.AdConfigMapper;
import net.xdclass.mapper.ProductOrderMapper;
import net.xdclass.model.AdConfigDO;
import net.xdclass.model.ProductOrderDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * 小滴课堂,愿景：让技术不再难学
 *
 * @Description
 * @Author 二当家小D
 * @Remark 有问题直接联系我，源码-笔记-技术交流群
 * @Version 1.0
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@Slf4j
public class DbTest {

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Autowired
    private AdConfigMapper adConfigMapper;

    @Test
    public void testSaveProductOrder(){

        Random random = new Random();
        for(int i=0; i<20;i++){
            ProductOrderDO productOrderDO = new ProductOrderDO();
            productOrderDO.setCreateTime(new Date());
            productOrderDO.setNickname("自定义水平分库分表-小滴课堂 i="+i);
            productOrderDO.setOutTradeNo(UUID.randomUUID().toString().substring(0,32));
            productOrderDO.setPayAmount(100.00);
            productOrderDO.setState("PAY");

            productOrderDO.setUserId( Long.valueOf(random.nextInt(50)) );

            productOrderMapper.insert(productOrderDO);

        }

    }



    @Test
    public void testSaveAdConfig(){

        AdConfigDO adConfigDO = new AdConfigDO();
        adConfigDO.setConfigKey("banner");
        adConfigDO.setConfigValue("xdclass.net");
        adConfigDO.setType("ad");

        adConfigMapper.insert(adConfigDO);

    }




    @Test
    public void testBingding(){

        List<Object> list = productOrderMapper.listProductOrderDetail();
        System.out.println(list);
    }


    /**
     * 有分片键
     */
    @Test
    public void testPartitionKeySelect(){
        productOrderMapper.selectList(new QueryWrapper<ProductOrderDO>().eq("id",1464129579089227778L));
        //productOrderMapper.selectList(new QueryWrapper<ProductOrderDO>().in("id",Arrays.asList(1464129579089227778L,1464129582369173506L,1464129583140925441L)));
    }


    /**
     * 无有分片键
     */
    @Test
    public void testNoPartitionKeySelect(){
        productOrderMapper.selectList(new QueryWrapper<ProductOrderDO>().eq("out_trade_no","2cc08fb8-7e77-4973-b408-7c68925b"));
        //productOrderMapper.selectList(new QueryWrapper<ProductOrderDO>().in("out_trade_no",Arrays.asList("2cc08fb8-7e77-4973-b408-7c68925b")));
    }




    /**
     * 有分片键
     */
    @Test
    public void testPartitionKeyDel(){
        //productOrderMapper.delete(new QueryWrapper<ProductOrderDO>().eq("id",1464129579089227778L));
        //productOrderMapper.delete(new QueryWrapper<ProductOrderDO>().in("id",Arrays.asList(1464129579089227778L,1464129582369173506L,1464129583140925441L)));
    }


    /**
     * 无有分片键
     */
    @Test
    public void testNoPartitionKeyDel(){
        //productOrderMapper.delete(new QueryWrapper<ProductOrderDO>().eq("out_trade_no","2cc08fb8-7e77-4973-b408-7c68925b"));
        productOrderMapper.delete(new QueryWrapper<ProductOrderDO>().in("out_trade_no",Arrays.asList("2cc08fb8-7e77-4973-b408-7c68925b")));
    }


}
