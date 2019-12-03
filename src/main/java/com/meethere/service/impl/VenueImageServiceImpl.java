package com.meethere.service.impl;

import com.meethere.dao.VenueDAO;
import com.meethere.dao.VenueImageDAO;
import com.meethere.pojo.District;
import com.meethere.pojo.Venue;
import com.meethere.pojo.VenueImage;
import com.meethere.service.DistrictService;
import com.meethere.service.VenueImageService;
import com.meethere.service.VenueService;
import com.meethere.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VenueImageServiceImpl implements VenueImageService {

    @Autowired
    VenueImageDAO venueImageDAO;
    @Autowired
    VenueService venueService;

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public List<VenueImage> listByVenue(int vid){
        Venue venue = venueService.get(vid);
        return venueImageDAO.findByVenue(venue);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void add(VenueImage venueImage) {
        venueImageDAO.save(venueImage);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void delete(int id){
        venueImageDAO.delete(id);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public VenueImage get(int id){
        return venueImageDAO.findOne(id);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void setFirstVenueImages(List<Venue> venues) {
        for (Venue venue : venues)
            setFirstVenueImage(venue);
    }
    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void setFirstVenueImage(Venue venue) {
        List<VenueImage> singleImages = listByVenue(venue.getId());
        venue.setVenueImages(singleImages);
        if(!singleImages.isEmpty())
            venue.setFirstVenueImage(singleImages.get(0));
        else
            venue.setFirstVenueImage(new VenueImage());
    }
}