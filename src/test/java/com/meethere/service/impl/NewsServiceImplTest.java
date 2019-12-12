package com.meethere.service.impl;

import com.meethere.Application;
import com.meethere.dao.DistrictDAO;
import com.meethere.dao.NewsDAO;
import com.meethere.pojo.News;
import com.meethere.service.DistrictService;
import com.meethere.service.NewsService;
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

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-10 21:42
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class NewsServiceImplTest {
	@MockBean
	private NewsDAO newsDAO;
	@Autowired
	private NewsService newsService;

	private Page pageFromJPA;
	private News news;

	@Before
	public void setUp() {
		pageFromJPA = mock(Page.class);
		news=new News.NewsBuilder().id(1).build();
	}

	@Test
	public void list_when_key_blank_or_undefined() {
		when(newsDAO.findAll((Pageable)any())).thenReturn(pageFromJPA);
		when(newsDAO.findByTitleLike(anyString(),any())).thenReturn(pageFromJPA);

		newsService.list("",1,8,5);
		newsService.list(null,1,8,5);
		newsService.list("undefined",1,8,5);


		verify(newsDAO,times(3)).findAll((Pageable)any());
		verify(newsDAO,times(0)).findByTitleLike(anyString(),(Pageable) any());
	}

	@Test
	public void list_when_key_not_blank_and_undefined() {
		when(newsDAO.findAll((Pageable)any())).thenReturn(pageFromJPA);
		when(newsDAO.findByTitleLike(anyString(),any())).thenReturn(pageFromJPA);

		newsService.list("dis",1,8,5);

		verify(newsDAO,times(0)).findAll((Pageable)any());
		verify(newsDAO,times(1)).findByTitleLike(anyString(),(Pageable) any());
	}

	@Test
	public void date_should_be_now(){
		when(newsDAO.save(news)).thenReturn(news);

		newsService.saveNews(news);

		ArgumentCaptor<News> newsArgumentCaptor=ArgumentCaptor.forClass(News.class);
		verify(newsDAO).save(newsArgumentCaptor.capture());

		assertEquals(newsArgumentCaptor.getValue().getCreateDate().toString().substring(0,15),new Date().toString().substring(0,15));
	}
}
