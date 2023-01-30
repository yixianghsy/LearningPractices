package com.xdclass.shop.repository;

import com.xdclass.shop.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Daniel
 */
@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
}
