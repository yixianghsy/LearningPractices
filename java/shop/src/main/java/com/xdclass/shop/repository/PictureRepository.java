package com.xdclass.shop.repository;

import com.xdclass.shop.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Daniel
 */
@Repository
public interface PictureRepository extends JpaRepository<Picture, Integer> {
}
