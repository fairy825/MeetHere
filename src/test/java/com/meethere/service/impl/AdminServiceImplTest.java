package com.meethere.service.impl;

import com.meethere.Application;
import com.meethere.dao.AdminDAO;
import com.meethere.pojo.Admin;
import com.meethere.service.AdminService;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

//@Epic("用户单元测试")
//@Feature("AdminFeature")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AdminServiceImplTest {
    @MockBean
    private AdminDAO adminDAO;
    @Autowired
    private AdminService adminService;
    private Admin admin;
    @Before//对应jnuit5的beforeEach 4的@beforeClass对应5的beforeAll
    public void setUp() throws Exception {
        admin = new Admin.AdminBuilder().id(1).name("a").password("123456").build();
    }

    //根据name和password查到admin存在
    @Test
    public void should_get_an_admin() {
       //Stub to return value
        when(adminDAO.findByNameAndPassword(anyString(),anyString())).thenReturn(admin);

        //Execute
        Admin admin_for_login = adminService.queryUserForLogin("a","123456");

        //verify mapping
        verify(adminDAO, times(1)).findByNameAndPassword("a","123456");

        assertEquals(1, admin_for_login.getId());
        assertEquals("a", admin_for_login.getName());
        assertEquals("123456", admin_for_login.getPassword());
    }

    //根据name和password查到admin不存在
    @Test
    public void should_get_no_admin() {
        //Stub to return value
        when(adminDAO.findByNameAndPassword(anyString(),anyString())).thenReturn(null);
        //Execute
        Admin admin_for_login = adminService.queryUserForLogin("a","123");
        //verify mapping
        verify(adminDAO, times(1)).findByNameAndPassword("a","123");
        assertNull(admin_for_login);
    }

    @Test
    public void should_get_admin() {
        //Stub to return value
        when(adminDAO.findByName(anyString())).thenReturn(admin);
        //Execute
        boolean success = adminService.queryUsernameIsExist("123");
        //verify mapping
        verify(adminDAO, times(1)).findByName("123");
        assertTrue(success);
    }
}
