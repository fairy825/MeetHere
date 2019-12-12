package com.meethere.service.impl.interationTest;

import com.meethere.Application;
import com.meethere.dao.AdminDAO;
import com.meethere.pojo.Admin;
import com.meethere.service.AdminService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * @description:
 * @author: ivy034
 * @create: 2019-12-12 15:44
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AdminServiceImplIntegrationTest {
	@Autowired
	private AdminDAO adminDAO;
	@Autowired
	private AdminService adminService;
	private Admin admin;
	@Before//对应jnuit5的beforeEach 4的@beforeClass对应5的beforeAll
	public void setUp() throws Exception {
//		admin = new Admin.AdminBuilder().id(1).name("a").password("123456").build();
	}

	@Test
	@Transactional
	@Rollback
	public void database_integrity_test() {

		admin = new Admin.AdminBuilder().id(1).name("a").password("123456").build();

		adminDAO.save(admin);

        //queryUserForLogin
		Admin admin_for_login = adminService.queryUserForLogin("a","123456");

		assertEquals("a", admin_for_login.getName());
		assertEquals("123456", admin_for_login.getPassword());

		//queryUsernameIsExist
		boolean isExist1=adminService.queryUsernameIsExist("a");
		assertTrue(isExist1);

		boolean isExist2=adminService.queryUsernameIsExist("b");
		assertFalse(isExist2);
	}

}
