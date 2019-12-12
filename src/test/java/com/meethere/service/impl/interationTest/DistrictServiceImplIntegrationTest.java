package com.meethere.service.impl.interationTest;

import com.meethere.Application;
import com.meethere.dao.DistrictDAO;
import com.meethere.pojo.District;
import com.meethere.pojo.Venue;
import com.meethere.service.DistrictService;
import com.meethere.util.Page4Navigator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-12 19:59
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DistrictServiceImplIntegrationTest {

	@Autowired
	private DistrictDAO districtDAO;
	@Autowired
	private DistrictService districtService;

	private Venue venue;
	private List<Venue> venues;

	@Before
	public void setUp() {
		venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();
		venues=new ArrayList<>();
		venues.add(venue);
		venues.add(venue);
	}

	@Test
	@Transactional
	@Rollback
	public void database_integrity_test() {
		District district1=new District.DistrictBuilder().id(1).name("ds1").venues(venues).build();
		District district2=new District.DistrictBuilder().id(2).name("ds2").venues(venues).build();
		District district3=new District.DistrictBuilder().id(3).name("ds3").venues(venues).build();

		districtDAO.save(district1);
		districtDAO.save(district2);
		districtDAO.save(district3);

		//list
		Page4Navigator<District> entries= districtService.list(null,0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(3,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(3,entries.getContent().size());
		assertEquals(district1.getId(),entries.getContent().get(0).getId());

		entries= districtService.list("",0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(3,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(3,entries.getContent().size());
		assertEquals(district1.getId(),entries.getContent().get(0).getId());

		entries= districtService.list("ds1",0,8,5);
		assertEquals(8,entries.getSize());
		assertEquals(1,entries.getTotalElements());
		assertEquals(1,entries.getTotalPages());
		assertEquals(1,entries.getContent().size());
		assertEquals("ds1",entries.getContent().get(0).getName());


	}
}
