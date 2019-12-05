package com.meethere.controller;

import java.util.UUID;

import com.meethere.pojo.Admin;
import com.meethere.pojo.User;
import com.meethere.service.AdminService;
import com.meethere.service.UserService;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


@RestController
public class RegistLoginController extends BasicController{
	
	@Autowired
	UserService userService;
	@Autowired
	AdminService adminService;

	@PostMapping("/regist")
	public IMoocJSONResult regist(@RequestBody User user) throws Exception{
		
		if(StringUtils.isBlank(user.getName()) || StringUtils.isBlank(user.getPassword())) {
			return IMoocJSONResult.errorMsg("用户名或密码不能为空");
		}
		boolean usernameIsExist = userService.queryUsernameIsExist(user.getName());
		if(!usernameIsExist) {
			user.setNickname(user.getName());
			user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
			
			userService.saveUser(user);
		}
		else {
			return IMoocJSONResult.errorMsg("用户名已存在");
		}
		return IMoocJSONResult.ok(user);
	}

	public User setUserRedisSessionToken(User userModel) {
		String uniqueToken = UUID.randomUUID().toString();
		redis.set(USER_REDIS_SESSION + ":" + userModel.getId(), uniqueToken, 1000 * 60 * 30);
		
		User user = new User();
		BeanUtils.copyProperties(userModel, user);

		user.setUserToken(uniqueToken);
		return user;
	}

	@PostMapping("/login")
	public IMoocJSONResult login(@RequestBody User user, HttpSession session) throws Exception {
		String username = user.getName();
		String password = user.getPassword();
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return IMoocJSONResult.errorMsg("用户名或密码不能为空");
		}
		Boolean flag = userService.queryUsernameIsExist(username);
		if (flag) {
			User userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(user.getPassword()));
			if (userResult != null) {
				User userVO = setUserRedisSessionToken(userResult);
				session.setAttribute("user", userVO);
				return IMoocJSONResult.ok(userVO);
			}else{
				return IMoocJSONResult.errorMsg("密码错误");
			}
		} else {
			return IMoocJSONResult.errorMsg("用户不存在");
		}
	}

	@PostMapping("/logout")
	public IMoocJSONResult logout(String userId) throws Exception {
		redis.del(USER_REDIS_SESSION + ":" + userId);
		return IMoocJSONResult.ok();
	}

	public Admin setAdminRedisSessionToken(Admin admin) {
		String uniqueToken = UUID.randomUUID().toString();
		redis.set(ADMIN_REDIS_SESSION + ":" + admin.getId(), uniqueToken, 1000 * 60 * 30);

		Admin admin1 = new Admin();
		BeanUtils.copyProperties(admin, admin1);

		admin1.setAdminToken(uniqueToken);
		return admin1;
	}

	@PostMapping("/loginAdmin")
	public IMoocJSONResult adminLogin(@RequestBody Admin admin, HttpSession session) throws Exception {
		String username = admin.getName();
		String password = admin.getPassword();
		if(StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return IMoocJSONResult.errorMsg("登录名或密码不能为空");
		}
		Boolean flag = userService.queryUsernameIsExist(username);
		if (flag) {
			Admin admin1 = adminService.queryUserForLogin(username, admin.getPassword());
			if (admin1 != null) {
				Admin a = setAdminRedisSessionToken(admin1);
				session.setAttribute("admin", a);

				return IMoocJSONResult.ok(a);
			}else{
				return IMoocJSONResult.errorMsg("密码错误");
			}
		}
		else {
			return IMoocJSONResult.errorMsg("管理员不存在");
		}
	}

	@GetMapping("/logoutAdmin")
	public IMoocJSONResult adminLogout(HttpSession session) throws Exception {
		Admin admin = (Admin)session.getAttribute("admin");
		redis.del(ADMIN_REDIS_SESSION + ":" + admin.getId());
		session.removeAttribute("admin");
		return IMoocJSONResult.ok();
	}
}