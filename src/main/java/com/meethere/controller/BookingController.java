package com.meethere.controller;

import com.meethere.dao.BookingDAO;
import com.meethere.pojo.*;
import com.meethere.service.BookingService;
import com.meethere.service.NewsService;
import com.meethere.service.TimeSlotService;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.Page4Navigator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.meethere.service.BookingService.*;

@RestController
public class BookingController extends BasicController{

    @Autowired
    BookingService bookingService;
    @Autowired
    TimeSlotService timeSlotService;
    @GetMapping("/bookings")
    public IMoocJSONResult list(
                                @RequestParam(value = "start", defaultValue = "0") Integer start,
                                Integer size)
            throws Exception {
        start = start<0?0:start;
        if(size == null) size = PAGE_SIZE;
        Page4Navigator<Booking> page = bookingService.list(start,size,5);
        return IMoocJSONResult.ok(page);
    }
    @PutMapping("/arrive")
    public IMoocJSONResult arrive(int id){
        Booking booking = bookingService.get(id);
        booking.setState(waitFinish);
        bookingService.update(booking);
        return IMoocJSONResult.ok();
    }
    @PostMapping("/myBookings")
    public IMoocJSONResult listMyBookings(@RequestBody Booking booking,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            Integer size,HttpSession session)
            throws Exception {


        User user = (User)session.getAttribute("user");
        booking.setUser(user);
        start = start<0?0:start;
        if(size == null) size = PAGE_SIZE;
        Page4Navigator<Booking> page = bookingService.search(booking,start,size,5);
        return IMoocJSONResult.ok(page);
    }
    @PutMapping("/bookings")
    public IMoocJSONResult update(@RequestBody Booking booking) throws Exception {
        bookingService.update(booking);
        return IMoocJSONResult.ok(booking);
    }
    @PutMapping("/deleteBookings/{id}")
    public IMoocJSONResult delete(@PathVariable("id") int id) throws Exception {
        Booking booking = bookingService.get(id);
        booking.setState(delete);
        bookingService.update(booking);
        return IMoocJSONResult.ok(booking);
    }
    @GetMapping("/buy/{tid}")
    public IMoocJSONResult add( @PathVariable("tid")Integer tid, String remark , HttpSession session) throws Exception {
        User user = (User)session.getAttribute("user");
        TimeSlot timeSlot = timeSlotService.get(tid);
        timeSlotService.reduceSeat(timeSlot);
        Venue venue = timeSlot.getVenue();
        Booking booking = new Booking();
        booking.setRemark(remark);
        booking.setCreateDate(new Date());
        booking.setState(BookingService.waitApprove);
        booking.setUser(user);
        booking.setVenue(venue);
        booking.setTimeSlot(timeSlot);
        bookingService.saveBooking(booking);
        return IMoocJSONResult.ok(booking);
    }
    @PutMapping("/payed/{id}")
    public IMoocJSONResult payed( @PathVariable("id")Integer id, HttpSession session) throws Exception {
        User user = (User)session.getAttribute("user");
        Booking booking = bookingService.get(id);
        if(booking.getUser().getId()!=user.getId())
            return IMoocJSONResult.errorMsg("请重新登录");
        if(!booking.getState().equalsIgnoreCase("waitPay"))
            return IMoocJSONResult.errorMsg("付款已超期");
        booking.setState(waitTime);
        bookingService.update(booking);
        return IMoocJSONResult.ok(booking);
    }
    @PostMapping("/bookings")
    public IMoocJSONResult search(@RequestBody Booking booking,Integer date,
                                  @RequestParam(value = "start", defaultValue = "0") Integer start,
                                  Integer size)
            throws Exception {
        start = start<0?0:start;
        if(size == null) size = PAGE_SIZE;
        if(date!=null) {
            Date bookingDate = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(bookingDate);//设置起时间
            cal.add(Calendar.DATE, date);//增加一天  
            Date d = cal.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = formatter.format(d);
            Date dd = formatter.parse(dateString);
            booking.getTimeSlot().setBookingDate(dd);
        }
        Page4Navigator<Booking> page = bookingService.search(booking,start,size,5);
        return IMoocJSONResult.ok(page);
    }
    @PutMapping("/cancel/{id}")
    public IMoocJSONResult cancel(@PathVariable("id") int id) throws Exception {

        Booking booking = bookingService.get(id);
        booking.setState(cancelled);
        bookingService.update(booking);
        return IMoocJSONResult.ok(booking);
    }
    @PutMapping("/approve/{id}")
    public IMoocJSONResult approve(@PathVariable("id") int id) throws Exception {

        Booking booking = bookingService.get(id);
        booking.setState(waitPay);
        bookingService.update(booking);
        return IMoocJSONResult.ok(booking);
    }
    @PutMapping("/refuse/{id}")
    public IMoocJSONResult refuse(@PathVariable("id") int id) throws Exception {

        Booking booking = bookingService.get(id);
        booking.setState(refused);
        bookingService.update(booking);
        return IMoocJSONResult.ok(booking);
    }
}