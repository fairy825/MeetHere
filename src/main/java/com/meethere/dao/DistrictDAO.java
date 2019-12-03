package com.meethere.dao;

import com.meethere.pojo.District;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictDAO extends JpaRepository<District,Integer>{
    Page<District> findByNameLike(String title, Pageable pageable);
}
