package com.meethere.service;

import com.meethere.pojo.Admin;
import com.meethere.pojo.User;
import com.meethere.util.Page4Navigator;

public interface AdminService {
    public Admin queryUserForLogin(String username, String password);
}