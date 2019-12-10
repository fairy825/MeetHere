package com.meethere.service.impl;

import com.meethere.Application;
import com.meethere.dao.UserDAO;
import com.meethere.pojo.User;
import com.meethere.service.UserService;
import com.meethere.util.Page4Navigator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceImplTest {
    @MockBean
    private UserDAO userDAO;
    @Autowired
    private UserService userService;
    private User user;
    private Page pageFromJPA;
    @Before
    public void setUp() throws Exception {
        user = new User.UserBuilder().id(1).name("mh").password("12345").phoneNumber("12345678901")
                .email("6666@meethere.com").nickname("meethere")
                .faceImage("img/faceImage/20190906012034.jpg").build();
        pageFromJPA = mock(Page.class);
    }

    @Test
    public void list() {
        when(userDAO.findAll((Pageable)any())).thenReturn((Page)pageFromJPA);
        //Execute
        Page4Navigator<User> page = userService.list(0,8,5);
        verify(userDAO, times(1)).findAll((Pageable)any());
    }

    @Test
    public void search() {
    }

    @Test
    public void should_get_an_user_when_id_right() {
        when(userDAO.findOne(1)).thenReturn(user);
        //Execute
        User user1 = userService.get(1);
        //verify mapping
        verify(userDAO, times(1)).findOne(1);
        assertEquals(Integer.valueOf(1), user1.getId());
        assertEquals("mh", user1.getName());
        assertEquals("12345", user1.getPassword());
        assertEquals("12345678901", user1.getPhoneNumber());
    }
    @Test
    public void should_get_none_when_id_wrong() {
        when(userDAO.findOne(2)).thenReturn(null);
        //Execute
        User user1 = userService.get(2);
        //verify mapping
        verify(userDAO, times(1)).findOne(2);
        assertNull(user1);
    }
    @Test
    public void should_add_user() {
        when(userDAO.save((User)any())).thenReturn(null);
        //Execute
        userService.saveUser(user);
        //verify mapping
        verify(userDAO, times(1)).save(user);
    }

    @Test
    public void should_update_user() {
        when(userDAO.save((User)any())).thenReturn(null);
        //Execute
        userService.saveUser(user);
        //verify mapping
        verify(userDAO, times(1)).save(user);
    }

    @Test
    public void should_not_allowed_to_login_when_password_right() {
        //Stub to return value
        when(userDAO.findByNameAndPassword("mh","12345")).thenReturn(user);
        //Execute
        User user_for_login = userService.queryUserForLogin("mh","12345");
        //verify mapping
        verify(userDAO, times(1)).findByNameAndPassword("mh","12345");
        assertEquals(Integer.valueOf(1), user_for_login.getId());
        assertEquals("mh", user_for_login.getName());
        assertEquals("12345", user_for_login.getPassword());
        assertEquals("12345678901", user_for_login.getPhoneNumber());
    }
    @Test
    public void should_allowed_to_login_when_password_wrong() {
        //Stub to return value
        when(userDAO.findByNameAndPassword("mh","11111")).thenReturn(null);
        //Execute
        User user_for_login = userService.queryUserForLogin("mh","11111");
        //verify mapping
        verify(userDAO, times(1)).findByNameAndPassword("mh","11111");
        assertNull(user_for_login);
    }
    @Test
    public void should_return_true_when_username_exist() {
        //Stub to return value
        when(userDAO.findByName("mh")).thenReturn(user);
        //Execute
        boolean succeed = userService.queryUsernameIsExist("mh");
        //verify mapping
        verify(userDAO, times(1)).findByName("mh");
        assertTrue(succeed);
    }
    @Test
    public void should_return_true_when_username_not_exist() {
        //Stub to return value
        when(userDAO.findByName("notexist")).thenReturn(null);
        //Execute
        boolean succeed = userService.queryUsernameIsExist("notexist");
        //verify mapping
        verify(userDAO, times(1)).findByName("notexist");
        assertFalse(succeed);
    }
    @Test
    public void should_get_an_user_when_name_right() {
        when(userDAO.findByName("mh")).thenReturn(user);
        //Execute
        User user1 =  userService.findByName("mh");
        ArgumentCaptor<String> stringArgCaptor = ArgumentCaptor.forClass(String.class);
        verify(userDAO, times(1)).findByName(stringArgCaptor.capture());
        assertEquals("mh", stringArgCaptor.getValue());
        //verify mapping
        assertEquals(Integer.valueOf(1), user1.getId());
        assertEquals("mh", user1.getName());
        assertEquals("12345", user1.getPassword());
        assertEquals("12345678901", user1.getPhoneNumber());
    }
    @Test
    public void should_get_none_when_name_wrong() {
        when(userDAO.findByName("notexist")).thenReturn(null);
        //Execute
        User user1 = userService.findByName("notexist");
        //verify mapping
        verify(userDAO, times(1)).findByName("notexist");
        assertNull(user1);
    }
}