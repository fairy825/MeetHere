package com.meethere.controller;

import com.alibaba.fastjson.JSONObject;
import com.meethere.Application;
import com.meethere.dao.DistrictDAO;
import com.meethere.pojo.District;
import com.meethere.pojo.User;
import com.meethere.service.DistrictService;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-15 18:53
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DistrictControllerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private DistrictDAO districtDAO;
	@MockBean
	private DistrictService districtService;
	@Mock
	private Page4Navigator<User> page;
	@Mock
	MockHttpSession mockHttpSession;
	private MockMvc mockMvc;
	private User user;

	public static final String baseUrl="/districts";



	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

	}

	//list
	@Test
	public void should_list_users_of_first_page() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"?keyword="+"an"+"&start=0&size=8")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());
		ArgumentCaptor<Integer> intArgCaptor1 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> intArgCaptor2 = ArgumentCaptor.forClass(Integer.class);
		verify(districtService).list(anyString(),intArgCaptor1.capture(),intArgCaptor2.capture(),eq(5));
		assertEquals(Integer.valueOf(0), intArgCaptor1.getValue());
		assertEquals(Integer.valueOf(8), intArgCaptor2.getValue());
	}

	//listAll
	@Test
	public void should_list_all() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"/all")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());
		verify(districtService).findAll();
	}

	//add
	@Test
	public void should_add_district() throws Exception {

		District district1=new District.DistrictBuilder().id(1).name("ds1").build();
		String districtJson = JSONObject.toJSONString(district1);

		doNothing().when(districtService).saveDistrict(eq(district1));

		mockMvc.perform(MockMvcRequestBuilders.post(new URI(baseUrl))
				.contentType(MediaType.APPLICATION_JSON).content(districtJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("ds1"))
				.andDo(MockMvcResultHandlers.print());

		verify(districtService).saveDistrict(any());
	}

	//delete
	@Test
	public void should_delete_district() throws Exception {

		doNothing().when(districtService).delete(anyInt());

		mockMvc.perform(MockMvcRequestBuilders.delete(new URI(baseUrl)+"/1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		verify(districtService).delete(anyInt());
	}

	//get
	@Test
	public void should_get_district() throws Exception {

		District district1=new District.DistrictBuilder().id(1).name("ds1").build();

		when(districtService.get(anyInt())).thenReturn(district1);

		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"/1")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("ds1"))
				.andDo(MockMvcResultHandlers.print());

		verify(districtService).get(anyInt());
	}

	//update
	@Test
	public void should_update_district() throws Exception {

		District district1=new District.DistrictBuilder().id(1).name("ds1").build();
		String districtJson = JSONObject.toJSONString(district1);

		doNothing().when(districtService).update(district1);

		mockMvc.perform(MockMvcRequestBuilders.put(new URI(baseUrl))
				.contentType(MediaType.APPLICATION_JSON).content(districtJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("ds1"))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		verify(districtService).update(any());
	}




}
