package com.meethere.controller;

import com.meethere.pojo.Booking;
import com.meethere.service.BookingService;
import com.meethere.service.TimeSlotService;
import com.meethere.util.DateUtils;
import com.meethere.util.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.meethere.service.BookingService.*;

@Controller
public class BasicController {

	@Autowired
	public RedisOperator redis;
	@Autowired
	BookingService bookingService;
	@Autowired
	TimeSlotService timeSlotService;

	public static final String USER_REDIS_SESSION = "user-redis-session";
	public static final String ADMIN_REDIS_SESSION = "admin-redis-session";
//	public static final String FILE_SPACE = "/Users/yangxiaoyan/Desktop/meethere1222/meethere/src/main/webapp/img";
	public static final String FILE_SPACE = "D:/软件测试与验证/实践/MeetHere/src/main/webapp/img";
		// 每页分页的记录数
	public static final Integer PAGE_SIZE = 5;

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
