package com.meethere.controller;

import com.meethere.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ForePageController extends BasicController{
//	@GetMapping(value="/")
//	public String index(){
//		return "redirect:home";
//	}
	@GetMapping(value="/home")
	public String home(){
		return "fore/home";
	}
	@GetMapping(value="/register")
	public String register(){
		return "fore/register";
	}
	@GetMapping(value="/profile")
	public String profile(){
		return "fore/profile";
	}
	@GetMapping(value="/password")
	public String password(){
		return "fore/password";
	}
	@GetMapping(value="/new")
	public String thenew(){
		return "fore/new";
	}
	@GetMapping(value="/forenews")
	public String news(){
		return "fore/news";
	}
	@GetMapping(value="/venue")
	public String venue(){
		return "fore/venue";
	}
	@GetMapping(value="/forevenues")
	public String venues(){
		return "fore/venues";
	}
	@GetMapping(value="/pay")
	public String pay(){
		return "fore/pay";
	}
	@GetMapping(value="/bought")
	public String bought(){
		return "fore/bought";
	}
	@GetMapping(value="/confirmPay")
	public String confirmPay(){
		return "fore/confirmPay";
	}
	@GetMapping(value="/login")
	public String login(){
		return "fore/login";
	}
	@GetMapping(value="/orderConfirmed")
	public String orderConfirmed(){
		return "fore/orderConfirmed";
	}
//	@GetMapping(value="/payed")
//	public String payed(){
//		return "fore/payed";
//	}
	@GetMapping(value="/product")
	public String product(){
		return "venue";
	}
	@GetMapping(value="/review")
	public String review(){
		return "fore/review";
	}
//	@GetMapping(value="/search")
//	public String searchResult(){
//		return "fore/venues";
//	}
	@GetMapping("/forelogout")
	public String logout(HttpSession session) {
		User user = (User) session.getAttribute("user");
		redis.del(USER_REDIS_SESSION + ":" + user.getId());
		session.removeAttribute("user");
		return "redirect:home";
	}

}

