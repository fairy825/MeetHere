package com.meethere.controller;

import com.alibaba.fastjson.JSONObject;
import com.meethere.Application;
import com.meethere.dao.BookingDAO;
import com.meethere.pojo.Booking;
import com.meethere.pojo.TimeSlot;
import com.meethere.pojo.User;
import com.meethere.pojo.Venue;
import com.meethere.service.BookingService;
import com.meethere.service.TimeSlotService;
import com.meethere.service.UserService;
import com.meethere.util.MD5Utils;
import com.meethere.util.Page4Navigator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-15 14:56
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BookingControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private Page4Navigator<Booking> page=new Page4Navigator<>();
	@Mock
	MockHttpSession mockHttpSession;
	//@MockBean=@Autowired+mock方法实例化对象
	@MockBean
	private UserService userService;

	@MockBean
	private BookingDAO bookingDAO;

	@MockBean
	private BookingService bookingService;

	@MockBean
	private TimeSlotService timeSlotService;

	private MockMvc mockMvc;
	private User user;
	private Venue venue;
	private TimeSlot timeSlot;
	private Booking booking;

	public static final String baseUrl="/bookings";

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		timeSlot=new TimeSlot.TimeSlotBuilder().id(1).beginTime(1).endTime(9).seat(100).venue(venue).build();


		user = new User.UserBuilder().id(1).name("mh").password("12345").phoneNumber("12345678901")
				.email("6666@meethere.com").nickname("meethere")
				.faceImage("img/faceImage/20190906012034.jpg").build();

		venue=new Venue.VenueBuilder().id(1).name("vn").build();
		booking=new Booking.BookingBuilder().id(1).venue(venue).user(user).timeSlot(timeSlot).arriveDate(new Date()).state(BookingService.waitPay).build();


	}
	//list
	@Test
	public void should_list_bookings_of_first_page() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"?start=0&size=8")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());
		ArgumentCaptor<Integer> intArgCaptor1 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> intArgCaptor2 = ArgumentCaptor.forClass(Integer.class);
		verify(bookingService,times(1)).list(intArgCaptor1.capture(),intArgCaptor2.capture(),eq(5));
		assertEquals(Integer.valueOf(0), intArgCaptor1.getValue());
		assertEquals(Integer.valueOf(8), intArgCaptor2.getValue());
	}
	//grtOne
	@Test
	public void should_get_one_booking() throws Exception {
		when(bookingService.get(1)).thenReturn(booking);

		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"/1")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1))
				.andDo(MockMvcResultHandlers.print());

		ArgumentCaptor<Integer> intArgCaptor1 = ArgumentCaptor.forClass(Integer.class);
		verify(bookingService,times(1)).get(intArgCaptor1.capture());
		assertEquals(Integer.valueOf(1), intArgCaptor1.getValue());
	}

	//booked_1
	@Test
	public void should_booked_fail() throws Exception {
		when(bookingService.searchByUserAndTimeslot(any())).thenReturn(booking);
		when(mockHttpSession.getAttribute("user")).thenReturn(user);
		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"/booked/1")).session(mockHttpSession))

				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("已预订过该时段！请前往订单中心查看！"))
				.andDo(MockMvcResultHandlers.print());

	}

	//booked_2
	@Test
	public void should_booked_fail_when_user_is_null() throws Exception {
		when(bookingService.searchByUserAndTimeslot(booking)).thenReturn(booking);
		when(mockHttpSession.getAttribute("user")).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"/booked/1")).session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("未登录"))
				.andDo(MockMvcResultHandlers.print());
	}

	//booked_3
	@Test
	public void should_booked_success_when_booking_is_null() throws Exception {
		when(bookingService.searchByUserAndTimeslot(booking)).thenReturn(null);
		when(mockHttpSession.getAttribute("user")).thenReturn(user);
		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"/booked/1")).session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());
	}

	//arrive
	@Test
	public void should_update_booking_waitFinish() throws Exception {
		when(bookingService.get(1)).thenReturn(booking);
		doNothing().when(bookingService).update(booking);
		when(mockHttpSession.getAttribute("user")).thenReturn(user);


		mockMvc.perform(MockMvcRequestBuilders.put(new URI(baseUrl+"/arrive/?id=1")).session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());
		ArgumentCaptor<Booking> bookingArgumentCaptor = ArgumentCaptor.forClass(Booking.class);
		verify(bookingService).update(bookingArgumentCaptor.capture());
		assertEquals(BookingService.waitFinish, bookingArgumentCaptor.getValue().getState());
	}

	//paid_1
	@Test
	public void should_update_booking_paid_right() throws Exception {
		booking.setState(BookingService.waitPay);
		when(bookingService.get(1)).thenReturn(booking);
		doNothing().when(bookingService).update(booking);
		when(mockHttpSession.getAttribute("user")).thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.put(new URI(baseUrl+"/payed/1"))
				.session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());
		ArgumentCaptor<Booking> bookingArgumentCaptor = ArgumentCaptor.forClass(Booking.class);
		verify(bookingService).update(bookingArgumentCaptor.capture());
		assertEquals(BookingService.waitTime, bookingArgumentCaptor.getValue().getState());
	}

	//paid_2
	@Test
	public void should_update_booking_paid_fail() throws Exception {
		booking.setState(BookingService.refused);
		when(bookingService.get(1)).thenReturn(booking);
		doNothing().when(bookingService).update(booking);
		when(mockHttpSession.getAttribute("user")).thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.put(new URI(baseUrl+"/payed/1")).session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("付款已超期"))
				.andDo(MockMvcResultHandlers.print());
	}

	//refuse
	@Test
	public void should_refuse_booking() throws Exception {

		when(bookingService.get(anyInt())).thenReturn(booking);

		mockMvc.perform(MockMvcRequestBuilders.put(new URI(baseUrl+"/refuse/1")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		ArgumentCaptor<Booking> bookingArgumentCaptor=ArgumentCaptor.forClass(Booking.class);

		verify(bookingService).update(bookingArgumentCaptor.capture());
		assertEquals("refused",bookingArgumentCaptor.getValue().getState());
	}

	//cancel
	@Test
	public void should_cancel_booking() throws Exception {

		when(bookingService.get(anyInt())).thenReturn(booking);
		when(mockHttpSession.getAttribute("user")).thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.put(new URI(baseUrl+"/cancel/1")).session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		ArgumentCaptor<Booking> bookingArgumentCaptor=ArgumentCaptor.forClass(Booking.class);

		verify(bookingService).update(bookingArgumentCaptor.capture());
		assertEquals("cancelled",bookingArgumentCaptor.getValue().getState());
	}

	//approve
	@Test
	public void should_approve_booking() throws Exception {

		when(bookingService.get(anyInt())).thenReturn(booking);

		mockMvc.perform(MockMvcRequestBuilders.put(new URI(baseUrl+"/approve/1")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		ArgumentCaptor<Booking> bookingArgumentCaptor=ArgumentCaptor.forClass(Booking.class);

		verify(bookingService).update(bookingArgumentCaptor.capture());
		assertEquals("waitPay",bookingArgumentCaptor.getValue().getState());
	}

	//add
	@Test
	public void should_add_booking() throws Exception {


		doNothing().when(bookingService).saveBooking(eq(booking));
		when(mockHttpSession.getAttribute("user")).thenReturn(user);
		when(timeSlotService.get(anyInt())).thenReturn(timeSlot);


		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"/buy/1"))
				.session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		verify(bookingService).saveBooking(any());
	}

	//delete
	@Test
	public void should_delete_booking() throws Exception {
		when(bookingService.get(anyInt())).thenReturn(booking);
		when(mockHttpSession.getAttribute("user")).thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.put(new URI(baseUrl+"/delete/1")).session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		ArgumentCaptor<Booking> bookingArgumentCaptor=ArgumentCaptor.forClass(Booking.class);

		verify(bookingService).update(bookingArgumentCaptor.capture());
		assertEquals("delete",bookingArgumentCaptor.getValue().getState());
	}

	//update
	@Test
	public void should_update_booking() throws Exception {

		String bookingJson = JSONObject.toJSONString(booking);
		when(mockHttpSession.getAttribute("user")).thenReturn(user);


		mockMvc.perform(MockMvcRequestBuilders.put(new URI(baseUrl)).session(mockHttpSession)
				.contentType(MediaType.APPLICATION_JSON).content(bookingJson)
				.session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(MockMvcResultHandlers.print());

		verify(bookingService).update(any());
	}

	//search
	@Test
	public void should_getAll() throws Exception {
		String bookingJson = JSONObject.toJSONString(booking);

		when(bookingService.search(eq(booking),anyInt(),anyInt(),anyInt())).thenReturn(page);
		mockMvc.perform(MockMvcRequestBuilders.post(new URI(baseUrl+"?date=0&start=0&size=9"))
				.contentType(MediaType.APPLICATION_JSON).content(bookingJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		verify(bookingService).search(any(),anyInt(),anyInt(),anyInt());
	}


	//myreview
	@Test
	public void should_review_when_user_is_not_null() throws Exception {
		String bookingJson = JSONObject.toJSONString(booking);

		when(mockHttpSession.getAttribute("user")).thenReturn(user);
		when(bookingService.searchWithoutDelete(eq(booking),anyInt(),anyInt(),anyInt())).thenReturn(page);

		mockMvc.perform(MockMvcRequestBuilders.post(new URI(baseUrl+"/mine?start=0&size=8"))
				.session(mockHttpSession)
				.contentType(MediaType.APPLICATION_JSON).content(bookingJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		verify(bookingService).searchWithoutDelete(any(),anyInt(),anyInt(),anyInt());
	}




}
