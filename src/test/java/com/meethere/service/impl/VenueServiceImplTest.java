package com.meethere.service.impl;

import com.meethere.Application;
import com.meethere.dao.VenueDAO;
import com.meethere.pojo.District;
import com.meethere.pojo.Venue;
import com.meethere.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-11 17:02
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class VenueServiceImplTest {
	@MockBean
	VenueDAO venueDAO;
	@MockBean
	DistrictService districtService;
	@MockBean
	MessageService messageService;
	@MockBean
	BookingService bookingService;
	@MockBean
	TimeSlotService timeSlotService;
	@Autowired
	VenueService venueService;

	private Page pageFromJPA;
	private District district;
	private Venue venue;
	private List<Venue> venueList;


	@Before
	public void setUp() {
		pageFromJPA = mock(Page.class);
		venue=new Venue.VenueBuilder().id(1).price(2f).name("vn").startTime(1).endTime(9).totalSeat(100).build();
		venueList=new ArrayList<>();
		venueList.add(venue);
		venueList.add(venue);
		district=new District.DistrictBuilder().id(1).name("dis1").venues(venueList).build();

	}

	@Test
	public void list_by_district_when_did_is_0(){
		when(districtService.get(anyInt())).thenReturn(district);
		when(venueDAO.findByDistrict(eq(district),any())).thenReturn(pageFromJPA);
		when(venueDAO.findAll((Pageable)any())).thenReturn(pageFromJPA);

		venueService.listByDistrict(0,1,5,8);

		verify(venueDAO,times(1)).findAll((Pageable)any());
		verify(venueDAO,times(0)).findByDistrict(eq(district),any());

	}

	@Test
	public void list_by_district_when_did_is_not_0(){
		when(districtService.get(anyInt())).thenReturn(district);
		when(venueDAO.findByDistrict(eq(district),any())).thenReturn(pageFromJPA);
		when(venueDAO.findAll((Pageable)any())).thenReturn(pageFromJPA);

		venueService.listByDistrict(1,1,5,8);

		verify(venueDAO,times(0)).findAll((Pageable)any());
		verify(venueDAO,times(1)).findByDistrict(eq(district),any());

	}

	@Test
	public void list(){
		when(venueDAO.findAll((Pageable)any())).thenReturn(pageFromJPA);

		venueService.list(1,5,8);

		verify(venueDAO).findAll((Pageable)any());
	}

	@Test
	public void should_get_null_district_when_did_is_0(){
		when(districtService.get(0)).thenReturn(null);
		when(venueDAO.findByDistrict(eq(district),any())).thenReturn(pageFromJPA);

		venueService.search(0,null,venue,0,100,1,5,8);

		ArgumentCaptor<Example> exampleArgumentCaptor=ArgumentCaptor.forClass(Example.class);
		verify(venueDAO).findAll(exampleArgumentCaptor.capture(),(Sort)any());
		assertNull(((Venue)(exampleArgumentCaptor.getValue().getProbe())).getDistrict());
	}

	@Test
	public void should_get_right_district_when_did_is_not_0(){
		when(districtService.get(1)).thenReturn(district);
		when(venueDAO.findAll(any(),(Sort) any())).thenReturn(venueList);

		venueService.search(1,null,venue,0,100,1,5,8);

		ArgumentCaptor<Example> exampleArgumentCaptor=ArgumentCaptor.forClass(Example.class);
		verify(venueDAO).findAll(exampleArgumentCaptor.capture(),(Sort)any());
		assertEquals(((Venue)(exampleArgumentCaptor.getValue().getProbe())).getDistrict(),district);
	}

	@Test
	public void should_create_3_times_when_save_venue() throws ParseException {
		when(venueDAO.save(venue)).thenReturn(venue);

		venueService.saveVenue(venue);

		verify(timeSlotService).createByVenue(venue,0);
		verify(timeSlotService).createByVenue(venue,1);
		verify(timeSlotService).createByVenue(venue,2);

	}



}
