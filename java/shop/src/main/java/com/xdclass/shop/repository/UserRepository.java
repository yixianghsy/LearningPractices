package com.xdclass.shop.repository;

import com.xdclass.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User DAO
 *
 * @author Daniel
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsernameAndPassword(String username, String password);

    public User findByUsername(String username);
}
