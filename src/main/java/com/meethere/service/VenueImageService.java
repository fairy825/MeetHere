package com.meethere.service;

import com.meethere.pojo.Venue;
import com.meethere.pojo.VenueImage;
import com.meethere.util.Page4Navigator;

import java.util.List;

public interface VenueImageService {
    public  List<VenueImage> listByVenue(int vid);
    public void add(VenueImage venueImage);
    public void delete(int id);
    public VenueImage get(int id);
    public void setFirstVenueImages(List<Venue> venues);
    public void setFirstVenueImage(Venue venue);
}