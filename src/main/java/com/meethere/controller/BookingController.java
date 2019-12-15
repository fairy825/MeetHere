package com.meethere.controller;

import com.meethere.dao.BookingDAO;
import com.meethere.pojo.*;
import com.meethere.service.BookingService;
import com.meethere.service.NewsService;
import com.meethere.service.TimeSlotService;
import com.meethere.util.DateUtils;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.Page4Navigator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.meethere.service.BookingService.*;

@RestController
@RequestMapping("/bookings")
public class BookingController extends BasicController{

    @Autowired
    BookingService bookingService;
    @Autowired
    TimeSlotService timeSlotService;
    @GetMapping("/{id}")
    public IMoocJSONResult getOne(@PathVariable("id")Integer id)
            throws Exception {
        return IMoocJSONResult.ok(bookingService.get(id));
    }
    @GetMapping("")
    public IMoocJSONResult list(
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            Integer size)
            throws Exception {
        start = start<0?0:start;
        if(size == null) size = PAGE_SIZE;
        Page4Navigator<Booking> page = bookingService.list(start,size,5);
        return IMoocJSONResult.ok(page);
    }
    @GetMapping("/booked/{tid}")
    public IMoocJSONResult booked(@PathVariable("tid")Integer tid, HttpSession session){
        User user = (User)session.getAttribute("user");
        if(user==null)
            return IMoocJSONResult.build(501,"未登录",null);
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTimeSlot(timeSlotService.get(tid));
        Booking b = bookingService.searchByUserAndTimeslot(booking);
        if(b==null)
            return IMoocJSONResult.ok();
        return IMoocJSONResult.errorMsg("已预订过该时段！请前往订单中心查看！");
    }
    @PutMapping("/arrive")
    public IMoocJSONResult arrive(int id, HttpSession session){
        User user = (User)session.getAttribute("user");
        if(user==null)
            return IMoocJSONResult.build(501,"未登录",null);
        Booking booking = bookingService.get(id);
        booking.setState(waitFinish);
        booking.setArriveDate(new Date());
        bookingService.update(booking);
        return IMoocJSONResult.ok();
    }
    @PostMapping("/mine")
    public IMoocJSONResult listMyBookings(@RequestBody Booking booking,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            Integer size,HttpSession session)
            throws Exception {
        User user = (User)session.getAttribute("user");
        booking.setUser(user);
        start = start<0?0:start;
        if(size == null) size = PAGE_SIZE;
        Page4Navigator<Booking> page = bookingService.searchWithoutDelete(booking,start,size,5);
        return IMoocJSONResult.ok(page);
    }
    @PutMapping("")
    public IMoocJSONResult update(@RequestBody Booking booking,HttpSession session) throws Exception {
        User user = (User)session.getAttribute("user");
        if(user==null)
            return IMoocJSONResult.build(501,"未登录",null);
        bookingService.update(booking);
        return IMoocJSONResult.ok(booking);
    }
    @PutMapping("/delete/{id}")
    public IMoocJSONResult delete(@PathVariable("id") int id,HttpSession session) throws Exception {
        User user = (User)session.getAttribute("user");
        Booking booking = bookingService.get(id);
        if(user==null||booking.getUser().getId()!=user.getId())
            return IMoocJSONResult.build(501,"未登录",null);
        booking.setState(delete);
        bookingService.update(booking);
        return IMoocJSONResult.ok(booking);
    }
    @GetMapping("/buy/{tid}")
    public IMoocJSONResult add( @PathVariable("tid")Integer tid, String remark , HttpSession session) throws Exception {
        User user = (User)session.getAttribute("user");
        if(user==null)
            return IMoocJSONResult.build(501,"未登录",null);
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
        if(user==null||booking.getUser().getId()!=user.getId())
            return IMoocJSONResult.build(501,"未登录",null);
        if(!booking.getState().equalsIgnoreCase("waitPay"))
            return IMoocJSONResult.errorMsg("付款已超期");
        booking.setState(waitTime);
        booking.setPayDate(new Date());
        bookingService.update(booking);
        return IMoocJSONResult.ok(booking);
    }
    @PostMapping("")
    public IMoocJSONResult search(@RequestBody Booking booking,Integer date,
                                  @RequestParam(value = "start", defaultValue = "0") Integer start,
                                  Integer size)
            throws Exception {
        start = start<0?0:start;
        if(size == null) size = PAGE_SIZE;
        if(date!=null) {
            Date dd = DateUtils.formatDate(date);
            booking.getTimeSlot().setBookingDate(dd);
        }
        Page4Navigator<Booking> page = bookingService.search(booking, start, size, 5);
        return IMoocJSONResult.ok(page);
    }
    @PutMapping("/cancel/{id}")
    public IMoocJSONResult cancel(@PathVariable("id") int id,HttpSession session) throws Exception {
        User user = (User)session.getAttribute("user");
        Booking booking = bookingService.get(id);
        if(user==null||booking.getUser().getId()!=user.getId())
            return IMoocJSONResult.build(501,"未登录",null);
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

    @Scheduled(cron = "0 0 * * * ? ")//每个小时更新
    public void changeBookingState() throws ParseException {
        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(rightNow.getTime());
        Date nowDate = new Timestamp(formatter.parse(dateString).getTime());

        int nowHour = rightNow.get(Calendar.HOUR_OF_DAY);
        String nextState = "";
        List<Booking> bookings = bookingService.findAll();
        for(Booking b : bookings) {
            int begin = b.getTimeSlot().getBeginTime();
            int end = b.getTimeSlot().getEndTime();
            Date bookingDate = b.getTimeSlot().getBookingDate();
            String curState = b.getState();
            nextState = curState;
            if((bookingDate.before(nowDate))||(nowHour>=end&&bookingDate.equals(nowDate))){
                if(curState.equals(waitPay)||curState.equals(waitTime)
                        ||curState.equals(waitArrive)||curState.equals(waitApprove))
                    nextState = cancelled;
            }else if(nowHour==begin&&bookingDate.equals(nowDate)){
                if(curState.equals(waitTime))
                    nextState = waitArrive;
            }
            b.setState(nextState);
            bookingService.update(b);
        }
    }
}
