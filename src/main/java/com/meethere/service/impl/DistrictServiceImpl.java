package com.meethere.service.impl;

import com.meethere.dao.DistrictDAO;
import com.meethere.dao.UserDAO;
import com.meethere.pojo.District;
import com.meethere.pojo.User;
import com.meethere.service.DistrictService;
import com.meethere.service.UserService;
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

import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {

    @Autowired
    DistrictDAO districtDAO;

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<District> list(String keyword, int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA =null;

        if(keyword!=null&&!keyword.equalsIgnoreCase("undefined")&& !StringUtils.isBlank(keyword)) {
            pageFromJPA = districtDAO.findByNameLike("%"+keyword+"%",pageable);
        }else
            pageFromJPA =districtDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public List<District> findAll() {
        return districtDAO.findAll();
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void saveDistrict(District district) {
        districtDAO.save(district);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void delete(int id){
        districtDAO.delete(id);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public District get(int id){
        return districtDAO.findOne(id);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void update(District district){
        districtDAO.save(district);
    }
}