package com.meethere.service.impl.interationTest;

import com.meethere.Application;
import com.meethere.dao.TimeSlotDAO;
import com.meethere.dao.VenueDAO;
import com.meethere.pojo.News;
import com.meethere.pojo.TimeSlot;
import com.meethere.pojo.Venue;
import com.meethere.service.TimeSlotService;
import com.meethere.util.Page4Navigator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-12 20:54
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TimeSlotServiceImplIntegrationTest {

	@Autowired
	private TimeSlotDAO timeSlotDAO;

	@Autowired
	private TimeSlotService timeSlotService;
	@Autowired
	private VenueDAO venueDAO;

	private Venue venue;

	@Before
	public void setUp() {
	}

	@Test
	@Transactional
	@Rollback
	public void database_integrity_test() throws ParseException {
		venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();

		venue=venueDAO.save(venue);

		Date bookingDate=new Date();


		TimeSlot timeSlot1=new TimeSlot.TimeSlotBuilder().id(1).beginTime(1).bookingDate(bookingDate).endTime(9).seat(100).venue(venue).build();
		TimeSlot timeSlot2=new TimeSlot.TimeSlotBuilder().id(2).beginTime(1).bookingDate(bookingDate).endTime(9).seat(100).venue(venue).build();
		TimeSlot timeSlot3=new TimeSlot.TimeSlotBuilder().id(3).beginTime(1).bookingDate(bookingDate).endTime(9).seat(100).venue(venue).build();

		timeSlot1=timeSlotDAO.save(timeSlot1);
		timeSlot2=timeSlotDAO.save(timeSlot2);
		timeSlot3=timeSlotDAO.save(timeSlot3);

		//list
		Page4Navigator<TimeSlot> entries= timeSlotService.list(venue.getId(),0,0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(3,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(3,entries.getContent().size());
		assertEquals(timeSlot1.getBeginTime(),entries.getContent().get(0).getBeginTime());
		assertEquals(timeSlot1.getEndTime(),entries.getContent().get(0).getEndTime());
		assertEquals(timeSlot1.getSeat(),entries.getContent().get(0).getSeat());
		assertEquals(timeSlot1.getVenue(),entries.getContent().get(0).getVenue());

		entries= timeSlotService.list(2,0,0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(0,entries.getTotalElements());
		assertEquals(0,entries.getTotalPages());
		assertEquals(0,entries.getContent().size());

		//createByVenue 值
		timeSlotService.createByVenue(venue,0);
		List<TimeSlot> timeSlots=timeSlotDAO.findAll();
		assertEquals(11,timeSlots.size());

	}
}
