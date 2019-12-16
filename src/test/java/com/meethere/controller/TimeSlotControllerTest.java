package com.meethere.controller;

import com.alibaba.fastjson.JSONObject;
import com.meethere.Application;
import com.meethere.dao.TimeSlotDAO;
import com.meethere.dao.VenueDAO;
import com.meethere.pojo.TimeSlot;
import com.meethere.pojo.User;
import com.meethere.pojo.Venue;
import com.meethere.service.TimeSlotService;
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

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-16 15:47
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TimeSlotControllerTest {
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Mock
	private Page4Navigator<User> page;
	@Mock
	MockHttpSession mockHttpSession;
	HttpServletRequest req;

	@MockBean
	private TimeSlotDAO timeSlotDAO;

	@MockBean
	private TimeSlotService timeSlotService;
	@MockBean
	VenueDAO venueDAO;
	@MockBean
	private VenueService venueService;

	private Venue venue;
	private List<Venue> venueList=new ArrayList<>();
	private TimeSlot timeSlot;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		req=mock(HttpServletRequest.class);

		venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();
//		timeSlot=new TimeSlot.TimeSlotBuilder().id(1).beginTime(1).endTime(9).seat(100).venue(venue).build();
		venueList.add(venue);
		venueList.add(venue);
	}
	//list
	@Test
	public void should_list_timeslot_of_first_page() throws Exception {
		TimeSlot timeSlot1=new TimeSlot.TimeSlotBuilder().id(1).beginTime(1).endTime(9).seat(100).venue(venue).build();

		List<TimeSlot> timeSlots=new ArrayList<>();
		timeSlots.add(timeSlot1);
		when(timeSlotService.list(anyInt(),anyInt())).thenReturn(timeSlots);

		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/venues/1/timeSlots")).param("date","1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

	}

	//update
	@Test
	public void should_not_update_profile_when_not_login() throws Exception {
		TimeSlot timeSlot1=new TimeSlot.TimeSlotBuilder().id(1).beginTime(1).endTime(9).seat(100).venue(venue).build();

		String timeJson = JSONObject.toJSONString(timeSlot1);

		mockMvc.perform(MockMvcRequestBuilders.put(new URI("/timeSlots"))
				.contentType(MediaType.APPLICATION_JSON).content(timeJson)
				.session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(MockMvcResultHandlers.print());

		verify(timeSlotService).update(any());
	}
}
