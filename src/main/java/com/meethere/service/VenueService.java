package com.meethere.service;

import com.meethere.pojo.District;
import com.meethere.pojo.Venue;
import com.meethere.util.Page4Navigator;

import java.text.ParseException;
import java.util.List;

public interface VenueService {
    public Page4Navigator<Venue> list(int start, int size,int navigatePages) ;
        public Page4Navigator<Venue> listByDistrict(int did,int start, int size, int navigatePages);
    public Page4Navigator<Venue> search(int did,String sort, Venue venue,Integer minPrice,Integer maxPrice,int start, int size, int navigatePages);
    public Page4Navigator<Venue> searchByKeyword(int did,String sort, String keyword,Integer minPrice,Integer maxPrice,int start, int size, int navigatePages);
    public void saveVenue(Venue venue) throws ParseException;
    public void delete(int id);
    public Venue get(int id);
    public void update(Venue venue);
    public Venue findByName(String name);
    public List<Venue> findByNameLike(String name);
    public List<Venue> list();
    public void setSaleAndReviewNumber(Venue venue);
    public void setSaleAndReviewNumber(List<Venue> venues);

    }