package com.meethere.controller;

import com.meethere.pojo.News;
import com.meethere.pojo.Venue;
import com.meethere.service.DistrictService;
import com.meethere.service.NewsService;
import com.meethere.service.VenueService;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.Page4Navigator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/news")
public class NewsController extends BasicController{

    @Autowired
    NewsService newsService;

    @GetMapping("")
    public IMoocJSONResult list(String keyword,
            @RequestParam(value = "start", defaultValue = "0") Integer start,
                                Integer size)
            throws Exception {
    	start = start<0?0:start;
    	if(size == null) size = PAGE_SIZE;
    	Page4Navigator<News> page = newsService.list(keyword,start,size,5);
        return IMoocJSONResult.ok(page);
    }

    @PostMapping("")
    public IMoocJSONResult add(@RequestBody News news) throws Exception {
        newsService.saveNews(news);
        return IMoocJSONResult.ok(news);
    }
    @DeleteMapping("/{id}")
    public IMoocJSONResult delete(@PathVariable("id") int id)  throws Exception {
        newsService.delete(id);
        return IMoocJSONResult.ok();
    }

    @GetMapping("/{id}")
    public IMoocJSONResult get(@PathVariable("id") int id) throws Exception {
        News news=newsService.get(id);
        return IMoocJSONResult.ok(news);
    }

}