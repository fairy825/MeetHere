package com.meethere.dao;

import com.meethere.pojo.News;
import com.meethere.pojo.TimeSlot;
import com.meethere.pojo.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TimeSlotDAO extends JpaRepository<TimeSlot,Integer>{
    Page<TimeSlot> findByVenueAndBookingDate(Venue venue, Date bookingDate, Pageable pageable);
    List<TimeSlot> findByVenueAndBookingDate(Venue venue, Date bookingDate);
    List<TimeSlot> findByBookingDate(Date bookingDate);
}
