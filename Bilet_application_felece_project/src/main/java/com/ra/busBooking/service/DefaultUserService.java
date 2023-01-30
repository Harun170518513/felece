package com.ra.busBooking.service;

import com.ra.busBooking.model.BusData;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ra.busBooking.DTO.BookingsDTO;
import com.ra.busBooking.DTO.UserRegisteredDTO;
import com.ra.busBooking.model.Bookings;
import com.ra.busBooking.model.User;

import java.util.List;

public interface DefaultUserService extends UserDetailsService{

	User save(UserRegisteredDTO userRegisteredDTO);

	Bookings updateBookings(BookingsDTO bookingDTO,UserDetails user);
	
	void sendEmail(BookingsDTO bookingDTO, User users, String nameGenrator);


	
}
