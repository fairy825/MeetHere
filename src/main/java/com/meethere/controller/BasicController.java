package com.meethere.controller;

import com.meethere.util.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class BasicController {
	
	@Autowired
	public RedisOperator redis;

	public static final String USER_REDIS_SESSION = "user-redis-session";
	public static final String ADMIN_REDIS_SESSION = "admin-redis-session";
	public static final String FILE_SPACE = "D:/workspace/meethere/src/main/webapp/img";
		
		// 每页分页的记录数
		public static final Integer PAGE_SIZE = 3;
	
}
