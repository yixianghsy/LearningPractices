package com.mall.portal.dao;

import com.mall.portal.dto.ProductDetailDTO;

public interface ProductDao {
    ProductDetailDTO getProductDetail(Long id);
}
