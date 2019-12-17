package com.meethere.service.impl;

import com.meethere.Application;
import com.meethere.dao.DistrictDAO;
import com.meethere.pojo.District;
import com.meethere.pojo.TimeSlot;
import com.meethere.pojo.Venue;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

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
	private District district;
	private Venue venue;
	private List<Venue> venueList=new ArrayList<>();

	@Before
	public void setUp() {
		pageFromJPA = mock(Page.class);
		venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();
		venueList.add(venue);
		venueList.add(venue);
		district=new District.DistrictBuilder().id(1).name("dis1").venues(venueList).build();

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

	@Test
	public void should_update_success() throws ParseException {
		when(districtDAO.save(district)).thenReturn(district);

		districtService.update(district);

		verify(districtDAO).save(district);
	}

	@Test
	public void should_get_success() throws ParseException {
		when(districtDAO.findOne(anyInt())).thenReturn(district);

		districtService.get(1);

		verify(districtDAO).findOne(1);
	}

	@Test
	public void should_save_success() throws ParseException {
		when(districtDAO.save(district)).thenReturn(district);

		districtService.saveDistrict(district);

		verify(districtDAO).save(district);
	}
}
