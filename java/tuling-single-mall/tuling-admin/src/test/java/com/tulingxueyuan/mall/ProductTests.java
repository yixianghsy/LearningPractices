package com.tulingxueyuan.mall;

import com.tulingxueyuan.mall.dto.RelationAttrInfoDTO;
import com.tulingxueyuan.mall.modules.pms.mapper.PmsProductAttributeCategoryMapper;
import com.tulingxueyuan.mall.modules.pms.mapper.PmsProductAttributeMapper;
import com.tulingxueyuan.mall.modules.pms.service.PmsProductAttributeService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProductTests {
    @Autowired
    PmsProductAttributeCategoryMapper mapper;
    @Autowired
    PmsProductAttributeService productAttributeService;
    @Test
    public void test01(){
        System.out.println(mapper.getListWithAttr());
    }
    @Test
    public  void test02 (){
        List<RelationAttrInfoDTO> cid = productAttributeService.getRelationAttrInfoByCid(24L);
        System.out.println(cid);

    }
}

