package com.meethere.service;

import com.meethere.pojo.News;
import com.meethere.pojo.Venue;
import com.meethere.util.Page4Navigator;

public interface NewsService {
    public Page4Navigator<News> list(String keyword, int start, int size, int navigatePages);
    public void saveNews(News news);
    public void delete(int id);
    public News get(int id);
}