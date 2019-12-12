package com.meethere.controller;

import com.meethere.pojo.News;
import com.meethere.pojo.TimeSlot;
import com.meethere.service.NewsService;
import com.meethere.service.TimeSlotService;
import com.meethere.util.DateUtils;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
public class TimeSlotController extends BasicController{

    @Autowired
    TimeSlotService timeSlotService;

    @GetMapping("/venues/{vid}/timeSlots")
    public IMoocJSONResult list(@PathVariable("vid") int vid,
                                HttpServletRequest request)
            throws Exception {
        int date = Integer.parseInt(request.getParameter("date"));
        List<TimeSlot> list = timeSlotService.list(vid,date);
        return IMoocJSONResult.ok(list);
    }

    @PutMapping("/timeSlots")
    public IMoocJSONResult update(@RequestBody TimeSlot timeSlot) throws Exception {
        timeSlotService.update(timeSlot);

        return IMoocJSONResult.ok(timeSlot);
    }

//    @Scheduled(cron = "0 0 0 */1 * ?")//每天
    @Scheduled(cron = "0 * * * * ? ")
    public void addNewTimeSlot() throws ParseException {
//        Date today = new Date();
//        Calendar cal = Calendar.getInstance();
        for(int i = 0;i<=2;i++) {
            Date dd = DateUtils.formatDate(i);
            if (timeSlotService.findByBookingDate(dd).size() == 0) {
                System.out.println("add");
                System.out.println(dd);
                System.out.println("i="+i);
                timeSlotService.addNewTimeSlot(i);
            }
        }
    }

}
