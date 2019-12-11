package com.meethere.dao;

import com.meethere.pojo.Admin;
import com.meethere.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminDAO extends JpaRepository<Admin,Integer>{
    Admin findByName(String name);
    Admin findByNameAndPassword(String name, String password);
}
