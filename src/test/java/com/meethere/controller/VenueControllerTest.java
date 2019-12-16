package com.meethere.controller;

import com.alibaba.fastjson.JSONObject;
import com.meethere.Application;
import com.meethere.dao.TimeSlotDAO;
import com.meethere.dao.VenueDAO;
import com.meethere.pojo.*;
import com.meethere.service.TimeSlotService;
import com.meethere.service.VenueImageService;
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
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-16 16:51
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class VenueControllerTest {
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext webApplicationContext;

	private Page4Navigator<Venue> page=new Page4Navigator<>();
	@Mock
	MockHttpSession mockHttpSession;
	HttpServletRequest req;

	@MockBean
	private VenueImageService venueImageService;
	@MockBean
	VenueDAO venueDAO;
	@MockBean
	private VenueService venueService;

	private Venue venue;
	private List<Venue> venueList=new ArrayList<>();
	private TimeSlot timeSlot;

	public static final String baseUrl="/venues";

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();
//		timeSlot=new TimeSlot.TimeSlotBuilder().id(1).beginTime(1).endTime(9).seat(100).venue(venue).build();
		venueList.add(venue);
		venueList.add(venue);
	}


	//list
	@Test
	public void should_list_venues_by_district_of_first_page() throws Exception {

		when(venueService.listByDistrict(anyInt(),anyInt(),anyInt(),anyInt())).thenReturn(page);

		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/districts/1/venues?start=0&size=8")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		ArgumentCaptor<Integer> intArgCaptor1 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> intArgCaptor2 = ArgumentCaptor.forClass(Integer.class);


		verify(venueService).listByDistrict(eq(1),intArgCaptor1.capture(),intArgCaptor2.capture(),eq(5));
		assertEquals(Integer.valueOf(0), intArgCaptor1.getValue());
		assertEquals(Integer.valueOf(8), intArgCaptor2.getValue());
	}

	//listAll
	@Test
	public void should_list_allVenues_of_first_page() throws Exception {

		when(venueService.list(anyInt(),anyInt(),anyInt())).thenReturn(page);
		doNothing().when(venueService).setSaleAndReviewNumber(venue);
		doNothing().when(venueImageService).setFirstVenueImages(any());


		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"?start=0&size=8")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		ArgumentCaptor<Integer> intArgCaptor1 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> intArgCaptor2 = ArgumentCaptor.forClass(Integer.class);


		verify(venueService).list(intArgCaptor1.capture(),intArgCaptor2.capture(),eq(5));
		assertEquals(Integer.valueOf(0), intArgCaptor1.getValue());
		assertEquals(Integer.valueOf(8), intArgCaptor2.getValue());

	}

	//update
	@Test
	public void should_update_venues() throws Exception {
		Venue venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();

		String venueJson = JSONObject.toJSONString(venue);

		mockMvc.perform(MockMvcRequestBuilders.put(new URI(baseUrl))
				.contentType(MediaType.APPLICATION_JSON).content(venueJson)
				.session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(MockMvcResultHandlers.print());

		verify(venueService).update(any());
	}

	//get
	@Test
	public void should_get_venues() throws Exception {

		Venue venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();

		when(venueService.get(anyInt())).thenReturn(venue);
		doNothing().when(venueService).setSaleAndReviewNumber(venue);
		doNothing().when(venueImageService).setFirstVenueImage(venue);

		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"/1")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("vn"))
				.andDo(MockMvcResultHandlers.print());

		verify(venueService).get(anyInt());
		verify(venueService).setSaleAndReviewNumber(venue);
		verify(venueImageService).setFirstVenueImage(venue);

	}

	//getDetail
	@Test
	public void should_get_venueDetail() throws Exception {

		Venue venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();

		when(venueService.get(anyInt())).thenReturn(venue);
		doNothing().when(venueService).setSaleAndReviewNumber(venue);
		doNothing().when(venueImageService).setFirstVenueImage(venue);

		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"/1")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("vn"))
				.andDo(MockMvcResultHandlers.print());

		verify(venueService).get(anyInt());
		verify(venueService).setSaleAndReviewNumber(venue);
		verify(venueImageService).setFirstVenueImage(venue);

	}

	//add
	@Test
	public void should_add_district() throws Exception {

		Venue venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();
		String venueJson = JSONObject.toJSONString(venue);

		doNothing().when(venueService).saveVenue(eq(venue));

		mockMvc.perform(MockMvcRequestBuilders.post(new URI(baseUrl))
				.contentType(MediaType.APPLICATION_JSON).content(venueJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("vn"))
				.andDo(MockMvcResultHandlers.print());

		verify(venueService).saveVenue(any());
	}

	//delete
	@Test
	public void should_delete_district() throws Exception {

		doNothing().when(venueService).delete(anyInt());

		mockMvc.perform(MockMvcRequestBuilders.delete(new URI(baseUrl)+"/1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		verify(venueService).delete(anyInt());
	}

	//search2
	@Test
	public void should_search_by_user() throws Exception {

		when(venueService.searchByKeyword(anyInt(),anyString(),anyString(),anyInt(),anyInt(),anyInt(),anyInt(),anyInt())).thenReturn(page);
		doNothing().when(venueImageService).setFirstVenueImages(page.getContent());

		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/districts/1/venues/sort/keyword")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		verify(venueService).searchByKeyword(anyInt(),anyString(),anyString(),anyInt(),anyInt(),anyInt(),anyInt(),anyInt());
	}

	//search3
	@Test
	public void should_search_by_admin() throws Exception {

		when(venueService.searchByKeyword(anyInt(),anyString(),anyString(),anyInt(),anyInt(),anyInt(),anyInt(),anyInt())).thenReturn(page);
		doNothing().when(venueImageService).setFirstVenueImages(any());

		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/districts/1/venues/keyword")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		verify(venueService).searchByKeyword(anyInt(),anyString(),anyString(),anyInt(),anyInt(),anyInt(),anyInt(),anyInt());
	}


}
