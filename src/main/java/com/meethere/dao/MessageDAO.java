package com.meethere.dao;

import com.meethere.pojo.Admin;
import com.meethere.pojo.Booking;
import com.meethere.pojo.Message;
import com.meethere.pojo.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageDAO extends JpaRepository<Message,Integer>{
    Page<Message> findByVenue(Venue venue, Pageable pageable);
    Page<Message> findByVenueAndState(Venue venue, String state, Pageable pageable);
    Integer countByVenueAndState(Venue venue,String state);
    Message findByBooking(Booking booking);
}
