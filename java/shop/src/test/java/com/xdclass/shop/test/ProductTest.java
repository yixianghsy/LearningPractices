package com.xdclass.shop.test;

import com.xdclass.shop.model.Admin;
import com.xdclass.shop.model.Product;
import com.xdclass.shop.repository.AdminRepository;
import com.xdclass.shop.service.ProductService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author daniel
 * @version 2020/03/14
 */
public class ProductTest extends BaseTest {

    @Autowired
    ProductService productService;

    @Autowired
    private AdminRepository adminDao;

    @Test
    public void testAddProduct() {
        List<Admin> adminList = adminDao.findAll();
        Admin admin = null;
        if (CollectionUtils.isEmpty(adminList)) {
            admin = new Admin();
            admin.setUsername("product_test");
            admin.setPassword("123456");
            adminDao.save(admin);
        } else {
            admin = adminList.get(0);
        }

        Product product = new Product();
        product.setCreateTime(new Date());
        product.setCode("KDF-SD1-200");
        product.setStock(200l);
        product.setModel("400CM");
        product.setPoint(200);
        product.setTitle("3T Pro 铝坐管");

        Product product2 = new Product();
        product2.setCreateTime(new Date());
        product2.setCode("BMC-SLR01-54");
        product2.setStock(200l);
        product2.setModel("54");
        product2.setPoint(19999);
        product2.setTitle("MBC SLR01 车队版碳纤维车架");

        productService.save(product2);
        productService.save(product);

    }

    @Test
    public void testFindNew() {
        List<Product> productList = productService.findOld();
        for (Product product : productList) {
            System.out.println(product.toString());
        }
    }

}
