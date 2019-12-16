package com.meethere.controller;

import com.alibaba.fastjson.JSONObject;
import com.meethere.Application;
import com.meethere.dao.MessageDAO;
import com.meethere.pojo.Booking;
import com.meethere.pojo.Message;
import com.meethere.pojo.User;
import com.meethere.pojo.Venue;
import com.meethere.service.BookingService;
import com.meethere.service.MessageService;
import com.meethere.service.VenueService;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-16 14:09
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MessageControllerTest {
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Mock
	private Page4Navigator<User> page;
	@Mock
	MockHttpSession mockHttpSession;
	private MockMvc mockMvc;

	@MockBean
	private BookingService bookingService;
	@MockBean
	private MessageService messageService;
	@MockBean
	private VenueService venueService;

	private Venue venue;
	private Booking existBooking;
	private Booking nullBooking;
	private Message message;
	private User user;

	public static final String baseUrl="/messages";

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		user = new User.UserBuilder().id(1).name("mh").password("12345").phoneNumber("12345678901")
				.email("6666@meethere.com").nickname("meethere")
				.faceImage("img/faceImage/20190906.jpg").build();
		venue=new Venue.VenueBuilder().id(1).name("vn").build();

		existBooking=new Booking.BookingBuilder().user(user).state(BookingService.waitReview).id(1).build();
		nullBooking=new Booking.BookingBuilder().id(0).build();
		message=new Message.MessageBuilder().id(1).state(MessageService.refused).build();

	}

	//listByVenue
	@Test
	public void should_list_by_venues_of_first_page() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/venues/1/messages?start=0&size=8")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		verify(messageService).listByVenue(anyInt(),anyInt(),anyInt(),anyInt(),eq(5));

	}

	//show
	@Test
	public void should_show_by_venues_of_first_page() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/venues/1/messages/show?start=0&size=8")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		verify(messageService).listByVenue(anyInt(),anyInt(),anyInt(),anyInt(),eq(5));
	}

	//myreview_1
	@Test
	public void should_review_fail_when_user_is_null() throws Exception {
		when(mockHttpSession.getAttribute("user")).thenReturn(null);
		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"/mine?bid=1")).session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("未登录"))
				.andDo(MockMvcResultHandlers.print());
	}

	//myreview_2
	@Test
	public void should_review_when_user_is_not_null() throws Exception {
		when(mockHttpSession.getAttribute("user")).thenReturn(user);
		when(bookingService.get(anyInt())).thenReturn(existBooking);
		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"/mine?bid=1")).session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		verify(messageService).queryByBooking(existBooking);
	}

	//agree
	@Test
	public void should_agree() throws Exception {
		when(messageService.get(anyInt())).thenReturn(message);
		mockMvc.perform(MockMvcRequestBuilders.put(new URI(baseUrl+"/agree/1")).session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());
		ArgumentCaptor<Message> messageArgumentCaptor=ArgumentCaptor.forClass(Message.class);


		verify(messageService).update(messageArgumentCaptor.capture());
		assertEquals(MessageService.pass,messageArgumentCaptor.getValue().getState());
	}

	//refuse
	@Test
	public void should_refuse() throws Exception {
		when(messageService.get(anyInt())).thenReturn(message);
		mockMvc.perform(MockMvcRequestBuilders.put(new URI(baseUrl+"/refuse/1")).session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());
		ArgumentCaptor<Message> messageArgumentCaptor=ArgumentCaptor.forClass(Message.class);


		verify(messageService).update(messageArgumentCaptor.capture());
		assertEquals(MessageService.refused,messageArgumentCaptor.getValue().getState());
	}

	//add
	@Test
	public void should_add() throws Exception {
		when(messageService.get(anyInt())).thenReturn(message);
		when(mockHttpSession.getAttribute("user")).thenReturn(user);
		when(bookingService.get(anyInt())).thenReturn(existBooking);
		when(venueService.get(anyInt())).thenReturn(venue);

		String messageJson= JSONObject.toJSONString(message);

		mockMvc.perform(MockMvcRequestBuilders.post(new URI(baseUrl+"/1?vid=1&bid=1")).session(mockHttpSession)
				.contentType(MediaType.APPLICATION_JSON).content(messageJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		ArgumentCaptor<Message> messageArgumentCaptor=ArgumentCaptor.forClass(Message.class);
		verify(messageService).saveMessage(messageArgumentCaptor.capture());
		assertEquals(MessageService.waitApprove,messageArgumentCaptor.getValue().getState());
	}

}
