package com.xdclass.shop.repository;

import com.xdclass.shop.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author daniel
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {


}
