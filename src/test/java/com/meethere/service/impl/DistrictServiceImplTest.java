package com.meethere.service.impl;

import com.meethere.Application;
import com.meethere.dao.DistrictDAO;
import com.meethere.service.DistrictService;
import com.meethere.service.VenueService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-10 20:30
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DistrictServiceImplTest {
	@MockBean
	private DistrictDAO districtDAO;
	@Autowired
	private DistrictService districtService;


	private Page pageFromJPA;

	@Before
	public void setUp() {
		pageFromJPA = mock(Page.class);
	}

	@Test
	public void list_when_key_blank_or_undefined() {
		when(districtDAO.findAll((Pageable)any())).thenReturn(pageFromJPA);
		when(districtDAO.findByNameLike(anyString(),any())).thenReturn(pageFromJPA);

		districtService.list("",1,8,5);
		districtService.list(null,1,8,5);
		districtService.list("undefined",1,8,5);


		verify(districtDAO,times(3)).findAll((Pageable)any());
		verify(districtDAO,times(0)).findByNameLike(anyString(),(Pageable) any());
	}

	@Test
	public void list_when_key_not_blank_and_undefined() {
		when(districtDAO.findAll((Pageable)any())).thenReturn(pageFromJPA);
		when(districtDAO.findByNameLike(anyString(),any())).thenReturn(pageFromJPA);

		districtService.list("dis",1,8,5);

		verify(districtDAO,times(0)).findAll((Pageable)any());
		verify(districtDAO,times(1)).findByNameLike(anyString(),(Pageable) any());
	}
}
