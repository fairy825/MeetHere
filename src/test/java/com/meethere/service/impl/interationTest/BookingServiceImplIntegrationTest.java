package com.meethere.service.impl.interationTest;

import com.meethere.Application;
import com.meethere.dao.BookingDAO;
import com.meethere.dao.TimeSlotDAO;
import com.meethere.dao.UserDAO;
import com.meethere.dao.VenueDAO;
import com.meethere.pojo.*;
import com.meethere.service.AdminService;
import com.meethere.service.BookingService;
import com.meethere.service.UserService;
import com.meethere.service.VenueService;
import com.meethere.util.Page4Navigator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-12 16:17
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BookingServiceImplIntegrationTest {

	@Autowired
	private BookingDAO bookingDAO;
	@Autowired
	private BookingService bookingService;
	@Autowired
	VenueDAO venueDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	UserService userService;
	@Autowired
	VenueService venueService;
	@Autowired
	TimeSlotDAO timeSlotDAO;

	private User user;
	private User user1;
	private Venue venue;

	@Before
	public void setUp() {



	}

	@Test
	@Transactional
	@Rollback
	public void database_integrity_test() throws ParseException {
		user = new User.UserBuilder().id(1).name("mh").password("12345").phoneNumber("12345678901")
				.email("6666@meethere.com").nickname("meethere")
				.faceImage("img/faceImage/20190906012034.jpg").build();
		user1 = new User.UserBuilder().id(2).name("mh2").password("12345").phoneNumber("12345678901")
				.email("6666@meethere.com").nickname("meethere")
				.faceImage("img/faceImage/20190906012034.jpg").build();
		venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();



		User cur_user=userDAO.save(user);
		User cur_user1=userDAO.save(user1);

		Venue cur_venue=venueDAO.save(venue);

		TimeSlot timeSlot1=new TimeSlot.TimeSlotBuilder().id(1).beginTime(1).endTime(9).seat(100).venue(cur_venue).build();
		TimeSlot cur_time=timeSlotDAO.save(timeSlot1);

		Booking booking1=new Booking.BookingBuilder().venue(cur_venue).timeSlot(cur_time).user(cur_user).state(BookingService.waitTime).build();
		Booking booking2=new Booking.BookingBuilder().venue(cur_venue).timeSlot(cur_time).user(cur_user).state(BookingService.delete).build();
		Booking booking3=new Booking.BookingBuilder().venue(cur_venue).timeSlot(cur_time).user(cur_user1).build();

		bookingDAO.save(booking1);
		bookingDAO.save(booking2);
		bookingDAO.save(booking3);

		//list
		Page4Navigator<Booking> entries =bookingService.list(0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(3,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(3,entries.getContent().size());

		//listBookingsByUser
		entries=bookingService.listBookingsByUser(cur_user,0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(1,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(1,entries.getContent().size());
		System.out.println(entries.getContent());
		assertEquals(cur_user.getName(),entries.getContent().get(0).getUser().getName());

		//search
		Booking booking_for_search = new Booking.BookingBuilder().id(3).user(cur_user).venue(cur_venue).build();
		entries = bookingService.search(booking_for_search,0,8,5);
		assertTrue(entries.isFirst());
		assertEquals(8,entries.getSize());
		assertEquals(2,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(cur_venue.getName(),entries.getContent().get(0).getVenue().getName());
		assertEquals(cur_user.getName(),entries.getContent().get(0).getUser().getName());

		booking_for_search = new Booking.BookingBuilder().id(4).user(cur_user1).venue(cur_venue).build();
		entries = bookingService.search(booking_for_search,0,8,5);
		assertEquals(1,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());

		//searchWithoutDelete
		entries=bookingService.searchWithoutDelete(booking2,0,8,5);
		assertEquals(0,entries.getTotalElements());
		assertEquals(0,entries.getTotalPages());

		entries=bookingService.searchWithoutDelete(booking1,0,8,5);
		assertEquals(1,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());

		//searchByUser
		entries=bookingService.searchByUser("mh",0,8,5);
		assertEquals(2,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());

		entries=bookingService.searchByUser("notexsit",0,8,5);
		assertEquals(0,entries.getTotalElements());
		assertEquals(0,entries.getTotalPages());

		//searchByVenue
		entries=bookingService.searchByVenue("vn",0,8,5);
		assertEquals(3,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());

		entries=bookingService.searchByUser("notexsit",0,8,5);
		assertEquals(0,entries.getTotalElements());
		assertEquals(0,entries.getTotalPages());

		//searchByUserAndTimeslot
		booking_for_search.setState(BookingService.cancelled);
		Booking nullBooking1=bookingService.searchByUserAndTimeslot(booking_for_search);
		assertNull(nullBooking1);


		booking1.setState(BookingService.waitTime);
		bookingDAO.save(booking1);
		Booking existBooking=bookingService.searchByUserAndTimeslot(booking1);
		assertEquals(booking1,existBooking);

	}

}
