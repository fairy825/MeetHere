package com.meethere.dao;

import com.meethere.pojo.Booking;
import com.meethere.pojo.TimeSlot;
import com.meethere.pojo.User;
import com.meethere.pojo.Venue;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.awt.print.Book;
import java.util.List;

public interface BookingDAO extends JpaRepository<Booking,Integer>, JpaSpecificationExecutor<Booking> {
    Page<Booking> findByUser(User user, Pageable pageable);
    Page<Booking> findByUserAndStateNotOrderByIdDesc(User user, String state, Pageable pageable);
    Page<Booking> findByVenue(Venue venue, Pageable pageable);
    Page<Booking> findByVenueLikeAndUserLikeAndState(Venue venue, User user, String state, Pageable pageable);
    Integer countByVenueAndStateNot(Venue venue,String state);
    Booking findByUserAndTimeSlot(User user,TimeSlot timeSlot);

}
