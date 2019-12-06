package com.meethere.service;

import com.meethere.pojo.Booking;
import com.meethere.pojo.Message;
import com.meethere.pojo.News;
import com.meethere.pojo.Venue;
import com.meethere.util.Page4Navigator;

public interface MessageService {
    public static final String waitApprove = "waitApprove";
    public static final String refused = "refused";
    public static final String pass = "pass";
    public Page4Navigator<Message> listByVenue(int vid, int status,int start, int size, int navigatePages);
    public Page4Navigator<Message> list(int start, int size, int navigatePages);
    public void saveMessage(Message message);
    public void delete(int id);
    public void update(Message message);
    public Message get(int id);
    public Integer total(Venue venue);
    public Message queryByBooking(Booking booking);

}