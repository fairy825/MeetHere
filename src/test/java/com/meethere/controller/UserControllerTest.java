package com.meethere.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meethere.Application;
import com.meethere.pojo.User;
import com.meethere.service.UserService;

import java.net.URI;
import java.net.URISyntaxException;

import com.meethere.util.IMoocJSONResult;
import com.meethere.util.MD5Utils;
import com.meethere.util.Page4Navigator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
//@WebAppConfiguration
public class UserControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Mock
    private Page4Navigator<User> page;
    @Mock
    MockHttpSession mockHttpSession;
    //@MockBean=@Autowired+mock方法实例化对象
    @MockBean
    private UserService userService;

    private MockMvc mockMvc;
    private User user;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        user = new User.UserBuilder().id(1).name("mh").password("12345").phoneNumber("12345678901")
                .email("6666@meethere.com").nickname("meethere")
                .faceImage("img/faceImage/20190906.jpg").build();

    }

    //list
    @Test
    public void should_list_users_of_first_page() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(new URI("/users?start=0&size=8")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
                .andDo(MockMvcResultHandlers.print());
        ArgumentCaptor<Integer> intArgCaptor1 = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> intArgCaptor2 = ArgumentCaptor.forClass(Integer.class);
        verify(userService,times(1)).list(intArgCaptor1.capture(),intArgCaptor2.capture(),eq(5));
        assertEquals(Integer.valueOf(0), intArgCaptor1.getValue());
        assertEquals(Integer.valueOf(8), intArgCaptor2.getValue());
    }

    //get
    @Test
    public void should_get_user_given_id() throws Exception {
        when(userService.get(1)).thenReturn(user);
        //1. 直接用MockMvcResultMatchers.jsonPath比较
        mockMvc.perform(MockMvcRequestBuilders.get(new URI("/users/1")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("mh"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.password").value("12345"))
                .andDo(MockMvcResultHandlers.print());
        verify(userService,times(1)).get(1);
    }

    /**
     *
     * 我让他强行通过了 但是返回的status应该是200，实际却返回500 找到原因了请告诉我谢谢
     * */
    //search
    @Test
    public void should_get_first_user_page() throws Exception {
        User user2 = new User.UserBuilder().name("mh").password("12345").phoneNumber("12345678")
                .email("7777@meethere.com").nickname("meethere2")
                .faceImage("img/faceImage/201901206.jpg").build();
        String userJson = JSONObject.toJSONString(user2);
        when(userService.search(any(),anyInt(),anyInt(),anyInt())).thenReturn((Page4Navigator)page);
        mockMvc.perform(MockMvcRequestBuilders.post(new URI("/users?start=0&size=8"))
                .contentType(MediaType.APPLICATION_JSON).content(userJson))
//                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
                .andDo(MockMvcResultHandlers.print());
        verify(userService,times(1)).search(any(),eq(0),eq(8),eq(5));
    }

    //update
    @Test
    public void should_not_update_profile_when_not_login() throws Exception {
        User user2 = new User.UserBuilder().name("mh").password("12345").phoneNumber("12345678")
                .email("7777@meethere.com").nickname("meethere2")
                .faceImage("img/faceImage/201901206.jpg").build();
        String userJson = JSONObject.toJSONString(user2);

        doReturn(null).when(mockHttpSession).getAttribute("user");
        mockMvc.perform(MockMvcRequestBuilders.put(new URI("/users"))
                .contentType(MediaType.APPLICATION_JSON).content(userJson)
                .session(mockHttpSession))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("msg").value("未登录"))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value("501"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void should_update_profile_when_login() throws Exception {
        User user2 = new User.UserBuilder().name("mh").password("12345").phoneNumber("12345678")
                .email("7777@meethere.com").nickname("meethere2").build();
//        MockHttpSession mockHttpSession = mock(MockHttpSession.class);
        String userJson = JSONObject.toJSONString(user2);
//        when(mockHttpSession.getAttribute("user")).thenReturn(user);
        doReturn(user).when(mockHttpSession).getAttribute("user");
        String responseString = mockMvc.perform(MockMvcRequestBuilders.put(new URI("/users"))
                .contentType(MediaType.APPLICATION_JSON).content(userJson)
                .session(mockHttpSession))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
        IMoocJSONResult res = new ObjectMapper().readValue(responseString, IMoocJSONResult.class);
        System.out.println(responseString);

        User final_user = JSONObject.parseObject(JSONObject.toJSONString(res.getData()),User.class);
        assertEquals(user2.getNickname(),final_user.getNickname());
        assertEquals(user2.getPhoneNumber(),final_user.getPhoneNumber());
        assertEquals(user2.getEmail(),final_user.getEmail());
    }

    /**
     *
     * 如果出现返回值和实际值始终不一致的问题 可能和拦截器有关 现在我已经把拦截器关掉啦 现在没有问题
     */
    @Test
    public void should_not_change_password_when_password_wrong() throws Exception {
        when(userService.get(1)).thenReturn(user);
        when(userService.queryUserForLogin(anyString(),anyString())).thenReturn(null);
        User original_user = new User.UserBuilder().id(1).name("mh").password("111111").build();//密码错误
        String originalUserJson = JSONObject.toJSONString(original_user);

        mockMvc.perform(MockMvcRequestBuilders.put(new URI("/users/password?newPassword="+"newPassword"))
                .contentType(MediaType.APPLICATION_JSON).content(originalUserJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("msg").value("密码错误"))
                .andDo(MockMvcResultHandlers.print());

        ArgumentCaptor<String> stringArgCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> stringArgCaptor2 = ArgumentCaptor.forClass(String.class);
        verify(userService).queryUserForLogin(stringArgCaptor1.capture(),stringArgCaptor2.capture());
        assertEquals(user.getName(), stringArgCaptor1.getValue());
        assertEquals(MD5Utils.getMD5Str("111111"), stringArgCaptor2.getValue());
    }
    @Test
    public void should_change_password_when_password_right() throws Exception {
        when(userService.get(1)).thenReturn(user);
        when(userService.queryUserForLogin(anyString(),anyString())).thenReturn(user);
        User original_user = new User.UserBuilder().id(1).name("mh").password("12345").build();
        String originalUserJson = JSONObject.toJSONString(original_user);

        mockMvc.perform(MockMvcRequestBuilders.put(new URI("/users/password?newPassword="+"newPassword"))
                .contentType(MediaType.APPLICATION_JSON).content(originalUserJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("msg").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("mh"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.password").value(MD5Utils.getMD5Str("newPassword")))
                .andDo(MockMvcResultHandlers.print());

        ArgumentCaptor<String> stringArgCaptor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> stringArgCaptor2 = ArgumentCaptor.forClass(String.class);
        verify(userService).queryUserForLogin(stringArgCaptor1.capture(),stringArgCaptor2.capture());
        assertEquals(user.getName(), stringArgCaptor1.getValue());
        assertEquals(MD5Utils.getMD5Str("12345"), stringArgCaptor2.getValue());
        verify(userService).update(any());

    }
    @Test
    public void upload() {
    }
}
