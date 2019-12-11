package com.meethere.service.impl;

import com.meethere.Application;
import com.meethere.dao.MessageDAO;
import com.meethere.pojo.Booking;
import com.meethere.pojo.Message;
import com.meethere.pojo.Venue;
import com.meethere.service.BookingService;
import com.meethere.service.MessageService;
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
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-10 20:47
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MessageServiceImplTest {
	@MockBean
	private MessageDAO messageDAO;
	@MockBean
	private VenueService venueService;
	@MockBean
	private BookingService bookingService;
	@Autowired
	private MessageService messageService;


	private Page pageFromJPA;
	private Venue venue;
	private Booking existBooking;
	private Booking nullBooking;
	private Message message;

	@Before
	public void setUp() {
		pageFromJPA = mock(Page.class);
		venue=new Venue.VenueBuilder().id(1).name("vn").build();
		existBooking=new Booking.BookingBuilder().id(1).build();
		nullBooking=new Booking.BookingBuilder().id(0).build();
		message=new Message.MessageBuilder().id(1).build();
	}

	@Test
	public void list_by_venue_when_status_is_0(){
		when(venueService.get(anyInt())).thenReturn(venue);
		when(messageDAO.findByVenue(eq(venue),any())).thenReturn(pageFromJPA);
		when(messageDAO.findByVenueAndState(eq(venue),eq(MessageService.pass),any())).thenReturn(pageFromJPA);

		messageService.listByVenue(1,0,5,8,1);

		verify(messageDAO,times(1)).findByVenue(eq(venue),any());
		verify(messageDAO,times(0)).findByVenueAndState(any(),anyString(),any());


	}

	@Test
	public void list_by_venue_when_status_is_1(){
		when(venueService.get(anyInt())).thenReturn(venue);
		when(messageDAO.findByVenue(eq(venue),any())).thenReturn(pageFromJPA);
		when(messageDAO.findByVenueAndState(eq(venue),eq(MessageService.pass),any())).thenReturn(pageFromJPA);

		messageService.listByVenue(1,1,5,8,1);

		verify(messageDAO,times(0)).findByVenue(any(),any());
		verify(messageDAO,times(1)).findByVenueAndState(eq(venue),eq(MessageService.pass),any());

	}

	@Test(expected = NullPointerException.class)//若抛出空指针异常 则测试成功
	public void should_throw_nullpoint_when_status_is_not_0_and_1(){
		when(venueService.get(anyInt())).thenReturn(venue);
		when(messageDAO.findByVenue(eq(venue),any())).thenReturn(pageFromJPA);
		when(messageDAO.findByVenueAndState(eq(venue),eq(MessageService.pass),any())).thenReturn(pageFromJPA);
		when(pageFromJPA.getTotalElements()).thenReturn(0l);

		Page4Navigator<Message> page=messageService.listByVenue(1,2,5,8,1);

		verify(messageDAO,times(0)).findByVenue(any(),any());
		verify(messageDAO,times(0)).findByVenueAndState(eq(venue),eq(MessageService.pass),any());


	}

	@Test
	public void list() {
		when(messageDAO.findAll((Pageable)any())).thenReturn((Page)pageFromJPA);
		//Execute
		messageService.list(0,8,5);
		verify(messageDAO, times(1)).findAll((Pageable)any());
	}

	@Test
	public void should_return_null_when_message_not_exist(){
		when(messageDAO.findByBooking(nullBooking)).thenReturn(null);

		Message message=messageService.queryByBooking(nullBooking);

		assertNull(message);

	}

	@Test
	public void should_return_null_when_message_exist(){

		when(messageDAO.findByBooking(existBooking)).thenReturn(message);

		messageService.queryByBooking(existBooking);

		ArgumentCaptor<Booking> bookingArgumentCaptor=ArgumentCaptor.forClass(Booking.class);
		verify(bookingService).update(bookingArgumentCaptor.capture());
		assertEquals(bookingArgumentCaptor.getValue().getState(),BookingService.finish);


	}

}
