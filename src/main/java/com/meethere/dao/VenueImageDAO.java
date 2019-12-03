package com.meethere.dao;

import com.meethere.pojo.District;
import com.meethere.pojo.Venue;
import com.meethere.pojo.VenueImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueImageDAO extends JpaRepository<VenueImage,Integer>{
    List<VenueImage> findByVenue(Venue venue);
}
