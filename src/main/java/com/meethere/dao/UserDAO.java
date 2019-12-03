package com.meethere.dao;

import com.meethere.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<User,Integer>{
    User findByName(String name);
    Page<User> findByNameLikeAndNicknameLike(String name, String nickName,Pageable pageable);
    List<User> findByNameLike(String name);
    Page<User> findByNameLike(String name,Pageable pageable);
    Page<User> findByNicknameLike(String nickName,Pageable pageable);
    User findByNameAndPassword(String name, String password);
}
