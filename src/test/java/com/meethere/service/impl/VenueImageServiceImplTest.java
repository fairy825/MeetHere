package com.meethere.service.impl;

import com.meethere.Application;
import com.meethere.dao.VenueDAO;
import com.meethere.dao.VenueImageDAO;
import com.meethere.pojo.TimeSlot;
import com.meethere.pojo.Venue;
import com.meethere.pojo.VenueImage;
import com.meethere.service.VenueImageService;
import com.meethere.service.VenueService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-11 16:11
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class VenueImageServiceImplTest {

	@MockBean
	VenueImageDAO venueImageDAO;
	@MockBean
	VenueDAO venueDAO;
	@MockBean
	private VenueService venueService;
	@Autowired
	private VenueImageService venueImageService;

	private Venue venue;
	private VenueImage venueImage;
	private List<Venue> venueList;
	private List<VenueImage> venueImageList1;
	private List<VenueImage> venueImageList2;

	private Page pageFromJPA;
	private TimeSlot timeSlot;

	@Before
	public void setUp() {
		pageFromJPA = mock(Page.class);
		venue=new Venue.VenueBuilder().id(1).name("vn").startTime(1).endTime(9).totalSeat(100).build();
		venueImage=new VenueImage.VenueImageBuilder().id(1).venue(venue).build();
		timeSlot=new TimeSlot.TimeSlotBuilder().id(1).beginTime(1).endTime(9).seat(100).venue(venue).build();
		venueImageList1=new ArrayList<>();
		venueImageList1.add(venueImage);

		venueImageList2=new ArrayList<>();

		venueList=new ArrayList<>();

		venueList.add(venue);
		venueList.add(venue);
	}

	@Test
	public void list_by_venue(){
		when(venueService.get(anyInt())).thenReturn(venue);
		when(venueImageDAO.findByVenue(venue)).thenReturn(venueImageList1);

		List<VenueImage> actualList=venueImageService.listByVenue(1);

		ArgumentCaptor<Venue> venueArgumentCaptor=ArgumentCaptor.forClass(Venue.class);
		verify(venueImageDAO).findByVenue(venueArgumentCaptor.capture());

		assertEquals(venueArgumentCaptor.getValue(),venue);
		assertEquals(actualList,venueImageList1);
	}

	@Test
	public void should_return_newImage_when_list_empty(){
		when(venueService.get(anyInt())).thenReturn(venue);
		when(venueImageDAO.findByVenue(venue)).thenReturn(venueImageList2);

		venueImageService.setFirstVenueImage(venue);
		VenueImage vi=venue.getFirstVenueImage();

		//new Image
		assertNull(vi.getVenue());
	}

	@Test
	public void should_return_firstImage_when_list_not_empty(){
		when(venueService.get(anyInt())).thenReturn(venue);
		when(venueImageDAO.findByVenue(venue)).thenReturn(venueImageList1);

		venueImageService.setFirstVenueImage(venue);
		VenueImage vi=venue.getFirstVenueImage();

		//first Image
		assertEquals(vi,venueImage);
	}

	@Test
	public void should_set_right_count_images(){
		when(venueService.get(anyInt())).thenReturn(venue);
		when(venueImageDAO.findByVenue(venue)).thenReturn(venueImageList1);

		venueImageService.setFirstVenueImages(venueList);

		verify(venueService,times(2)).get(anyInt());
		verify(venueImageDAO,times(2)).findByVenue(venue);
	}
}
