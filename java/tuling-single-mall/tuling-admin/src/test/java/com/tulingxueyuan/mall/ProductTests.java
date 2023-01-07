package com.tulingxueyuan.mall;

import com.tulingxueyuan.mall.dto.ProductAttributeCateDTO;
import com.tulingxueyuan.mall.dto.RelationAttrInfoDTO;
import com.tulingxueyuan.mall.modules.pms.mapper.PmsProductAttributeCategoryMapper;
import com.tulingxueyuan.mall.modules.pms.mapper.PmsProductAttributeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ProductTests {
    @Autowired
    PmsProductAttributeCategoryMapper mapper;
    @Autowired
    PmsProductAttributeMapper productAttributeMapper;
    @Test
    public void test01(){
        List<ProductAttributeCateDTO> listWithAttr = mapper.getListWithAttr();
        listWithAttr.forEach((srt)->{
            System.out.println(srt);
        });
        System.out.println(listWithAttr);
    }

    @Test
    public  void test02 (){
        List<RelationAttrInfoDTO> cid = productAttributeMapper.getRelationAttrInfoByCid(24L);
        System.out.println(cid);

    }
}