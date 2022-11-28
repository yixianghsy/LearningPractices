package com.tulingxueyuan.mall;

import com.tulingxueyuan.mall.modules.pms.service.PmsProductAttributeCategoryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PmsProductAttributeCategoryTest {
    @Autowired
    PmsProductAttributeCategoryService attributeCategoryService;
    @Test
    public void test01(){
        attributeCategoryService.getListWithAttr();
        System.out.println("test");
    }

}
