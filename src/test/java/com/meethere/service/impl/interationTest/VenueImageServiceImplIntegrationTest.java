package com.meethere.service.impl.interationTest;

import com.meethere.Application;
import com.meethere.dao.VenueDAO;
import com.meethere.dao.VenueImageDAO;
import com.meethere.pojo.TimeSlot;
import com.meethere.pojo.Venue;
import com.meethere.pojo.VenueImage;
import com.meethere.service.VenueImageService;
import com.meethere.service.VenueService;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-12 21:23
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class VenueImageServiceImplIntegrationTest {
	@Autowired
	VenueImageDAO venueImageDAO;
	@Autowired
	VenueDAO venueDAO;
	@Autowired
	private VenueImageService venueImageService;

	private Venue venue;
	private Venue venue1;
	private Venue venue2;

	private List<Venue> venueList;

	@Before
	public void setUp() {
		venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();
		venue1=new Venue.VenueBuilder().id(2).name("vn2").build();
		venue2=new Venue.VenueBuilder().id(3).name("vn3").build();

		venueList=new ArrayList<>();

		venueList.add(venue);
		venueList.add(venue1);
	}

	@Test
	@Transactional
	@Rollback
	public void database_integrity_test()  {
		venue=venueDAO.save(venue);
		venue1=venueDAO.save(venue1);


		VenueImage venueImage1=new VenueImage.VenueImageBuilder().id(1).venue(venue).build();
		VenueImage venueImage2=new VenueImage.VenueImageBuilder().id(2).venue(venue).build();
		VenueImage venueImage3=new VenueImage.VenueImageBuilder().id(3).venue(venue1).build();

		venueImage1=venueImageDAO.save(venueImage1);
		venueImage2=venueImageDAO.save(venueImage2);
		venueImage3=venueImageDAO.save(venueImage3);

		//listByVenue
		List<VenueImage> entries= venueImageService.listByVenue(venue.getId());
		assertEquals(2,entries.size());
		assertEquals(venue,entries.get(0).getVenue());

		entries= venueImageService.listByVenue(100);
		assertEquals(0,entries.size());

		//setFirstVenueImage
		venueImageService.setFirstVenueImage(venue);
		assertEquals(venueImage1,venue.getFirstVenueImage());

		venueImageService.setFirstVenueImage(venue2);
		assertNull(venue.getFirstVenueImage().getVenue());

	}
}
