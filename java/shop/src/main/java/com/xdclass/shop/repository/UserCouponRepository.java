package com.xdclass.shop.repository;

import com.xdclass.shop.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author daniel
 */
@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Integer> {
    List<UserCoupon> findByUserId(Integer userId);

    List<UserCoupon> findByUserCouponCode(String userCouponCode);
}
