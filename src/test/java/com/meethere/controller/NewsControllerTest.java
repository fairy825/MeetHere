package com.meethere.controller;

import com.alibaba.fastjson.JSONObject;
import com.meethere.Application;
import com.meethere.pojo.District;
import com.meethere.pojo.News;
import com.meethere.pojo.User;
import com.meethere.service.NewsService;
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
 * @create: 2019-12-16 15:29
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class NewsControllerTest {
	@Autowired
	private WebApplicationContext webApplicationContext;
	@Mock
	private Page4Navigator<User> page;
	@Mock
	MockHttpSession mockHttpSession;
	private MockMvc mockMvc;

	@MockBean
	private NewsService newsService;

	public static final String baseUrl="/news";

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//		news=new News.NewsBuilder().id(1).build();
	}

	//list
	@Test
	public void should_list_newss_of_first_page() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"?keyword=nn&start=0&size=8")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());
		ArgumentCaptor<Integer> intArgCaptor1 = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> intArgCaptor2 = ArgumentCaptor.forClass(Integer.class);
		verify(newsService).list(anyString(),intArgCaptor1.capture(),intArgCaptor2.capture(),eq(5));
		assertEquals(Integer.valueOf(0), intArgCaptor1.getValue());
		assertEquals(Integer.valueOf(8), intArgCaptor2.getValue());
	}

	//add
	@Test
	public void should_add_news() throws Exception {

		News news=new News.NewsBuilder().id(1).createDate(new Date()).title("t1").build();
		String newsJson = JSONObject.toJSONString(news);

		doNothing().when(newsService).saveNews(eq(news));

		mockMvc.perform(MockMvcRequestBuilders.post(new URI(baseUrl))
				.contentType(MediaType.APPLICATION_JSON).content(newsJson))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("t1"))
				.andDo(MockMvcResultHandlers.print());

		verify(newsService).saveNews(any());
	}

	//delete
	@Test
	public void should_delete_news() throws Exception {

		doNothing().when(newsService).delete(anyInt());

		mockMvc.perform(MockMvcRequestBuilders.delete(new URI(baseUrl+"/1")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andDo(MockMvcResultHandlers.print());

		verify(newsService).delete(anyInt());
	}

	//get
	@Test
	public void should_get_news() throws Exception {

		News news=new News.NewsBuilder().id(1).createDate(new Date()).title("t1").build();

		when(newsService.get(anyInt())).thenReturn(news);

		mockMvc.perform(MockMvcRequestBuilders.get(new URI(baseUrl+"/1")))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value("t1"))
				.andDo(MockMvcResultHandlers.print());

		verify(newsService).get(anyInt());
	}

}
