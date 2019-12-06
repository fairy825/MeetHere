package com.meethere.service.impl;

import com.meethere.dao.BookingDAO;
import com.meethere.dao.NewsDAO;
import com.meethere.pojo.*;
import com.meethere.service.*;
import com.meethere.util.Page4Navigator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingDAO bookingDAO;
    @Autowired
    UserService userService;
    @Autowired
    VenueService venueService;
    @Autowired
    TimeSlotService timeSlotService;

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<Booking> listBookingsByUser(User user, int start, int size, int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page pageFromJPA =bookingDAO.findByUserAndStateNotOrderByIdDesc(user,BookingService.delete,pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<Booking> searchByUser(String keyword, int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        User user = userService.findByName(keyword);
        Page pageFromJPA =bookingDAO.findByUser(user,pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<Booking> searchByVenue(String keyword, int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Venue venue = venueService.findByName(keyword);
        Page pageFromJPA =bookingDAO.findByVenue(venue,pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<Booking> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA = bookingDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Booking searchByUserAndTimeslot(Booking booking){
        Example<Booking> example = Example.of(booking);
        Booking booking1 = bookingDAO.findOne(example);
        return (booking1==null)||(booking1.getState().equalsIgnoreCase(cancelled)) ? null : booking1;
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<Booking> searchWithoutDelete(Booking booking, int start, int size, int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA =null;
        Booking b = new Booking();
        b.setId(null);
        if(StringUtils.isBlank(booking.getState())) booking.setState(null);
        b.setState(booking.getState());
        b.setUser(userService.findByName(booking.getUser().getName()));
        if(booking.getVenue()!=null)
            b.setVenue(venueService.findByName(booking.getVenue().getName()));
        else b.setVenue(null);
        Example<Booking> example = Example.of(b);
        List<Booking> list = new ArrayList<>();
        List<Booking> list1 = bookingDAO.findAll(example,sort);
        for(Booking booking1:list1){
            if(!booking1.getState().equalsIgnoreCase(delete)){
                list.add(booking1);
            }
        }
        List<Booking> res = new ArrayList<>();
        int count = 0;
        int i = 0;
        if(booking.getTimeSlot()==null||booking.getTimeSlot().getBookingDate()==null) {
            for(i=start*size;i<(start+1)*size&&i<list.size();i++) {//start=0 size=2
                Booking booking1 =  list.get(i);
                res.add(booking1);
            }
            count = list.size();
        }
        else {
            List<TimeSlot> tsList = timeSlotService.findByBookingDate(booking.getTimeSlot().getBookingDate());
            for (Booking booking1 : list) {
                TimeSlot timeSlot = booking1.getTimeSlot();
                for (TimeSlot ts : tsList) {
                    if (timeSlot.getId() == ts.getId()) {
                        if(count>=start*size&&count<(start+1)*size){
                            res.add(booking1);
                        }
                        count++;
                        break;
                    }
                }
            }
        }
        pageFromJPA = new PageImpl<Booking>(res,pageable,count);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<Booking> search(Booking booking, int start, int size, int navigatePages){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA =null;
        Booking b = new Booking();
        b.setId(null);
        if(StringUtils.isBlank(booking.getState())) booking.setState(null);
        b.setState(booking.getState());
        b.setUser(userService.findByName(booking.getUser().getName()));
        if(booking.getVenue()!=null)
            b.setVenue(venueService.findByName(booking.getVenue().getName()));
        else b.setVenue(null);
        Example<Booking> example = Example.of(b);
        List<Booking> list = bookingDAO.findAll(example,sort);
        List<Booking> res = new ArrayList<>();
        int count = 0;
        int i = 0;
        if(booking.getTimeSlot()==null||booking.getTimeSlot().getBookingDate()==null) {
            for(i=start*size;i<(start+1)*size&&i<list.size();i++)//start=0 size=2
                res.add(list.get(i));
            count = list.size();
        }
        else {
            List<TimeSlot> tsList = timeSlotService.findByBookingDate(booking.getTimeSlot().getBookingDate());
            for (Booking booking1 : list) {
                TimeSlot timeSlot = booking1.getTimeSlot();
                for (TimeSlot ts : tsList) {
                    if (timeSlot.getId() == ts.getId()) {
                        if(count>=start*size&&count<(start+1)*size){
                            res.add(booking1);
                        }
                        count++;
                        break;
                    }
                }
            }
        }
        pageFromJPA = new PageImpl<Booking>(res,pageable,count);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void update(Booking booking){
    bookingDAO.save(booking);
}

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Booking get(int id){
        return bookingDAO.findOne(id);
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Integer total(Venue venue){
        return bookingDAO.countByVenueAndStateNot(venue,refused);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void saveBooking(Booking booking){
        bookingDAO.save(booking);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public List<Booking> findAll(){
        return bookingDAO.findAll();
    }

}