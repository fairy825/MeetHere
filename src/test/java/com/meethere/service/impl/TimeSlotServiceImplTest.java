package com.meethere.service.impl;

import com.meethere.Application;
import com.meethere.dao.BookingDAO;
import com.meethere.dao.TimeSlotDAO;
import com.meethere.dao.VenueDAO;
import com.meethere.pojo.*;
import com.meethere.service.BookingService;
import com.meethere.service.TimeSlotService;
import com.meethere.service.UserService;
import com.meethere.service.VenueService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-11 14:50
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TimeSlotServiceImplTest {
	@MockBean
	private TimeSlotDAO timeSlotDAO;

	@Autowired
	private TimeSlotService timeSlotService;
	@MockBean
	VenueDAO venueDAO;
	@MockBean
	private VenueService venueService;

	private Venue venue;
	private List<Venue> venueList=new ArrayList<>();
	private Page pageFromJPA;
	private TimeSlot timeSlot;

	@Before
	public void setUp() {
		pageFromJPA = mock(Page.class);
		venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();
		timeSlot=new TimeSlot.TimeSlotBuilder().id(1).beginTime(1).endTime(9).seat(100).venue(venue).build();
		venueList.add(venue);
		venueList.add(venue);
	}

	@Test
	public void should_return_right_datestring() throws ParseException {
		when(venueService.get(anyInt())).thenReturn(venue);
		when(timeSlotDAO.findByVenueAndBookingDate(eq(venue),any(),any())).thenReturn(pageFromJPA);


		timeSlotService.list(1,1,0,1,1);

		ArgumentCaptor<Date> dateArgumentCaptor=ArgumentCaptor.forClass(Date.class);
		verify(timeSlotDAO).findByVenueAndBookingDate(any(),dateArgumentCaptor.capture(),any());
		Date d=dateArgumentCaptor.getValue();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(d);
		assertEquals("2019-12-19",dateString);

	}



	@Test
	public void should_create_right_nums_slot() throws ParseException {
		when(timeSlotDAO.save((TimeSlot) any())).thenReturn(timeSlot);

		timeSlotService.createByVenue(venue,1);

		verify(timeSlotDAO,times(8)).save((TimeSlot) any());
	}

	@Test
	public void should_add_right_nums_slot() throws ParseException {
		when(timeSlotDAO.save((TimeSlot) any())).thenReturn(timeSlot);
		when(venueService.list()).thenReturn(venueList);

		timeSlotService.addNewTimeSlot(1);

		verify(timeSlotDAO,times(16)).save((TimeSlot) any());//2*8
	}

	@Test
	public void should_update_success() throws ParseException {
		when(timeSlotDAO.save(timeSlot)).thenReturn(timeSlot);

		timeSlotService.update(timeSlot);

		verify(timeSlotDAO).save(timeSlot);
	}

	@Test
	public void should_get_success() throws ParseException {
		when(timeSlotDAO.findOne(anyInt())).thenReturn(timeSlot);

		timeSlotService.get(1);

		verify(timeSlotDAO).getOne(1);
	}

	@Test
	public void should_save_success() throws ParseException {
		when(timeSlotDAO.save(timeSlot)).thenReturn(timeSlot);

		timeSlotService.saveTimeSlot(timeSlot);

		verify(timeSlotDAO).save(timeSlot);
	}
}
