package com.meethere.controller;

import com.meethere.pojo.District;
import com.meethere.pojo.Venue;
import com.meethere.service.DistrictService;
import com.meethere.service.MessageService;
import com.meethere.service.VenueImageService;
import com.meethere.service.VenueService;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class VenueController extends BasicController{
	@Autowired
    DistrictService districtService;
    @Autowired
    VenueService venueService;
    @Autowired
    MessageService messageService;
    @Autowired
    VenueImageService venueImageService;
    @GetMapping("/districts/{did}/venues")
    public IMoocJSONResult list(@PathVariable("did") int did,@RequestParam(value = "start", defaultValue = "0") Integer start,
                                     Integer size)
            throws Exception {
    	start = start<0?0:start;
    	if(size == null) size = PAGE_SIZE;
    	Page4Navigator<Venue> page = venueService.listByDistrict(did, start,size,5);
        return IMoocJSONResult.ok(page);
    }
    @GetMapping("/venues")
    public IMoocJSONResult listAll(@RequestParam(value = "start", defaultValue = "0") Integer start,
                                Integer size)
            throws Exception {
        start = start<0?0:start;
        if(size == null) size = PAGE_SIZE;
        Page4Navigator<Venue> page = venueService.list(start,size,5);
        venueImageService.setFirstVenueImages(page.getContent());
        return IMoocJSONResult.ok(page);
    }
//    @PostMapping("/districts/{did}/venues/{sort}")
//    public IMoocJSONResult search(@RequestBody Venue venue,
//                                  @PathVariable("did") int did,
//                                  @PathVariable("sort") String sort,
//                                  @RequestParam(value = "minPrice", defaultValue = "0") Integer minPrice,
//                                  @RequestParam(value = "maxPrice",required = false) Integer maxPrice,
//                                  @RequestParam(value = "start", defaultValue = "0") Integer start,
//                                  Integer size)
//            throws Exception {
//        start = start<0?0:start;
//        if(size == null) size = PAGE_SIZE;
//        Page4Navigator<Venue> page = venueService.search(did,sort,venue,minPrice,maxPrice,start,size,5);
//        venueImageService.setFirstVenueImages(page.getContent());
//
//        return IMoocJSONResult.ok(page);
//    }
    @GetMapping("/districts/{did}/venues/{sort}/{keyword}")
    public IMoocJSONResult search2(
                                  @PathVariable("did") int did,
                                  @PathVariable("sort") String sort,
                                  @PathVariable("keyword") String keyword,
                                  @RequestParam(value = "minPrice", defaultValue = "0") Integer minPrice,
                                  @RequestParam(value = "maxPrice",required = false) Integer maxPrice,
                                  @RequestParam(value = "start", defaultValue = "0") Integer start,
                                  Integer size)
            throws Exception {
        start = start<0?0:start;
        if(size == null) size = PAGE_SIZE;
        Page4Navigator<Venue> page = venueService.searchByKeyword(did,sort,keyword,minPrice,maxPrice,start,size,5);
        venueImageService.setFirstVenueImages(page.getContent());

        return IMoocJSONResult.ok(page);
    }
    @GetMapping("/districts/{did}/venues/{keyword}")
    public IMoocJSONResult search3(
            @PathVariable("did") int did,
            @PathVariable("keyword") String keyword,
            @RequestParam(value = "minPrice", defaultValue = "0") Integer minPrice,
            @RequestParam(value = "maxPrice",required = false) Integer maxPrice,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
            Integer size)
            throws Exception {
        start = start<0?0:start;
        if(size == null) size = PAGE_SIZE;
        Page4Navigator<Venue> page = venueService.searchByKeyword(did,null,keyword,minPrice,maxPrice,start,size,5);
        venueImageService.setFirstVenueImages(page.getContent());

        return IMoocJSONResult.ok(page);
    }
    @PostMapping("/venues")
    public IMoocJSONResult add(@RequestBody Venue venue) throws Exception {
//        venue.setDistrict(districtService.get(did));
        venueService.saveVenue(venue);
        return IMoocJSONResult.ok(venue);
    }
    @DeleteMapping("/venues/{id}")
    public IMoocJSONResult delete(@PathVariable("id") int id)  throws Exception {
        venueService.delete(id);
        return IMoocJSONResult.ok();
    }

    @GetMapping("/venues/{id}")
    public IMoocJSONResult get(@PathVariable("id") int id) throws Exception {
        Venue venue=venueService.get(id);
        venueService.setSaleAndReviewNumber(venue);
        venueImageService.setFirstVenueImage(venue);
        return IMoocJSONResult.ok(venue);
    }
    @GetMapping("/showVenue/{id}")
    public IMoocJSONResult getDetail(@PathVariable("id") int id) throws Exception {
        Venue venue=venueService.get(id);
        venueService.setSaleAndReviewNumber(venue);
        venueImageService.setFirstVenueImage(venue);
        return IMoocJSONResult.ok(venue);
    }
    @PutMapping("/venues")
    public IMoocJSONResult update(@RequestBody Venue venue, HttpServletRequest request) throws Exception {
        venueService.update(venue);
        return IMoocJSONResult.ok(venue);
    }

}