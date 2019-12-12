package com.meethere.service.impl.interationTest;

import com.meethere.Application;
import com.meethere.dao.DistrictDAO;
import com.meethere.dao.VenueDAO;
import com.meethere.pojo.District;
import com.meethere.pojo.Venue;
import com.meethere.service.*;
import com.meethere.util.Page4Navigator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-12 21:37
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class VenueServiceImplIntegrationTest {
	@Autowired
	VenueDAO venueDAO;
	@Autowired
	DistrictDAO districtDAO;
	@Autowired
	DistrictService districtService;
	@Autowired
	MessageService messageService;
	@Autowired
	BookingService bookingService;
	@Autowired
	TimeSlotService timeSlotService;
	@Autowired
	VenueService venueService;

	private District district;
//	private Venue venue;
	private List<Venue> venueList;


	@Before
	public void setUp() {
//		venue=new Venue.VenueBuilder().id(1).price(2f).name("vn").startTime(1).endTime(9).totalSeat(100).build();
//		venueList=new ArrayList<>();
//		venueList.add(venue);
//		venueList.add(venue);
//		district=new District.DistrictBuilder().id(1).name("dis1").venues(venueList).build();
	}

	@Test
	@Transactional
	@Rollback
	public void database_integrity_test()  {
		Venue venue1=new Venue.VenueBuilder().id(1).price(2f).name("vn1").startTime(1).endTime(9).totalSeat(100).build();
		Venue venue2=new Venue.VenueBuilder().id(2).price(2f).name("vn2").startTime(1).endTime(9).totalSeat(100).build();
		Venue venue3=new Venue.VenueBuilder().id(3).price(2f).name("vn3").startTime(1).endTime(9).totalSeat(100).build();

		venueList=new ArrayList<>();
		venueList.add(venue1);
		venueList.add(venue2);
//		venueList.add(venue3);

		district=new District.DistrictBuilder().id(1).name("dis1").venues(venueList).build();

		districtDAO.save(district);


		venueDAO.save(venue1);
		venueDAO.save(venue2);
		venueDAO.save(venue3);

		//list
		Page4Navigator<Venue> entries= venueService.list(0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(3,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(3,entries.getContent().size());
		assertEquals(venue1.getId(),entries.getContent().get(0).getId());
		assertEquals(venue2.getId(),entries.getContent().get(1).getId());
		assertEquals(venue3.getId(),entries.getContent().get(2).getId());

		//listByDistrict
		entries= venueService.listByDistrict(1,0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(2,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(2,entries.getContent().size());

		entries= venueService.listByDistrict(2,0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(0,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(0,entries.getContent().size());

	}
}
