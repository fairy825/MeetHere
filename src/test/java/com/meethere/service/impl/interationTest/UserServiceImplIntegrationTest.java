package com.meethere.service.impl.interationTest;

import com.meethere.Application;
import com.meethere.dao.UserDAO;
import com.meethere.pojo.User;
import com.meethere.service.UserService;
import com.meethere.util.Page4Navigator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceImplIntegrationTest {
    @Autowired
    private UserService userService;
    private User user;
    private Page pageFromJPA;

    @Before
    public void setUp() throws Exception {
//        user = new User.UserBuilder().id(1).name("mh").password("12345").phoneNumber("12345678901")
//                .email("6666@meethere.com").nickname("meethere")
//                .faceImage("img/faceImage/20190906012034.jpg").build();
//        pageFromJPA = mock(Page.class);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void database_integrity_test() throws Exception{
        User user1 = new User.UserBuilder().name("mh").password("12345").phoneNumber("12345678901")
                .email("6666@meethere.com").nickname("meethere")
                .faceImage("img/faceImage/20190906.jpg").build();
        User user2 = new User.UserBuilder().name("mh2").password("8888").phoneNumber("12345678")
                .email("7777@meethere.com").nickname("meethere2")
                .faceImage("img/faceImage/201901206.jpg").build();
        User user3 = new User.UserBuilder().name("a").password("8888")
                .email("8888@meethere.com").nickname("a").build();
        //saveUser
        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);

        //get
        User cur_user = userService.get(user1.getId());
//        assertEquals(Integer.valueOf(1),cur_user.getId());
        assertEquals("mh",cur_user.getName());
        assertEquals("meethere",cur_user.getNickname());
        assertEquals("12345678901",cur_user.getPhoneNumber());
        assertEquals("6666@meethere.com",cur_user.getEmail());
        assertEquals("12345",cur_user.getPassword());

        //queryUserForLogin
        cur_user = userService.queryUserForLogin("mh2","8888");
        assertEquals("mh2",cur_user.getName());
        assertEquals("meethere2",cur_user.getNickname());
        assertEquals("12345678",cur_user.getPhoneNumber());
        assertEquals("7777@meethere.com",cur_user.getEmail());
        assertEquals("8888",cur_user.getPassword());

        //queryUsernameIsExist
        assertTrue(userService.queryUsernameIsExist("mh"));
        assertFalse(userService.queryUsernameIsExist("mh3"));

        //findByName
        cur_user = userService.findByName("mh2");
        assertEquals("mh2",cur_user.getName());
        assertEquals("meethere2",cur_user.getNickname());
        assertEquals("12345678",cur_user.getPhoneNumber());
        assertEquals("7777@meethere.com",cur_user.getEmail());
        assertEquals("8888",cur_user.getPassword());

        //search
        User user_for_search = new User.UserBuilder().name(null).nickname("meethere").build();
        Page4Navigator<User> entries = userService.search(user_for_search,0,8,5);
        assertTrue(entries.isFirst());
        assertEquals(8,entries.getSize());
        assertEquals(2,entries.getTotalElements());
        assertEquals(1,entries.getTotalPages());
        assertEquals("mh2",entries.getContent().get(0).getName());
        assertEquals("meethere2",entries.getContent().get(0).getNickname());
        assertEquals("12345678",entries.getContent().get(0).getPhoneNumber());
        assertEquals("mh",entries.getContent().get(1).getName());

        user_for_search = new User.UserBuilder().name("MH3").build();
        entries = userService.search(user_for_search,0,8,5);
        assertTrue(entries.isFirst());
        assertEquals(0,entries.getTotalElements());
        assertEquals(0,entries.getTotalPages());

        //update
        user1.setName("mh1");
        userService.update(user1);

        //list
        entries = userService.list(1,2,5);
//        assertFalse(entries.isFirst());
        assertEquals(2,entries.getSize());
        assertEquals(3,entries.getTotalElements());
        assertEquals(2,entries.getTotalPages());
        assertEquals(1,entries.getContent().size());
        assertEquals("mh1",entries.getContent().get(0).getName());
    }

}
