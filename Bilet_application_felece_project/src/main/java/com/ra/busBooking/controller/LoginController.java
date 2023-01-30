package com.ra.busBooking.controller;

import com.ra.busBooking.model.Bookings;
import com.ra.busBooking.model.BusData;
import com.ra.busBooking.model.User;
import com.ra.busBooking.repository.BusDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ra.busBooking.DTO.UserLoginDTO;

import com.ra.busBooking.service.DefaultUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/login")
public class LoginController {
	@Autowired
	private DefaultUserService userService;
	@Autowired
	private BusDataRepository dataRepository;

	@ModelAttribute("user")
	public UserLoginDTO userLoginDTO() {
		return new UserLoginDTO();
	}

	@GetMapping
	public String login() {
		return "login";
	}

	@PostMapping
	public void loginUser(@ModelAttribute("user")
						  UserLoginDTO userLoginDTO) {
		userService.loadUserByUsername(userLoginDTO.getUsername());
	}


}
