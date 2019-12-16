package com.meethere.service.impl;

import com.meethere.dao.MessageDAO;
import com.meethere.dao.NewsDAO;
import com.meethere.pojo.Booking;
import com.meethere.pojo.Message;
import com.meethere.pojo.News;
import com.meethere.pojo.Venue;
import com.meethere.service.BookingService;
import com.meethere.service.MessageService;
import com.meethere.service.NewsService;
import com.meethere.service.VenueService;
import com.meethere.util.Page4Navigator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageDAO messageDAO;
    @Autowired
    VenueService venueService;
    @Autowired
    BookingService bookingService;
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<Message> listByVenue(int vid,int status, int start, int size, int navigatePages){
        Venue venue = venueService.get(vid);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA = (Page) new Object();
        if(status==0)
            pageFromJPA = messageDAO.findByVenue(venue,pageable);
        else if(status==1)//只查找已通过的留言
            pageFromJPA = messageDAO.findByVenueAndState(venue,pass,pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<Message> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA =messageDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void saveMessage(Message message) {
        messageDAO.save(message);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void delete(int id){
        messageDAO.delete(id);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Message get(int id){
        return messageDAO.findOne(id);
    }
    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void update(Message message){
        messageDAO.save(message);
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Integer total(Venue venue){
        return messageDAO.countByVenueAndState(venue,pass);
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Message queryByBooking(Booking booking){
        Message message = messageDAO.findByBooking(booking);
        if(message!=null){
            booking.setState(bookingService.finish);
            bookingService.update(booking);
        }
        return message;
    }

}