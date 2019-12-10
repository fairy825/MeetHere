package com.meethere.service.impl;

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
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        user = new User.UserBuilder().id(1).name("mh").password("12345").phoneNumber("12345678901")
                .email("6666@meethere.com").nickname("meethere")
                .faceImage("img/faceImage/20190906012034.jpg").build();
        pageFromJPA = mock(Page.class);
    }

    //list
    @Test
    public void should_get_first_user_page() {
        when(userDAO.findAll((Pageable)any())).thenReturn((Page)pageFromJPA);
        //Execute
        Page4Navigator<User> page = userService.list(0,8,5);
        verify(userDAO, times(1)).findAll((Pageable)any());
    }

    //search
    @Test
    public void should_get_user_when_name_not_empty_and_nickname_not_empty() throws Exception {
        when(userDAO.findByNameLikeAndNicknameLike(anyString(), anyString(), (Pageable)any()))
                .thenReturn((Page)pageFromJPA);
        Page4Navigator<User> page = userService.search(user,0,8,5);

        ArgumentCaptor<String> nameArgCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> nicknameArgCaptor = ArgumentCaptor.forClass(String.class);
        verify(userDAO, times(1)).findByNameLikeAndNicknameLike(
                nameArgCaptor.capture(), nicknameArgCaptor.capture(), (Pageable)any());

        assertEquals("%mh%", nameArgCaptor.getValue());
        assertEquals("%meethere%", nicknameArgCaptor.getValue());
    }

    @Test
    public void should_get_user_when_name_empty_and_nickname_not_empty() throws Exception {
        User user2 = new User.UserBuilder().name(null).nickname("meethere").build();
        when(userDAO.findByNicknameLike(anyString(), (Pageable)any()))
                .thenReturn((Page)pageFromJPA);
        Page4Navigator<User> page = userService.search(user2,0,8,5);

        ArgumentCaptor<String> nicknameArgCaptor = ArgumentCaptor.forClass(String.class);
        verify(userDAO, times(1)).findByNicknameLike(
                nicknameArgCaptor.capture(), (Pageable)any());

        assertEquals("%meethere%", nicknameArgCaptor.getValue());
    }
    @Test
    public void should_get_user_when_name_not_empty_and_nickname_empty() throws Exception {
        User user3 = new User.UserBuilder().name("mh").nickname("").build();
        when(userDAO.findByNameLike(anyString(), (Pageable)any()))
                .thenReturn((Page)pageFromJPA);
        Page4Navigator<User> page = userService.search(user3,0,8,5);

        ArgumentCaptor<String> nameArgCaptor = ArgumentCaptor.forClass(String.class);
        verify(userDAO, times(1)).findByNameLike(
                nameArgCaptor.capture(), (Pageable)any());

        assertEquals("%mh%", nameArgCaptor.getValue());
    }
    @Test
    public void should_get_user_when_name_empty_and_nickname_empty() throws Exception {
        User user4 = new User.UserBuilder().name("").nickname("").build();
        when(userDAO.findAll((Pageable)any())).thenReturn((Page)pageFromJPA);
        Page4Navigator<User> page = userService.search(user4,0,8,5);

        verify(userDAO, times(1)).findAll((Pageable)any());
    }

    @Test
    public void should_throw_exception_when_user_null() throws Exception {
        User user4 = new User.UserBuilder().name("").nickname("").build();
        when(userDAO.findAll((Pageable)any())).thenReturn((Page)pageFromJPA);

        exception.expect(Exception.class);
        exception.expectMessage("user should be null");
        Page4Navigator<User> page = userService.search(null,0,8,5);
    }

    //get
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

    //saveUser
    @Test
    public void should_add_user() {
        when(userDAO.save((User)any())).thenReturn(null);
        //Execute
        userService.saveUser(user);
        //verify mapping
        verify(userDAO, times(1)).save(user);
    }

    //update
    @Test
    public void should_update_user() {
        when(userDAO.save((User)any())).thenReturn(null);
        //Execute
        userService.saveUser(user);
        //verify mapping
        verify(userDAO, times(1)).save(user);
    }

    //queryUserForLogin
    @Test
    public void should_allowed_to_login_when_password_right() {
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
    public void should_not_allowed_to_login_when_password_wrong() {
        //Stub to return value
        when(userDAO.findByNameAndPassword("mh","11111")).thenReturn(null);
        //Execute
        User user_for_login = userService.queryUserForLogin("mh","11111");
        //verify mapping
        verify(userDAO, times(1)).findByNameAndPassword("mh","11111");
        assertNull(user_for_login);
    }

    //queryUsernameIsExist
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
    public void should_return_false_when_username_not_exist() {
        //Stub to return value
        when(userDAO.findByName("notexist")).thenReturn(null);
        //Execute
        boolean succeed = userService.queryUsernameIsExist("notexist");
        //verify mapping
        verify(userDAO, times(1)).findByName("notexist");
        assertFalse(succeed);
    }

    //findByName
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
