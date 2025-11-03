package com.trafficchallan.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trafficchallan.entity.Admin;
import com.trafficchallan.entity.Vehiclechallan;
import com.trafficchallan.service.AdminServiceImplementation;
import com.trafficchallan.service.AppointmentServiceImplementation;
import com.trafficchallan.service.EmailService;
import com.trafficchallan.service.UserService;
import com.trafficchallan.service.VehiclechallanService;


@Controller
@RequestMapping("/admin")
public class AdminController {
	

	private UserService userService;

	 @Autowired
	 private EmailService emailService;
	
	private AdminServiceImplementation adminServiceImplementation;
	
	private AppointmentServiceImplementation appointmentServiceImplementation;

	@Autowired
    private VehiclechallanService vehiclechallanService;
	
	
	@Autowired
	public AdminController(UserService userService,AdminServiceImplementation obj,
			AppointmentServiceImplementation app) {
		this.userService = userService;
		adminServiceImplementation=obj;
		appointmentServiceImplementation=app;
	}
	
	
	@RequestMapping("/user-details")
	public String index(Model model){
		
		
		List<Admin> list=adminServiceImplementation.findByRole("ROLE_USER");
		model.addAttribute("user", list);
		
		
		// get last seen
		String username="";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
		   username = ((UserDetails)principal).getUsername();
		  String Pass = ((UserDetails)principal).getPassword();
		  System.out.println("One + "+username+"   "+Pass);
		  
		  
		} else {
		 username = principal.toString();
		  System.out.println("Two + "+username);
		}
		
		Admin admin = adminServiceImplementation.findByEmail(username);
		  model.addAttribute("name", admin.getFirstName());	 
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date now = new Date();  
		    
		         String log=now.toString();
		    
		         admin.setLastseen(log);
		         
		         adminServiceImplementation.save(admin);
		
		
		
