package com.xdclass.shop.repository;

import com.xdclass.shop.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Daniel
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("from Order o where o.user.id=?1")
    Page<Order> findByUserId(Integer id, Pageable pageable);

    @Query("select count(o.id) from Order o where o.user.id=?1")
    long countByUserId(Integer userId);
}
