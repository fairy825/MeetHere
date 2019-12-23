package com.meethere.controller;

import com.meethere.pojo.News;
import com.meethere.pojo.Venue;
import com.meethere.service.DistrictService;
import com.meethere.service.NewsService;
import com.meethere.service.VenueService;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.Page4Navigator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value="新闻的接口", tags= {"新闻管理的controller"})
@RequestMapping("/news")
public class NewsController extends BasicController{

    @Autowired
    NewsService newsService;
    @ApiOperation(value="查询新闻", notes="管理员或用户根据条件查询新闻的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="keyword", value="搜索关键词", dataType="String"),
            @ApiImplicitParam(name="start", value="页码", required = true, dataType="Integer"),
            @ApiImplicitParam(name="size", value="每页的显示个数",  dataType="Integer")
    })
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
    @ApiOperation(value="新增新闻", notes="管理员新增新闻的接口")
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