package com.meethere.dao;

import com.meethere.pojo.District;
import com.meethere.pojo.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueDAO extends JpaRepository<Venue,Integer>{
    Page<Venue> findByDistrict(District district, Pageable pageable);
    Venue findByName(String name);
    List<Venue> findByDistrict(District district);
    List<Venue> findByDistrictOrderById(District district);
    List<Venue> findByNameLike(String keyword);
}
