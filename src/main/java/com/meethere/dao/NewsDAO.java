package com.meethere.dao;

import com.meethere.pojo.District;
import com.meethere.pojo.News;
import com.meethere.pojo.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsDAO extends JpaRepository<News,Integer>{
    Page<News> findByTitleLike(String title, Pageable pageable);
}
