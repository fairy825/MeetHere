package com.meethere.service.impl;

import com.meethere.dao.AdminDAO;
import com.meethere.dao.UserDAO;
import com.meethere.pojo.Admin;
import com.meethere.pojo.User;
import com.meethere.service.AdminService;
import com.meethere.service.UserService;
import com.meethere.util.Page4Navigator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminDAO adminDAO;

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Admin queryUserForLogin(String username, String password){
        return adminDAO.findByNameAndPassword(username, password);
    }

}