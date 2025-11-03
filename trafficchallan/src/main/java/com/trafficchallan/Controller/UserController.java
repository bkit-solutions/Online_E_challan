package com.trafficchallan.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trafficchallan.entity.Admin;
import com.trafficchallan.entity.Appointment;
import com.trafficchallan.entity.User;
import com.trafficchallan.entity.Vehiclechallan;
import com.trafficchallan.repository.VehiclechallanRepository;
import com.trafficchallan.service.AdminServiceImplementation;
import com.trafficchallan.service.AppointmentServiceImplementation;
import com.trafficchallan.service.UserService;
import com.trafficchallan.service.VehiclechallanService;


@Controller
@RequestMapping("/user")
public class UserController {

	private AppointmentServiceImplementation appointmentServiceImplementation;

	private AdminServiceImplementation adminServiceImplementation;
	
	@Autowired
    private VehiclechallanService vehiclechallanService;
	
	@Autowired
	private VehiclechallanRepository vehiclechallanRepository;


 
    @Autowired
    private UserService userService;
	
	@Autowired
	public UserController(AppointmentServiceImplementation obj1,AdminServiceImplementation obj ) {
		appointmentServiceImplementation= obj1;
		adminServiceImplementation=obj;
		 
	}
	
	
	@GetMapping("/index")
	public String index(Model model){
		
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
		
	        admin.setLastseen(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
	        adminServiceImplementation.save(admin);

//	        List<Vehiclechallan> challans = vehiclechallanService.getAllChallans();
	        // Fetch user by email (assuming User has vehicleNumber)
	        User user = userService.findByEmail(username);
	        String vehicleNumber = user.getVehicleNumber();

	        List<Vehiclechallan> challans = vehiclechallanService.getChallansByVehicleNumber(vehicleNumber);


	        model.addAttribute("name", admin.getFirstName());
	        model.addAttribute("email", admin.getEmail());
	        model.addAttribute("user", admin.getFirstName() + " " + admin.getLastName());
	        model.addAttribute("challans", challans);
		
				 
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		    Date now = new Date();  
		    
		         String log=now.toString();
		    
		         admin.setLastseen(log);
		         
		         adminServiceImplementation.save(admin);
		
		 
		         
		 Appointment obj=new Appointment();
		 
		 obj.setName(admin.getFirstName()+" "+admin.getLastName());
		 
		 obj.setEmail(admin.getEmail());
			
		 System.out.println(obj);
		 
		 model.addAttribute("app",obj);
		
		return "user/index";
	}
	
	@PostMapping("/save-app")
	public String saveEmploye(@ModelAttribute("app") Appointment obj) {
		
		appointmentServiceImplementation.save(obj);
		
		
		
	
		// use a redirect to prevent duplicate submissions
		return "redirect:/user/index";
	}

	


//
//	// View challan details
//    @GetMapping("/view/{id}")
//    public String viewChallan(@PathVariable Long id, Model model) {
//        Optional<Vehiclechallan> challan = challanRepository.findById(id);
//        if (challan.isPresent()) {
//            model.addAttribute("challan", challan.get());
//            return "user/view-challan";
//        } else {
//            return "error"; // fallback if challan not found
//        }
//    }
//
//    // Pay challan
//    @GetMapping("/pay/{id}")
//    public String payChallan(@PathVariable Long id, Model model) {
//        Optional<Vehiclechallan> challan = challanRepository.findById(id);
//        if (challan.isPresent()) {
//            model.addAttribute("challan", challan.get());
//            return "user/pay-challan";
//        } else {
//            return "error";
//        }
//    }


	
//	@GetMapping("/view-challan")
//    public String viewChallans(Model model) {
//        List<Vehiclechallan> challans = vehiclechallanService.getAllChallans();
//        String username="";
//		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		if (principal instanceof UserDetails) {
//		   username = ((UserDetails)principal).getUsername();
//		  String Pass = ((UserDetails)principal).getPassword();
//		  System.out.println("One + "+username+"   "+Pass);
//		  
//		  
//		} else {
//		 username = principal.toString();
//		  System.out.println("Two + "+username);
//		}
//		
//        
//        Admin admin = adminServiceImplementation.findByEmail(username);
//		
//        admin.setLastseen(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
//        adminServiceImplementation.save(admin);
//
//       
//        model.addAttribute("name", admin.getFirstName());
//        model.addAttribute("email", admin.getEmail());
//        model.addAttribute("user", admin.getFirstName() + " " + admin.getLastName());
//        model.addAttribute("challans", challans);
//	
//        model.addAttribute("challans", challans);
//        return "user/view-challan";
//    }
	
	@GetMapping("/view-challan")
    public String viewChallans(Model model) {
        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Admin admin = adminServiceImplementation.findByEmail(username);
        model.addAttribute("name", admin.getFirstName());
        model.addAttribute("email", admin.getEmail());
        model.addAttribute("user", admin.getFirstName() + " " + admin.getLastName());
        
        // Fetch user by email (assuming User has vehicleNumber)
        User user = userService.findByEmail(username);
        String vehicleNumber = user.getVehicleNumber();

        List<Vehiclechallan> challans = vehiclechallanService.getChallansByVehicleNumber(vehicleNumber);

        model.addAttribute("name", user.getFirstName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("user", user.getFirstName() + " " + user.getLastName());
        model.addAttribute("challans", challans);

        return "user/view-challan";  // Thymeleaf page to display challans
    }

	@GetMapping("/pay-challan")
    public String showPayChallanPage(Model model) {
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
			        model.addAttribute("email", admin.getEmail());
			        model.addAttribute("user", admin.getFirstName() + " " + admin.getLastName());
			      
		
        // Load only unpaid challans (fine > 0)
//        List<Vehiclechallan> challans = vehiclechallanService.getAllChallans()
//                .stream().filter(c -> c.getFineAmount() > 0).toList();
			        // Fetch user by email (assuming User has vehicleNumber)
			        User user = userService.findByEmail(username);
			        String vehicleNumber = user.getVehicleNumber();
			        List<Vehiclechallan> challans = vehiclechallanService.getChallansByVehicleNumber(vehicleNumber);

        model.addAttribute("challans", challans);
        return "user/pay-challan";
    }

    @PostMapping("/pay-challan/{id}")
    public String payChallan(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Vehiclechallan challan = vehiclechallanService.findById(id);
        if (challan != null && challan.getFineAmount() > 0) {
            challan.setFineAmount(0);
         
            vehiclechallanService.saveChallan(challan);
            redirectAttributes.addFlashAttribute("message", "Payment successful!");
        }
        return "redirect:/user/pay-challan";
    }




}