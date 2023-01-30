package com.xdclass.shop.repository;

import com.xdclass.shop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Daniel
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    void deleteByOrderId(Integer orderId);
}
