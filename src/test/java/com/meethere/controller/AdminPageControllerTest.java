package com.meethere.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meethere.Application;
import com.meethere.pojo.Admin;
import com.meethere.pojo.User;
import com.meethere.service.AdminService;
import com.meethere.service.UserService;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.MD5Utils;
import com.meethere.util.Page4Navigator;
import com.meethere.util.RedisOperator;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-14 22:00
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AdminPageControllerTest {
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Mock
	private Page4Navigator<User> page;
	@Mock
	MockHttpSession mockHttpSession;
	@MockBean
	RedisOperator mockRedis;
	private Admin admin;

	private MockMvc mockMvc;

	@Before//对应jnuit5的beforeEach 4的@beforeClass对应5的beforeAll
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		admin = new Admin.AdminBuilder().id(1).name("a").password("123456").build();
	}

	@Test
	public void should_return_first() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/first")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
				//返回的html视图名 first.html
				.andExpect(MockMvcResultMatchers.view().name("first"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void should_return_index() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/")))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("first"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void should_return_alogin() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/alogin")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.view().name("admin/adminLogin"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void should_return_alogout() throws Exception {
		when(mockHttpSession.getAttribute("admin")).thenReturn(admin);
		doNothing().when(mockRedis).del(anyString());
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/alogout")).session(mockHttpSession))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("first"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void should_return_admin() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/admin")))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("admin_district_list"))
				.andDo(MockMvcResultHandlers.print());

	}
	@Test
	public void should_return_admin_district_list() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/admin_district_list")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.view().name("admin/listDistrict"))
				.andDo(MockMvcResultHandlers.print());


	}
	@Test
	public void should_return_admin_district_edit() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/admin_district_edit")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.view().name("admin/editDistrict"))
				.andDo(MockMvcResultHandlers.print());
	}
	@Test
	public void should_return_admin_message_list() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/admin_message_list")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.view().name("admin/listMessage"))
				.andDo(MockMvcResultHandlers.print());
	}
	@Test
	public void should_return_admin_venue_list() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/admin_venue_list")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.view().name("admin/listVenue"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void should_return_admin_news_list() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/admin_news_list")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.view().name("admin/listNews"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void should_return_admin_venue_edit() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/admin_venue_edit")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.view().name("admin/editVenue"))
				.andDo(MockMvcResultHandlers.print());
	}
	@Test
	public void should_return_admin_venueImage_list() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/admin_venueImage_list")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.view().name("admin/listVenueImage"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void should_return_admin_user_list() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/admin_user_list")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.view().name("admin/listUser"))
				.andDo(MockMvcResultHandlers.print());
	}
	@Test
	public void should_return_admin_timeSlot_list() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/admin_timeSlot_list")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.view().name("admin/listTimeSlot"))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void should_return_admin_booking_list() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI("/admin_booking_list")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
				.andExpect(MockMvcResultMatchers.view().name("admin/listBooking"))
				.andDo(MockMvcResultHandlers.print());
	}



}
