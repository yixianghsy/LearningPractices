package com.xdclass.shop.repository;

import com.xdclass.shop.model.Remember;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author daniel
 */
public interface RememberRepository extends JpaRepository<Remember, String> {
}
