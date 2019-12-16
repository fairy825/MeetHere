package com.meethere.service.impl.interationTest;

import com.meethere.Application;
import com.meethere.dao.BookingDAO;
import com.meethere.dao.DistrictDAO;
import com.meethere.dao.MessageDAO;
import com.meethere.dao.VenueDAO;
import com.meethere.pojo.Booking;
import com.meethere.pojo.District;
import com.meethere.pojo.Message;
import com.meethere.pojo.Venue;
import com.meethere.service.*;
import com.meethere.util.Page4Navigator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-12 21:37
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class VenueServiceImplIntegrationTest {
	@Autowired
	VenueDAO venueDAO;
	@Autowired
	DistrictDAO districtDAO;
	@Autowired
	DistrictService districtService;
	@Autowired
	MessageService messageService;
	@Autowired
	MessageDAO messageDAO;
	@Autowired
	BookingService bookingService;
	@Autowired
	BookingDAO bookingDAO;
	@Autowired
	TimeSlotService timeSlotService;
	@Autowired
	VenueService venueService;

	private District district;
//	private Venue venue;


	@Before
	public void setUp() {
//		venue=new Venue.VenueBuilder().id(1).price(2f).name("vn").startTime(1).endTime(9).totalSeat(100).build();
//		venueList=new ArrayList<>();
//		venueList.add(venue);
//		venueList.add(venue);
//		district=new District.DistrictBuilder().id(1).name("dis1").venues(venueList).build();
	}

	@Test
	@Transactional
	@Rollback
	public void database_integrity_test()  {


//		List<Venue> venueList=new ArrayList<>();
//
//		venueList.add(venue1);
//		venueList.add(venue2);
//		venueList.add(venue3);

		district=new District.DistrictBuilder().id(1).name("dis1").build();

		district=districtDAO.save(district);

		Venue venue1=new Venue.VenueBuilder().id(1).district(district).price(2f).name("vn1").startTime(1).endTime(9).totalSeat(100).build();
		Venue venue2=new Venue.VenueBuilder().id(2).district(district).price(2f).name("vn2").startTime(1).endTime(9).totalSeat(100).build();
		Venue venue3=new Venue.VenueBuilder().id(3).price(2f).name("vn3").startTime(1).endTime(9).totalSeat(100).build();

		venue1=venueDAO.save(venue1);
		venue2=venueDAO.save(venue2);
		venue3=venueDAO.save(venue3);


		//list
		Page4Navigator<Venue> entries= venueService.list(0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(3,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(3,entries.getContent().size());
		assertEquals(venue3,entries.getContent().get(0));
		assertEquals(venue2,entries.getContent().get(1));
		assertEquals(venue1,entries.getContent().get(2));

		//listByDistrict
		entries= venueService.listByDistrict(district.getId(),0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(2,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(2,entries.getContent().size());

		entries= venueService.listByDistrict(0,0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(3,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(3,entries.getContent().size());

		//setSaleAndReviewNumber
		Booking booking1=new Booking.BookingBuilder().id(1).venue(venue1).state(BookingService.waitTime).build();
		Booking booking2=new Booking.BookingBuilder().id(2).venue(venue1).state(BookingService.delete).build();
		Booking booking3=new Booking.BookingBuilder().id(3).venue(venue1).state(BookingService.refused).build();

		booking1=bookingDAO.save(booking1);
		booking2=bookingDAO.save(booking2);
		booking3=bookingDAO.save(booking3);

		Message message1=new Message.MessageBuilder().id(1).state(MessageService.refused).venue(venue2).build();
		Message message2=new Message.MessageBuilder().id(2).state(MessageService.pass).venue(venue1).build();
		Message message3=new Message.MessageBuilder().id(3).state(MessageService.pass).venue(venue2).build();

		message1=messageDAO.save(message1);
		message2=messageDAO.save(message2);
		message3=messageDAO.save(message3);

		venueService.setSaleAndReviewNumber(venue1);

		assertEquals(2,(int)venue1.getSaleCount());
		assertEquals(1,(int)venue1.getReviewCount());



	}
}
