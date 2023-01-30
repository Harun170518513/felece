package com.ra.busBooking.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.ra.busBooking.model.BusData;
import com.ra.busBooking.repository.BusDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.ra.busBooking.DTO.BookingsDTO;
import com.ra.busBooking.DTO.UserRegisteredDTO;
import com.ra.busBooking.model.Bookings;
import com.ra.busBooking.model.Role;
import com.ra.busBooking.model.User;
import com.ra.busBooking.repository.BookingsRepository;
import com.ra.busBooking.repository.RoleRepository;
import com.ra.busBooking.repository.UserRepository;

@Service
public class DefaultUserServiceImpl implements DefaultUserService{
   @Autowired
	private UserRepository userRepo;
   
   @Autowired
	private BookingsRepository bookingRepository;
	
   @Autowired
  	private RoleRepository roleRepo;
  	
   @Autowired
   private TemplateEngine templateEngine;
   @Autowired
   private BusDataRepository dataRepository;
   
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	
		User user = userRepo.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRole()));		
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
	}

	@Override
	public User save(UserRegisteredDTO userRegisteredDTO) {
		Role role = new Role();
		if(userRegisteredDTO.getRole().equals("USER"))
		  role = roleRepo.findByRole("USER");
		else if(userRegisteredDTO.getRole().equals("ADMIN"))
		 role = roleRepo.findByRole("ADMIN");
		User user = new User();
		user.setEmail(userRegisteredDTO.getEmail_id());
		user.setName(userRegisteredDTO.getName());
		user.setPassword(passwordEncoder.encode(userRegisteredDTO.getPassword()));
		user.setRole(role);
		
		return userRepo.save(user);
	}

	@Override
	public Bookings updateBookings(BookingsDTO bookingDTO,UserDetails user) {
		Bookings booking = new Bookings();
		String email = user.getUsername();
		User users = userRepo.findByEmail(email);
		booking.setBusName(bookingDTO.getBusName());
		booking.setFilterDate(bookingDTO.getFilterDate());
		booking.setFromDestination(bookingDTO.getFromDestination());
		booking.setToDestination(bookingDTO.getToDestination());
		booking.setNoOfPersons(bookingDTO.getNoOfPersons());
		booking.setTotalCalculated(bookingDTO.getTotalCalculated());
		booking.setTime(bookingDTO.getTime());
		booking.setUserId(users.getId());
		booking.setTripStatus(true);
		String filename=generatePDFAndSendMail(bookingDTO,users);
		booking.setFileName(filename);
		return bookingRepository.save(booking);
	}

	private String generatePDFAndSendMail(BookingsDTO bookingDTO, User users) {
		
			int random = (int) (Math.random() * 90) + 10;
			String nameGenrator = users.getName()+"_ticket_"+random+".pdf";
			try {
				createPdf(bookingDTO,users ,nameGenrator);
				sendEmail(bookingDTO,users ,nameGenrator);
            return nameGenrator;
			} catch (DocumentException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
				}
	@Override
	public void sendEmail(BookingsDTO bookingDTO, User users, String nameGenrator) {
		try {
			final String username = "";//email id of sender
		    final String password = "";//application password of Gmail , I dont know how to generate watch this https://bit.ly/3PY4IeS

		    Properties props = new Properties();
		    props.put("mail.smtp.auth", true);
		    props.put("mail.smtp.starttls.enable", true);
		    props.put("mail.smtp.host", "smtp.gmail.com");
		    props.put("mail.smtp.port", "587");

		    Session session = Session.getInstance(props,
		            new javax.mail.Authenticator() {
		                protected PasswordAuthentication getPasswordAuthentication() {
		                    return new PasswordAuthentication(username, password);
		                }
		            });

		         Message message = new MimeMessage(session);
		         message.setFrom(new InternetAddress(username));
		         message.setRecipients(Message.RecipientType.TO,
		            InternetAddress.parse(users.getEmail()));
		         message.setSubject("Testing Subject");
		         BodyPart messageBodyPart = new MimeBodyPart();
		         messageBodyPart.setText("This is message body");
		         Multipart multipart = new MimeMultipart();
		         multipart.addBodyPart(messageBodyPart);
		         messageBodyPart = new MimeBodyPart();
		         String filename = ""+nameGenrator+"";//directory in which pdf is created
		         DataSource source = new FileDataSource(filename);
		         messageBodyPart.setDataHandler(new DataHandler(source));
		         messageBodyPart.setFileName(filename);
		         multipart.addBodyPart(messageBodyPart);
		         message.setContent(multipart);
		            Transport.send(message);
	         System.out.println("Deneme mesajı basarıyla goderılmıstır");
		         

		} catch (MessagingException e) {
			e.printStackTrace();
		}

		
		
	}

	public void createPdf(BookingsDTO booking,User users,String nameGenrator) throws DocumentException, IOException {
		Context context = new Context();
		context.setVariable("name", users.getName());
		context.setVariable("date", booking.getFilterDate());
		context.setVariable("noOfPass", booking.getNoOfPersons());
		context.setVariable("From", booking.getFromDestination());
		context.setVariable("to", booking.getToDestination());
		context.setVariable("busName", booking.getBusName());
	
		String processHTML = templateEngine.process("template", context);
		
		try {
			OutputStream out = new FileOutputStream(""+nameGenrator+"");//directory in which you have to generate pdf of Ticket
			ITextRenderer ir = new ITextRenderer();
			ir.setDocumentFromString(processHTML);
			ir.layout();
			ir.createPDF(out, false);
			ir.finishPDF();
			out.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}


}
