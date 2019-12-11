package com.meethere.service.impl;

import com.meethere.comparator.*;
import com.meethere.dao.DistrictDAO;
import com.meethere.dao.VenueDAO;
import com.meethere.pojo.Booking;
import com.meethere.pojo.District;
import com.meethere.pojo.Venue;
import com.meethere.service.*;
import com.meethere.util.Page4Navigator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class VenueServiceImpl implements VenueService {

    @Autowired
    VenueDAO venueDAO;
    @Autowired
    DistrictService districtService;
    @Autowired
    MessageService messageService;
    @Autowired
    BookingService bookingService;
    @Autowired
    TimeSlotService timeSlotService;
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<Venue> listByDistrict(int did, int start, int size,int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Venue> pageFromJPA =null;
        if(did==0)
            pageFromJPA =venueDAO.findAll(pageable);
        else {
            District district = districtService.get(did);
            pageFromJPA = venueDAO.findByDistrict(district, pageable);
        }
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<Venue> list(int start, int size,int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Venue> pageFromJPA =venueDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public List<Venue> list() {
        return venueDAO.findAll();
    }
    public void setSaleAndReviewNumber(Venue venue){
        int saleCount = bookingService.total(venue);
        venue.setSaleCount(saleCount);
        int reviewCount = messageService.total(venue);
        venue.setReviewCount(reviewCount);
    }
    public void setSaleAndReviewNumber(List<Venue> venues){
        for (Venue venue : venues) {
            setSaleAndReviewNumber(venue);
        }
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<Venue> search(int did,String sort, Venue venue,Integer minPrice,Integer maxPrice,int start, int size, int navigatePages){
        District district = null;
        if(did!=0) district = districtService.get(did);
        Page<Venue> pageFromJPA;
        Sort sort1 = new Sort(Sort.Direction.DESC, "id");

        Pageable pageable = new PageRequest(start, size, sort1);
        Venue v = new Venue();
        v.setId(null);
        v.setPrice(null);
        v.setTotalSeat(null);
        v.setStartTime(null);
        v.setEndTime(null);
        v.setReviewCount(null);
        v.setSaleCount(null);
        if(StringUtils.isBlank(venue.getName())) venue.setName(null);
        v.setName(venue.getName());

        v.setDistrict(district);
        Example<Venue> example = Example.of(v);
        List<Venue> venueList= venueDAO.findAll(example,sort1);

        List<Venue> res = new ArrayList<>();
        int count = 0;
        for(Venue venue1: venueList){
            Float price = venue1.getPrice();
            if((maxPrice==null&&price>=minPrice)||(maxPrice!=null&&price>=minPrice&&price<=maxPrice)){
                count++;
            }
        }
        int i = 0;
        for(Venue venue1: venueList){
            float price = venue1.getPrice();
            if((maxPrice==null&&price>=minPrice)||(maxPrice!=null&&price>=minPrice&&price<=maxPrice)){
                if(i>=start*size&&i<(start+1)*size)//start=0 size=2
                    res.add(venue1);
                else if(i>=(start+1)*size)
                    break;
                i++;
            }
        }
        setSaleAndReviewNumber(res);
        if(null!=sort){
            switch(sort){
                case "review":
                    Collections.sort(res,new VenueReviewComparator());
                    break;
                case "saleCount" :
                    Collections.sort(res,new VenueSaleComparator());
                    break;
                case "price":
                    Collections.sort(res,new VenuePriceComparator());
                    break;
                case "all":
                    Collections.sort(res,new VenueAllComparator());
                    break;
            }
        }


        pageFromJPA = new PageImpl<Venue>(res,pageable,count);

        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<Venue> searchByKeyword(int did,String sort, String keyword,Integer minPrice,Integer maxPrice,int start, int size, int navigatePages){
        Page<Venue> pageFromJPA;
        Sort sort1 = new Sort(Sort.Direction.DESC, "id");

        Pageable pageable = new PageRequest(start, size, sort1);
        List<Venue> venueList = null;
        if(keyword!=null&& !keyword.equalsIgnoreCase("undefined")&&!keyword.equalsIgnoreCase("null")&&!StringUtils.isBlank(keyword)) {
            venueList = venueDAO.findByNameLike("%"+keyword+"%");
        }else
            venueList =venueDAO.findAll();
        setSaleAndReviewNumber(venueList);
        if(null!=sort){
            switch(sort){
                case "review":
                    Collections.sort(venueList,new VenueReviewComparator());
                    break;
                case "saleCount" :
                    Collections.sort(venueList,new VenueSaleComparator());
                    break;
                case "price":
                    Collections.sort(venueList,new VenuePriceComparator());
                    break;
                case "all":
                    Collections.sort(venueList,new VenueAllComparator());
                    break;
            }
        }
        List<Venue> res = new ArrayList<>();
        int count = 0;
        for(Venue venue1: venueList){
            float price = venue1.getPrice();
            int diid = venue1.getDistrict().getId();
            if(((diid==did)||did==0)&&((maxPrice==null&&price>=minPrice)||(maxPrice!=null&&price>=minPrice&&price<=maxPrice))){
                count++;
            }
        }
        int i = 0;
        for(Venue venue1: venueList){
            float price = venue1.getPrice();
            int diid = venue1.getDistrict().getId();
            if(((diid==did)||did==0)&&((maxPrice==null&&price>=minPrice)||(maxPrice!=null&&price>=minPrice&&price<=maxPrice))){
                if(i>=start*size&&i<(start+1)*size)//start=0 size=2
                    res.add(venue1);
                else if(i>=(start+1)*size)
                    break;
                i++;
            }
        }

        pageFromJPA = new PageImpl<Venue>(res,pageable,count);

        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public List<Venue> findByNameLike(String name){
        return venueDAO.findByNameLike(name);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void saveVenue(Venue venue) throws ParseException {
        venueDAO.save(venue);

        timeSlotService.createByVenue(venue,0);
        timeSlotService.createByVenue(venue,1);
        timeSlotService.createByVenue(venue,2);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void delete(int id){
        venueDAO.delete(id);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Venue get(int id){
        Venue venue =  venueDAO.findOne(id);
        return venue;
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void update(Venue venue){
        venueDAO.save(venue);
    }
    public Venue findByName(String name){
        return venueDAO.findByName(name);
    }
}
