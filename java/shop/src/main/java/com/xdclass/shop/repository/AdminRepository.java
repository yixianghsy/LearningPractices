package com.xdclass.shop.repository;

import com.xdclass.shop.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Daniel
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    public Admin findByUsernameAndPassword(String username, String password);
}
