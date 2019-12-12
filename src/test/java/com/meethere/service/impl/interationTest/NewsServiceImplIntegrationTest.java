package com.meethere.service.impl.interationTest;

import com.meethere.Application;
import com.meethere.dao.NewsDAO;
import com.meethere.pojo.News;
import com.meethere.service.NewsService;
import com.meethere.util.Page4Navigator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-12 20:38
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class NewsServiceImplIntegrationTest {

	@Autowired
	private NewsDAO newsDAO;
	@Autowired
	private NewsService newsService;


	@Before
	public void setUp() {
	}

	@Test
	@Transactional
	@Rollback
	public void database_integrity_test() {
		News news1=new News.NewsBuilder().id(1).title("t1").build();
		News news2=new News.NewsBuilder().id(2).title("t2").build();
		News news3=new News.NewsBuilder().id(3).title("t3").build();

		newsDAO.save(news1);
		newsDAO.save(news2);
		newsDAO.save(news3);

		//list
		Page4Navigator<News> entries= newsService.list(null,0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(3,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(3,entries.getContent().size());
		assertEquals(news1.getId(),entries.getContent().get(0).getId());

		entries= newsService.list("",0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(3,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(3,entries.getContent().size());
		assertEquals(news1.getId(),entries.getContent().get(0).getId());

		entries= newsService.list("t1",0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(1,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(1,entries.getContent().size());
		assertEquals("t1",entries.getContent().get(0).getTitle());

		entries= newsService.list("t",0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(3,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(3,entries.getContent().size());

		//saveNews
		newsService.saveNews(news1);
		assertEquals(news1.getCreateDate().toString().substring(0,15),new Date().toString().substring(0,15));

	}
}
