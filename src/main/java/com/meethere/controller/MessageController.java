package com.meethere.controller;

import com.meethere.pojo.Booking;
import com.meethere.pojo.Message;
import com.meethere.pojo.News;
import com.meethere.pojo.User;
import com.meethere.service.*;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.util.Date;

@RestController
public class MessageController extends BasicController{

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    VenueService venueService;
    @Autowired
    BookingService bookingService;
    @GetMapping("/venues/{vid}/messages")
    public IMoocJSONResult list(@PathVariable("vid") int vid,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
                                Integer size)
            throws Exception {
    	start = start<0?0:start;
    	if(size == null) size = PAGE_SIZE;
    	Page4Navigator<Message> page = messageService.listByVenue(vid,0,start,size,5);
        return IMoocJSONResult.ok(page);
    }
    @GetMapping("/venues/{vid}/messages/show")
    public IMoocJSONResult show(@PathVariable("vid") int vid,
                                @RequestParam(value = "start", defaultValue = "0") Integer start,
                                Integer size)
            throws Exception {
        start = start<0?0:start;
        if(size == null) size = PAGE_SIZE;
        Page4Navigator<Message> page = messageService.listByVenue(vid,1,start,size,5);
        return IMoocJSONResult.ok(page);
    }

    @GetMapping("/myreview")
    public IMoocJSONResult myreview(@RequestParam(value = "bid") Integer bid , HttpSession session)
            throws Exception {
        User user = (User)session.getAttribute("user");
        Booking booking = bookingService.get(bid);
        if(booking.getUser().getId()!=user.getId())
            return IMoocJSONResult.errorMsg("请重新登录");
        return IMoocJSONResult.ok(messageService.queryByBooking(booking));
    }

    @GetMapping("/messages")
    public IMoocJSONResult list(@RequestParam(value = "start", defaultValue = "0") Integer start,
                                Integer size)
            throws Exception {
        start = start<0?0:start;
        if(size == null) size = PAGE_SIZE;
        Page4Navigator<Message> page = messageService.list(start,size,5);
        return IMoocJSONResult.ok(page);
    }

    @PostMapping("/messages/{vid}")
    public IMoocJSONResult add(@RequestBody Message message, @PathVariable("vid")int vid,
                               @RequestParam(value = "bid") int bid, HttpSession session) throws Exception {
        Booking booking = bookingService.get(bid);
        booking.setState(bookingService.finish);
        bookingService.update(booking);
        User user = (User)session.getAttribute("user");
        message.setUser(user);
        message.setVenue(venueService.get(vid));
        message.setState(messageService.waitApprove);
        message.setCreateDate(new Date());
        message.setBooking(booking);
        messageService.saveMessage(message);
        return IMoocJSONResult.ok(message);
    }

    @DeleteMapping("/messages/{id}")
    public IMoocJSONResult suspend(@PathVariable("id") int id)  throws Exception {
        Booking booking = messageService.get(id).getBooking();
        booking.setState(bookingService.waitReview);
        messageService.delete(id);

        bookingService.update(booking);
        return IMoocJSONResult.ok();
    }
    @PutMapping("/agreeMessage/{id}")
    public IMoocJSONResult agree(@PathVariable("id") int id) throws Exception {

        Message message = messageService.get(id);
        message.setState(messageService.pass);
        messageService.update(message);
        return IMoocJSONResult.ok(message);
    }
    @PutMapping("/refuseMessage/{id}")
    public IMoocJSONResult refuse(@PathVariable("id") int id) throws Exception {

        Message message = messageService.get(id);
        message.setState(messageService.refused);
        messageService.update(message);
        return IMoocJSONResult.ok(message);
    }
}