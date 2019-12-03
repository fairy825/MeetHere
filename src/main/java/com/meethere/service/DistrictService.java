package com.meethere.service;

import com.meethere.pojo.District;
import com.meethere.pojo.User;
import com.meethere.util.Page4Navigator;

import java.util.List;

public interface DistrictService {
    public Page4Navigator<District> list(String keyword, int start, int size, int navigatePages);
    public void saveDistrict(District district);
    public void delete(int id);
    public District get(int id);
    public void update(District district);
    public List<District> findAll();
}