package com.meethere.interceptor;

import com.meethere.pojo.Admin;
import com.meethere.pojo.User;
import com.meethere.util.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserLoginInterceptor implements HandlerInterceptor {
    @Autowired
    public RedisOperator redis;
    public static final String USER_REDIS_SESSION= "user-redis-session";

	@Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
    	HttpSession session = httpServletRequest.getSession();
            User user = (User) session.getAttribute("user");
			if(user==null) {
				httpServletResponse.sendRedirect("login");
				return false;
			}
			else{
			    String uniqueToken = redis.get(USER_REDIS_SESSION + ":" + user.getId());
			    if(!user.getUserToken().equals(uniqueToken)){
                    httpServletResponse.sendRedirect("login");
                    return false;
                }
            }

        return true;   
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
