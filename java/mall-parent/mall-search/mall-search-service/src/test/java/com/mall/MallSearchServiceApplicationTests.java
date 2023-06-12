package com.mall;

import com.mall.search.service.SearchItemService;
import com.mall.utils.E3Result;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MallSearchServiceApplicationTests {
    private SearchItemService searchItemService;
    @Test
    void contextLoads() {

        E3Result e3Result = searchItemService.importAllItems();
        System.out.println(e3Result);
    }

}
