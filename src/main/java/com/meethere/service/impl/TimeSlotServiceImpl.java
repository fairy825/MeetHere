package com.meethere.service.impl;

import com.meethere.dao.NewsDAO;
import com.meethere.dao.TimeSlotDAO;
import com.meethere.pojo.News;
import com.meethere.pojo.TimeSlot;
import com.meethere.pojo.Venue;
import com.meethere.service.NewsService;
import com.meethere.service.TimeSlotService;
import com.meethere.service.VenueService;
import com.meethere.util.DateUtils;
import com.meethere.util.Page4Navigator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

    @Autowired
    TimeSlotDAO timeSlotDAO;
    @Autowired
    VenueService venueService;
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<TimeSlot> list(int vid, int date , int start, int size, int navigatePages) throws ParseException {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Venue venue = venueService.get(vid);
        Date dd = DateUtils.formatDate(date);
        Page pageFromJPA = timeSlotDAO.findByVenueAndBookingDate(venue,dd,pageable);

        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void saveTimeSlot(TimeSlot timeSlot) {
        timeSlotDAO.save(timeSlot);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void createByVenue(Venue venue,Integer date) throws ParseException {
        Integer seat = venue.getTotalSeat();
        Integer startTime = venue.getStartTime();
        Integer endTime = venue.getEndTime();
        Date dd = DateUtils.formatDate(date);
        for(int i = startTime;i<endTime;i++) {
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setSeat(seat);
            timeSlot.setVenue(venue);
            timeSlot.setBeginTime(i);
            timeSlot.setEndTime(i+1);
            timeSlot.setBookingDate(dd);
            saveTimeSlot(timeSlot);
        }
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public List<TimeSlot> findByBookingDate(Date date){
        return timeSlotDAO.findByBookingDate(date);
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public List<TimeSlot> list(int vid, int date) throws ParseException{
        Venue venue = venueService.get(vid);
        Date dd = DateUtils.formatDate(date);
        return timeSlotDAO.findByVenueAndBookingDate(venue,dd);
    }
    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void addNewTimeSlot(Integer date) throws ParseException {
        List<Venue> venueList = venueService.list();
        for(Venue venue : venueList)
            createByVenue(venue,date);
    }
    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void update(TimeSlot timeSlot){
        timeSlotDAO.save(timeSlot);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public TimeSlot get(int id){
        return timeSlotDAO.getOne(id);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void reduceSeat(TimeSlot timeSlot){
        timeSlot.setSeat(timeSlot.getSeat()-1);
    }


}
