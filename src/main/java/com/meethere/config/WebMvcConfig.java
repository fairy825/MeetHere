package com.meethere.config;

import com.meethere.interceptor.AdminLoginInterceptor;
import com.meethere.interceptor.UserLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{
	@Bean
	public AdminLoginInterceptor getAdminLoginIntercepter() {
		return new AdminLoginInterceptor();
	}
	@Bean
	public UserLoginInterceptor getUserLoginInterceptor() {
		return new UserLoginInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getAdminLoginIntercepter())
				.addPathPatterns("/**");
		registry.addInterceptor(getUserLoginInterceptor())
				.addPathPatterns("/profile")
				.addPathPatterns("/password")
				.addPathPatterns("/bought")
				.addPathPatterns("/pay")
				.addPathPatterns("/review")
				.addPathPatterns("/users/upload")
				.addPathPatterns("/users/password")
				.addPathPatterns("/bookings/mine");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		//所有请求都允许跨域
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("*")
				.allowedHeaders("*");
	}
}