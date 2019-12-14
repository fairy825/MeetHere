package com.meethere.service.impl.interationTest;

import com.meethere.Application;
import com.meethere.dao.MessageDAO;
import com.meethere.dao.VenueDAO;
import com.meethere.pojo.*;
import com.meethere.service.BookingService;
import com.meethere.service.MessageService;
import com.meethere.service.VenueService;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-12 20:24
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MessageServiceImplIntegrationTest {

	@Autowired
	private MessageDAO messageDAO;
	@Autowired
	private MessageService messageService;
	@Autowired
	private VenueDAO venueDAO;

	private Venue venue;
	private Venue venue1;
	private Booking existBooking;
	private Booking nullBooking;
	private Message message;

	@Before
	public void setUp() {
		venue=new Venue.VenueBuilder().id(1).name("vn").build();
		venue1=new Venue.VenueBuilder().id(2).name("vn2").build();

		existBooking=new Booking.BookingBuilder().id(1).build();
		nullBooking=new Booking.BookingBuilder().id(0).build();
		message=new Message.MessageBuilder().id(1).build();
	}

	@Test
	@Transactional
	@Rollback
	public void database_integrity_test() {

		venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();
		venue1=new Venue.VenueBuilder().id(2).name("vn2").build();

		venue=venueDAO.save(venue);
		venue1=venueDAO.save(venue1);
		Message message1=new Message.MessageBuilder().id(1).state(MessageService.refused).venue(venue).build();
		Message message2=new Message.MessageBuilder().id(2).state(MessageService.pass).venue(venue1).build();
		Message message3=new Message.MessageBuilder().id(3).state(MessageService.pass).venue(venue).build();

		message1=messageDAO.save(message1);
		message2=messageDAO.save(message2);
		message3=messageDAO.save(message3);

		//listByVenue
		Page4Navigator<Message> entries= messageService.listByVenue(venue.getId(),0,0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(2,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(2,entries.getContent().size());

		entries= messageService.listByVenue(venue1.getId(),1,0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(1,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(1,entries.getContent().size());

		//list
		entries=messageService.list(0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(3,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(3,entries.getContent().size());


	}
}
