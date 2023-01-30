package com.xdclass.shop.repository;

import com.xdclass.shop.model.User;
import com.xdclass.shop.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Daniel
 */
@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {
    List<UserAddress> findByUser(User user);
}
