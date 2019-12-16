package com.meethere.controller;

import com.meethere.pojo.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Controller
public class AdminPageController extends BasicController{
	@GetMapping(value="/")
	public String index(){
		return "redirect:first";
	}
	@GetMapping(value="/first")
	public String first(){
		return "first";
	}
	@GetMapping(value="/alogin")
	public String adminLogin(){
		return "admin/adminLogin";
	}
	@GetMapping(value="/alogout")
	public String adminLogout(HttpSession session){
		Admin admin = (Admin)session.getAttribute("admin");
		redis.del(ADMIN_REDIS_SESSION + ":" + admin.getId());
		session.removeAttribute("admin");
		return "redirect:first";
	}
	@GetMapping(value="/admin")
	public String admin(){
		return "redirect:admin_district_list";
	}

	@GetMapping(value="/admin_district_list")
	public String listDistrict(){
		return "admin/listDistrict";
	}

	@GetMapping(value="/admin_district_edit")
	public String editDistrict(){
		return "admin/editDistrict";
	}
	@GetMapping(value="/admin_news_list")
	public String listNews(){
		return "admin/listNews";
	}
	@GetMapping(value="/admin_message_list")
	public String listMessage(){
		return "admin/listMessage";
	}

	@GetMapping(value="/admin_venue_list")
	public String listVenue(){
		return "admin/listVenue";
	}

	@GetMapping(value="/admin_venue_edit")
	public String editVenue(){
		return "admin/editVenue";
	}
	@GetMapping(value="/admin_venueImage_list")
	public String listVenueImage(){
		return "admin/listVenueImage";
	}

	@GetMapping(value="/admin_user_list")
	public String listUser(){
		return "admin/listUser";
	}
	@GetMapping(value="/admin_timeSlot_list")
	public String listTimeSlot(){
		return "admin/listTimeSlot";
	}

	@GetMapping(value="/admin_booking_list")
	public String listBooking(){
		return "admin/listBooking";
	}
}
