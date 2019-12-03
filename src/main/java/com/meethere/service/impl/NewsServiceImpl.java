package com.meethere.service.impl;

import com.meethere.dao.NewsDAO;
import com.meethere.dao.VenueDAO;
import com.meethere.pojo.District;
import com.meethere.pojo.News;
import com.meethere.pojo.Venue;
import com.meethere.service.DistrictService;
import com.meethere.service.NewsService;
import com.meethere.service.VenueService;
import com.meethere.util.Page4Navigator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    NewsDAO newsDAO;

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<News> list(String keyword, int start, int size, int navigatePages) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA =null;
        if(keyword!=null&& !keyword.equalsIgnoreCase("undefined")&&!StringUtils.isBlank(keyword)) {
            pageFromJPA = newsDAO.findByTitleLike("%"+keyword+"%",pageable);
        }else
            pageFromJPA =newsDAO.findAll(pageable);

        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void saveNews(News news) {
        news.setCreateDate(new Date());
        newsDAO.save(news);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void delete(int id){
        newsDAO.delete(id);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public News get(int id){
        return newsDAO.findOne(id);
    }

}