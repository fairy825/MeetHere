package com.meethere.service;

import com.meethere.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.meethere.dao.UserDAO;
import com.meethere.pojo.User;
import com.meethere.util.Page4Navigator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    public Page4Navigator<User> list(int start, int size, int navigatePages);
    public Page4Navigator<User> search(User user, int start, int size, int navigatePages);
    public void saveUser(User user);
    public User get(int id);
    public void update(User user);
    public User queryUserForLogin(String username, String password);
    public boolean queryUsernameIsExist(String username);
    public User findByName(String name);
//    public List<User> findByNameLike(String name);
}