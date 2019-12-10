package com.meethere.service.impl;

import com.meethere.Application;
import com.meethere.dao.BookingDAO;
import com.meethere.dao.UserDAO;
import com.meethere.dao.VenueDAO;
import com.meethere.pojo.Booking;
import com.meethere.pojo.User;
import com.meethere.pojo.Venue;
import com.meethere.service.BookingService;
import com.meethere.service.UserService;
import com.meethere.service.VenueService;
import com.meethere.util.Page4Navigator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-10 18:26
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BookingServiceImplTest {

	@MockBean
	private BookingDAO bookingDAO;
	@MockBean
	private UserService userService;
	@Autowired
	private BookingService bookingService;
	@MockBean
	VenueDAO venueDAO;
	@MockBean
	private VenueService venueService;



	private User user;
	private Venue venue;
	private Page pageFromJPA;

	@Before
	public void setUp() {
		user = new User.UserBuilder().id(1).name("mh").password("12345").phoneNumber("12345678901")
				.email("6666@meethere.com").nickname("meethere")
				.faceImage("img/faceImage/20190906012034.jpg").build();

		venue=new Venue.VenueBuilder().id(1).name("vn").build();

		pageFromJPA = mock(Page.class);
	}

	@Test
	public void list() {
		when(bookingDAO.findAll((Pageable)any())).thenReturn((Page)pageFromJPA);
		//Execute
		bookingService.list(0,8,5);
		verify(bookingDAO, times(1)).findAll((Pageable)any());
	}

	@Test
	public void should_list_once() {
		when(bookingDAO.findByUserAndStateNotOrderByIdDesc(any(),anyString(),any())).thenReturn(pageFromJPA);
		//Execute
		bookingService.listBookingsByUser(user,0,5,1);
		verify(bookingDAO,times(1)).findByUserAndStateNotOrderByIdDesc(eq(user),eq(BookingService.delete),any());
	}

	@Test
	public void should_find_user_when_keyword_exist() {
		//Stub to return value
		when(userService.findByName("mh")).thenReturn(user);
		when(bookingDAO.findByUser(eq(user),any())).thenReturn(pageFromJPA);

		//Execute
		Page4Navigator<Booking> page=bookingService.searchByUser("mh",0,5,1);
		ArgumentCaptor<User> userCaptor=ArgumentCaptor.forClass(User.class);
		verify(bookingDAO).findByUser(userCaptor.capture(),ArgumentCaptor.forClass(Pageable.class).capture());
		assertEquals(userCaptor.getValue(),user);

	}

	@Test
	public void should_find_null_user_when_keyword_not_exist() {
		//Stub to return value
		when(userService.findByName("notexist")).thenReturn(null);
		when(bookingDAO.findByUser(eq(null),any())).thenReturn(pageFromJPA);
		when(pageFromJPA.getTotalElements()).thenReturn(0l);

		//Execute
		Page4Navigator<Booking> page=bookingService.searchByUser("notexist",0,5,1);
		ArgumentCaptor<User> userCaptor=ArgumentCaptor.forClass(User.class);

		//verify mapping
		verify(bookingDAO).findByUser(userCaptor.capture(),ArgumentCaptor.forClass(Pageable.class).capture());
		assertNull(userCaptor.getValue());
		assertEquals(page.getTotalElements(),0l);
	}

	@Test
	public void should_find_venue_when_keyword_exist() {
		//Stub to return value
		when(venueService.findByName("vn")).thenReturn(venue);
		when(bookingDAO.findByVenue(eq(venue),any())).thenReturn(pageFromJPA);

		//Execute
		Page4Navigator<Booking> page=bookingService.searchByVenue("vn",0,5,1);
		ArgumentCaptor<Venue> venueCaptor=ArgumentCaptor.forClass(Venue.class);
		verify(bookingDAO).findByVenue(venueCaptor.capture(),ArgumentCaptor.forClass(Pageable.class).capture());
		assertEquals(venueCaptor.getValue(),venue);

	}

	@Test
	public void should_find_null_venue_when_keyword_not_exist() {
		//Stub to return value
		when(venueService.findByName("notexist")).thenReturn(null);
		when(bookingDAO.findByVenue(eq(null),any())).thenReturn(pageFromJPA);
		when(pageFromJPA.getTotalElements()).thenReturn(0l);

		//Execute
		Page4Navigator<Booking> page=bookingService.searchByVenue("notexist",0,5,1);
		ArgumentCaptor<Venue> venueCaptor=ArgumentCaptor.forClass(Venue.class);

		//verify mapping
		verify(bookingDAO).findByVenue(venueCaptor.capture(),any());
		assertNull(venueCaptor.getValue());
		assertEquals(page.getTotalElements(),0l);
	}
}
