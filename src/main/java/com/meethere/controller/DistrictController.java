package com.meethere.controller;

import com.meethere.pojo.District;
import com.meethere.pojo.User;
import com.meethere.service.DistrictService;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/districts")
public class DistrictController extends BasicController{
	@Autowired
    DistrictService districtService;

    @GetMapping("")
    public IMoocJSONResult list(String keyword,
                                @RequestParam(value = "start", defaultValue = "0") Integer start,
                                     Integer size)
            throws Exception {
    	start = start<0?0:start;
    	if(size == null) size = PAGE_SIZE;
    	Page4Navigator<District> page = districtService.list(keyword,start,size,5);
        return IMoocJSONResult.ok(page);
    }
    @GetMapping("/all")
    public IMoocJSONResult listAll()
            throws Exception {
        return IMoocJSONResult.ok(districtService.findAll());
    }

    @PostMapping("")
    public IMoocJSONResult add(@RequestBody District district) throws Exception {
        districtService.saveDistrict(district);
        return IMoocJSONResult.ok(district);
    }


    @DeleteMapping("/{id}")
    public IMoocJSONResult delete(@PathVariable("id") int id)  throws Exception {
        districtService.delete(id);
        return IMoocJSONResult.ok();
    }

    @GetMapping("/{id}")
    public IMoocJSONResult get(@PathVariable("id") int id) throws Exception {
        District district=districtService.get(id);
        return IMoocJSONResult.ok(district);
    }

    @PutMapping("")
    public IMoocJSONResult update(@RequestBody District district, HttpServletRequest request) throws Exception {
        districtService.update(district);
        return IMoocJSONResult.ok(district);
    }

}