		return "admin/user";
	}
	
	@RequestMapping("/police-details")
	public String doctorDetails(Model model){
		
		
		// get last seen
		String username="";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
		   username = ((UserDetails)principal).getUsername();
		  String Pass = ((UserDetails)principal).getPassword();
		  System.out.println("One + "+username+"   "+Pass);
		  
		  
		} else {
		 username = principal.toString();
		  System.out.println("Two + "+username);
		}
		
		Admin admin = adminServiceImplementation.findByEmail(username);
				 
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date now = new Date();  
		    	
		         String log=now.toString();
		    
		         admin.setLastseen(log);
		         
		         adminServiceImplementation.save(admin);
		
		
		
		List<Admin> list=adminServiceImplementation.findByRole("ROLE_POLICE");
		
		
		
		// add to the spring model
		model.addAttribute("user", list);
		
		
		return "admin/police";
	}
	
	@RequestMapping("/admin-details")
	public String adminDetails(Model model){
		
		
		// get last seen
		String username="";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
		   username = ((UserDetails)principal).getUsername();
		  String Pass = ((UserDetails)principal).getPassword();
		  System.out.println("One + "+username+"   "+Pass);
		  
		  
		} else {
		 username = principal.toString();
		  System.out.println("Two + "+username);
		}
		
		Admin admin = adminServiceImplementation.findByEmail(username);
				 
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date now = new Date();  
		    
		         String log=now.toString();
		    
		         admin.setLastseen(log);
		         
		         adminServiceImplementation.save(admin);
		
		
		         
		List<Admin> list=adminServiceImplementation.findByRole("ROLE_ADMIN");
		
		
		
		// add to the spring model
		model.addAttribute("user", list);
		
		
		return "admin/admin";
	}
	
	
	@GetMapping("/addPolice")
	public String showFormForAdd(Model theModel) {
		
		
		// get last seen
		String username="";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
		   username = ((UserDetails)principal).getUsername();
		  String Pass = ((UserDetails)principal).getPassword();
		  System.out.println("One + "+username+"   "+Pass);
		  
		  
		} else {
		 username = principal.toString();
		  System.out.println("Two + "+username);
		}
		
		Admin admin1 = adminServiceImplementation.findByEmail(username);
				 
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date now = new Date();  
		    
		         String log=now.toString();
		    
		         admin1.setLastseen(log);
		         
		         adminServiceImplementation.save(admin1);         
		// create model attribute to bind form data
		Admin admin = new Admin();
		
		theModel.addAttribute("police", admin);
		
		return "admin/addPolice";
	}
	
	
	@PostMapping("/save-police")
	public String saveEmployee(@ModelAttribute("police") Admin admin) {
		
		// save the employee
	//	admin.setId(0);
		
		admin.setRole("ROLE_POLICE");
		
		admin.setPassword("POLICE");
		
		admin.setEnabled(true);
		
		admin.setConfirmationToken("ByAdmin-Panel");
		
		System.out.println("Adddddddddddddddddddd"+admin);
		System.out.println("EMillllllll"+admin.getEmail());
		// Send email notification
	    if (admin.getEmail() != null) {
	        String to = admin.getEmail();
	        String subject = "Your Police Account Has Been Created";
	        String body = "<!DOCTYPE html>"
	                    + "<html><head><style>"
	                    + "body { font-family: Arial, sans-serif; color: #333; }"
	                    + ".container { padding: 20px; }"
	                    + ".header { color: #007bff; }"
	                    + "</style></head><body>"
	                    + "<div class='container'>"
	                    + "<h3 class='header'>Welcome, " + admin.getFirstName() + "!</h3>"
	                    + "<p>Your police account has been successfully created by the Traffic Admin Panel.</p>"
	                    + "<p><strong>Username (Email):</strong> " + admin.getEmail() + "</p>"
	                    + "<p><strong>Temporary Password:</strong> POLICE</p>"
	                    + "<p>Please log in and change your password immediately.</p>"
	                    + "<br><p>Regards,<br>Traffic Police Department</p>"
	                    + "</div></body></html>";

	        try {
	            emailService.sendEmail(to, subject, body);
	        } catch (MessagingException e) {
	            e.printStackTrace(); // Optionally log this
	        }
	    } 
   
        

		
		adminServiceImplementation.save(admin);
		System.out.println("Password being set: " + admin.getPassword());

		// use a redirect to prevent duplicate submissions
		return "redirect:/admin/user-details";
	}
	
	

	@GetMapping("/add-admin")
	public String showForm(Model theModel) {
		
		
		// get last seen
		String username="";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
		   username = ((UserDetails)principal).getUsername();
		  String Pass = ((UserDetails)principal).getPassword();
		  System.out.println("One + "+username+"   "+Pass);
		  
		  
		} else {
		 username = principal.toString();
		  System.out.println("Two + "+username);
		}
		
		Admin admin1 = adminServiceImplementation.findByEmail(username);
				 
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date now = new Date();  
		    
		         String log=now.toString();
		    
		         admin1.setLastseen(log);
		         
		         adminServiceImplementation.save(admin1);
		
		
		
		// create model attribute to bind form data
		Admin admin = new Admin();
		
		theModel.addAttribute("doctor", admin);
		
		return "admin/addAdmin";
	}
	
	
	@PostMapping("/save-admin")
	public String saveEmploye(@ModelAttribute("doctor") Admin admin) {
		
		// save the employee
	//	admin.setId(0);
		
		admin.setRole("ROLE_ADMIN");
		
		admin.setPassword("default");
		
		admin.setEnabled(true);
		
		admin.setConfirmationToken("ByAdmin-Panel");
		
		System.out.println(admin);
		
		adminServiceImplementation.save(admin);
		
		// use a redirect to prevent duplicate submissions
		return "redirect:/admin/userdetails";
	}
	
	@GetMapping("/edit-my-profile")
	public String EditForm(Model theModel) {
		
		String username="";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
		   username = ((UserDetails)principal).getUsername();
		  String Pass = ((UserDetails)principal).getPassword();
		  System.out.println("One + "+username+"   "+Pass);
		} else {
		 username = principal.toString();
		  System.out.println("Two + "+username);
		}	
		
		// get the employee from the service
		
		Admin admin = adminServiceImplementation.findByEmail(username);
				 
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date now = new Date();  
		    
		         String log=now.toString();
		    
		         admin.setLastseen(log);
		         
		         adminServiceImplementation.save(admin);
		
		System.out.println(admin);
		
		theModel.addAttribute("profile", admin);
		
		return "admin/updateMyProfile";
	}
			
	
	@PostMapping("/update")
	public String update(@ModelAttribute("profile") Admin admin) {
		
		
		System.out.println(admin);
		
		adminServiceImplementation.save(admin);
		
		// use a redirect to prevent duplicate submissions
		return "redirect:/admin/user-details";
	}
	
	
	@RequestMapping("/appointments")
	public String appointments(Model model){
		
		
		// get last seen
		String username="";
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
		   username = ((UserDetails)principal).getUsername();
		  String Pass = ((UserDetails)principal).getPassword();
		  System.out.println("One + "+username+"   "+Pass);
		  
		  
		} else {
		 username = principal.toString();
		  System.out.println("Two + "+username);
		}
		
		Admin admin = adminServiceImplementation.findByEmail(username);
				 
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date now = new Date();  
		    
		         String log=now.toString();
		    
		         admin.setLastseen(log);
		         
		         adminServiceImplementation.save(admin);
		
//		          Load only unpaid challans (fine > 0)
		        List<Vehiclechallan> challans = vehiclechallanService.getAllChallans()
		                .stream().filter(c -> c.getFineAmount() >= 0).toList();
					        // Fetch user by email (assuming User has vehicleNumber)
//					        User user = userService.findByEmail(username);
//					        String vehicleNumber = user.getVehicleNumber();
//					        List<Vehiclechallan> challans = vehiclechallanService.getChallansByVehicleNumber(vehicleNumber);

		        model.addAttribute("challans", challans);   
		
		         
//		List<Appointment> list=appointmentServiceImplementation.findAll();
		
		
		
		// add to the spring model
//		model.addAttribute("app", list);
		
		
		return "admin/appointment";
	}
}
