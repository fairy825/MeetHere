package com.meethere.controller;

import com.meethere.pojo.*;
import com.meethere.service.*;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.Page4Navigator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.util.Date;

@RestController
@Api(value="留言的接口", tags= {"留言管理的controller"})
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
    @ApiOperation(value="查询某一场馆下的已通过的可以展示评论", notes="管理员或用户查询某一场馆下的已通过的评论的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="vid", value="场馆id", required = true, dataType="Integer"),
            @ApiImplicitParam(name="start", value="页码", required = true, dataType="Integer"),
            @ApiImplicitParam(name="size", value="每页的显示个数",  dataType="Integer")
    })
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

    @GetMapping("/messages/mine")
    public IMoocJSONResult myreview(@RequestParam(value = "bid") Integer bid , HttpSession session)
            throws Exception {
        User user = (User)session.getAttribute("user");
        Booking booking = bookingService.get(bid);
        if(user==null||!booking.getUser().getId().equals(user.getId()))
            return IMoocJSONResult.build(501,"未登录",null);
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

    @ApiOperation(value="对某一场馆留言评论", notes="用户在服务结束后给场馆留言的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="vid", value="场馆id", required = true, dataType="Integer"),
            @ApiImplicitParam(name="bid", value="订单id", required = true, dataType="Integer")
    })
    @PostMapping("/messages/{vid}")
    public IMoocJSONResult add(@RequestBody Message message, @PathVariable("vid")int vid,
                               @RequestParam(value = "bid") int bid, HttpSession session) throws Exception {
        Booking booking = bookingService.get(bid);
        if(!booking.getState().equalsIgnoreCase(bookingService.waitReview))
            return IMoocJSONResult.errorMsg("评论已超期");
        User user = (User)session.getAttribute("user");
        if(user==null||!booking.getUser().getId().equals(user.getId()))
            return IMoocJSONResult.build(501,"未登录",null);

        message.setUser(user);
        message.setVenue(venueService.get(vid));
        message.setState(messageService.waitApprove);
        message.setCreateDate(new Date());
        message.setBooking(booking);
        messageService.saveMessage(message);
        booking.setState(bookingService.finish);
        bookingService.update(booking);
        return IMoocJSONResult.ok(message);
    }

    @DeleteMapping("/messages/{id}")
    public IMoocJSONResult suspend(@PathVariable("id") int id, HttpSession session)  throws Exception {
        if((User)session.getAttribute("user")==null&&(Admin)session.getAttribute("admin")==null)
            return IMoocJSONResult.build(501,"未登录",null);
        Booking booking = messageService.get(id).getBooking();
        booking.setState(bookingService.waitReview);
        messageService.delete(id);

        bookingService.update(booking);
        return IMoocJSONResult.ok();
    }
    @PutMapping("/messages/agree/{id}")
    public IMoocJSONResult agree(@PathVariable("id") int id) throws Exception {

        Message message = messageService.get(id);
        message.setState(messageService.pass);
        messageService.update(message);
        return IMoocJSONResult.ok(message);
    }
    @PutMapping("/messages/refuse/{id}")
    public IMoocJSONResult refuse(@PathVariable("id") int id) throws Exception {

        Message message = messageService.get(id);
        message.setState(messageService.refused);
        messageService.update(message);
        return IMoocJSONResult.ok(message);
    }
}
