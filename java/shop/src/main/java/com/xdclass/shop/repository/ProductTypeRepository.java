package com.xdclass.shop.repository;

import com.xdclass.shop.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Daniel
 */
@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {
}
