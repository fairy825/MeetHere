package com.meethere.service.impl;

import com.meethere.dao.UserDAO;
import com.meethere.pojo.User;
import com.meethere.service.UserService;
import com.meethere.util.Page4Navigator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired UserDAO userDAO;

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<User> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        Page pageFromJPA =userDAO.findAll(pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public Page4Navigator<User> search(User user, int start, int size, int navigatePages) throws Exception {
        if (user==null)
            throw new Exception("user should be null");
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        user.setId(null);
        Page pageFromJPA =null;
        if(StringUtils.isBlank(user.getName())&&StringUtils.isBlank(user.getNickname())) {
            pageFromJPA =userDAO.findAll(pageable);
        }
        else if(StringUtils.isBlank(user.getName())||StringUtils.isBlank(user.getNickname())) {
            if(StringUtils.isBlank(user.getNickname()))
                pageFromJPA =userDAO.findByNameLike("%"+user.getName()+"%",pageable);
            if(StringUtils.isBlank(user.getName()))
                pageFromJPA =userDAO.findByNicknameLike("%"+user.getNickname()+"%",pageable);
        }
        else
        pageFromJPA =userDAO.findByNameLikeAndNicknameLike("%"+user.getName()+"%","%"+user.getNickname()+"%",pageable);
        return new Page4Navigator<>(pageFromJPA,navigatePages);
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public User get(int id){
        return userDAO.findOne(id);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void saveUser(User user) {
        userDAO.save(user);
    }
    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void update(User user){
        userDAO.save(user);
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public User queryUserForLogin(String username, String password){
        return userDAO.findByNameAndPassword(username, password);
    }

    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username){
        User user = userDAO.findByName(username);
        return user!=null;
    }
    @Transactional(propagation= Propagation.SUPPORTS)
    @Override
    public User findByName(String name){
        return userDAO.findByName(name);
    }
//    @Transactional(propagation= Propagation.SUPPORTS)
//    @Override
//    public List<User> findByNameLike(String name){
//        return userDAO.findByNameLike(name);
//    }
}