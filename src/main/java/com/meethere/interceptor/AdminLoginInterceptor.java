package com.meethere.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.meethere.pojo.Admin;
import com.meethere.util.IMoocJSONResult;
import com.meethere.util.JsonUtils;
import com.meethere.util.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class AdminLoginInterceptor implements HandlerInterceptor {
    @Autowired
    public RedisOperator redis;
    public static final String ADMIN_REDIS_SESSION= "admin-redis-session";

	@Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
    	HttpSession session = httpServletRequest.getSession();
        String contextPath=session.getServletContext().getContextPath();
        
        String uri = httpServletRequest.getRequestURI();

        uri = StringUtils.remove(uri, contextPath+"/");
        String page = uri;
//        String to = "/"+contextPath+"/admin/login";
		if(StringUtils.startsWith(page, "admin")){
            Admin admin = (Admin) session.getAttribute("admin");
			if(admin==null) {
				httpServletResponse.sendRedirect("alogin");
				return false;
			}
			else{
			    String uniqueToken = redis.get(ADMIN_REDIS_SESSION + ":" + admin.getId());
			    if(!admin.getAdminToken().equals(uniqueToken)){
                    httpServletResponse.sendRedirect("alogin");
                    return false;
                }
            }
		}
        return true;   
    }
//    private boolean begingWith(String page, String[] requiredAuthPages) {
//    	boolean result = false;
//    	for (String requiredAuthPage : requiredAuthPages) {
//			if(StringUtils.startsWith(page, requiredAuthPage)) {
//				result = true;
//				break;
//			}
//		}
//    	return result;
//	}

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
