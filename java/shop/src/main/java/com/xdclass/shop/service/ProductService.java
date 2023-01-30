/**
 *
 */
package com.xdclass.shop.service;

import com.xdclass.shop.common.Page;
import com.xdclass.shop.model.Product;
import com.xdclass.shop.model.ProductType;
import com.xdclass.shop.repository.ProductRepository;
import com.xdclass.shop.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Daniel
 */
@Service
@Transactional
public class ProductService {

    @Autowired
    ProductTypeRepository productTypeDao;

    @Autowired
    ProductRepository productDao;

    public void saveType(ProductType type) {
        productTypeDao.save(type);
    }

    public List<ProductType> findType() {
        return productTypeDao.findAll();
    }

    public void save(Product product) {
        productDao.save(product);
    }

    public Product findById(Integer id) {
        return productDao.findOne(id);
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public List<Product> findNew() {
        return productDao.findByOrderByCreateTimeDesc();
    }

    public List<Product> findOld() {
        return productDao.findByOrderByCreateTimeAsc();
    }

    public List<Product> findPop() {
        return productDao.findPopProducts();
    }

    public List<Product> findProducts(Page<Product> page) {
        List<Product> products = productDao.findAll(page.getPageable()).getContent();
        page.setResult(products);
        page.setTotalCount(productDao.count());
        return page.getResult();
    }
}
