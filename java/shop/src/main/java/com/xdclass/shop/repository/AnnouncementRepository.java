package com.xdclass.shop.repository;

import com.xdclass.shop.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Daniel
 */
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
}
