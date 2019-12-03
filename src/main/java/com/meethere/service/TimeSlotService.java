package com.meethere.service;

import com.meethere.pojo.News;
import com.meethere.pojo.TimeSlot;
import com.meethere.pojo.Venue;
import com.meethere.util.Page4Navigator;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface TimeSlotService {
    public Page4Navigator<TimeSlot> list(int vid, int date , int start, int size, int navigatePages) throws ParseException;
    public List<TimeSlot> list(int vid, int date) throws ParseException;
    public void update(TimeSlot timeSlot);
    public List<TimeSlot> findByBookingDate(Date date);
    public void createByVenue(Venue venue,Integer date) throws ParseException;
    public void addNewTimeSlot(Integer date) throws ParseException;
    public void saveTimeSlot(TimeSlot timeSlot);
    public void reduceSeat(TimeSlot timeSlot);
    public TimeSlot get(int id);
}