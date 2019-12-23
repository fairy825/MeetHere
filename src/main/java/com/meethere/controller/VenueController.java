package com.meethere.controller;

import com.meethere.pojo.District;
import com.meethere.pojo.Venue;
import com.meethere.service.DistrictService;
import com.meethere.service.MessageService;
import com.meethere.service.VenueImageService;
import com.meethere.service.VenueService;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.Page4Navigator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value="场馆的接口", tags= {"场馆管理的controller"})
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
    //用户的搜索
    @ApiOperation(value="用户查询场馆", notes="用户根据条件查询场馆的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="did", value="场馆所属的区域id", dataType="Integer"),
            @ApiImplicitParam(name="sort", value="排序方式", dataType="String"),
            @ApiImplicitParam(name="keyword", value="搜索关键词", dataType="String"),
            @ApiImplicitParam(name="minPrice", value="要搜索的最低价格", dataType="Integer"),
            @ApiImplicitParam(name="maxPrice", value="要搜索的最高价格", dataType="Integer"),
            @ApiImplicitParam(name="start", value="页码", required = true, dataType="Integer"),
            @ApiImplicitParam(name="size", value="每页的显示个数",  dataType="Integer")
    })
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
        if(size == null) size = 3;
        Page4Navigator<Venue> page = venueService.searchByKeyword(did,sort,keyword,minPrice,maxPrice,start,size,5);
        venueImageService.setFirstVenueImages(page.getContent());

        return IMoocJSONResult.ok(page);
    }

    //listVenue.html中管理员的搜索
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
    @GetMapping("/venues/{id}/show")
    public IMoocJSONResult getDetail(@PathVariable("id") int id) throws Exception {
        Venue venue=venueService.get(id);
        venueService.setSaleAndReviewNumber(venue);
        venueImageService.setFirstVenueImage(venue);
        return IMoocJSONResult.ok(venue);
    }
    @PutMapping("/venues")
    public IMoocJSONResult update(@RequestBody Venue venue) throws Exception {
        venueService.update(venue);
        return IMoocJSONResult.ok(venue);
    }

}