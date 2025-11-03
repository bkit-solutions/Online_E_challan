package com.trafficchallan.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trafficchallan.entity.Admin;
import com.trafficchallan.entity.Appointment;
import com.trafficchallan.entity.User;
import com.trafficchallan.entity.Vehiclechallan;
import com.trafficchallan.repository.UserRepository;
import com.trafficchallan.repository.VehiclechallanRepository;
import com.trafficchallan.service.AdminServiceImplementation;
import com.trafficchallan.service.AppointmentServiceImplementation;
import com.trafficchallan.service.EmailService;
import com.trafficchallan.service.UserService;
import com.trafficchallan.service.VehiclechallanService;

@Controller
@RequestMapping("/police")  // ✅ Changed from /doctor to /police
public class PoliceController {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private VehiclechallanService vehiclechallanService;
    
    @Autowired
    private UserRepository userRepository;

    private final UserService userService;
    private final AdminServiceImplementation adminServiceImplementation;
    private final AppointmentServiceImplementation appointmentServiceImplementation;

    @Autowired
    public PoliceController(UserService userService,
                            AdminServiceImplementation adminServiceImplementation,
                            AppointmentServiceImplementation appointmentServiceImplementation) {
        this.userService = userService;
        this.adminServiceImplementation = adminServiceImplementation;
        this.appointmentServiceImplementation = appointmentServiceImplementation;
    }

//    @RequestMapping("/index")
//    public String index(Model model) {
//        String username = getLoggedInUsername();
//
//        // Fetch Admin details
//        Admin admin = adminServiceImplementation.findByEmail(username);
//
//        // Update last seen
//        admin.setLastseen(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
//        adminServiceImplementation.save(admin);
//
//        // Get appointments
//        List<Appointment> list = appointmentServiceImplementation.findAll();
//
//        // Add model attributes
//        model.addAttribute("name", admin.getFirstName());
//        model.addAttribute("email", admin.getEmail());
//        model.addAttribute("user", admin.getFirstName() + " " + admin.getLastName());
//        model.addAttribute("app", list);
//        
//        Vehiclechallan newChallan = new Vehiclechallan();
//        List<Vehiclechallan> allChallans = vehiclechallanService.getAllChallans();
//
//        model.addAttribute("police", newChallan);
//        model.addAttribute("challans", allChallans);
//
//        return "police/index";  // Make sure this file exists in templates/police/index.html
//    }

    @GetMapping("/index")
    public String index(Model model) {
        String username = getLoggedInUsername();
        Admin admin = adminServiceImplementation.findByEmail(username);
        admin.setLastseen(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
        adminServiceImplementation.save(admin);

        List<Vehiclechallan> challans = vehiclechallanService.getAllChallans();

        model.addAttribute("name", admin.getFirstName());
        model.addAttribute("email", admin.getEmail());
        model.addAttribute("user", admin.getFirstName() + " " + admin.getLastName());
        model.addAttribute("challans", challans);
        model.addAttribute("police", new Vehiclechallan()); // For form binding

        return "police/index";
    }
    
    
    @GetMapping("/newchallan")
    public String showFormForAdd(Model model) {
        String username = getLoggedInUsername();
        System.out.println("11111111111111111111111");
        Admin admin = adminServiceImplementation.findByEmail(username);
        if (admin != null) {
            String log = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            admin.setLastseen(log);
            adminServiceImplementation.save(admin);
        }
        
       
        Vehiclechallan newChallan = new Vehiclechallan();
        model.addAttribute("police", newChallan);

        return "police/newchallan";
    }

    @PostMapping("/newchallan")
    public String submitForm(@Valid @ModelAttribute("police") Vehiclechallan vehiclechallan,
                             BindingResult bindingResult, Model model) {
    	
    	System.out.println("222222222222222222222222");
        if (bindingResult.hasErrors()) {
            return "police/newchallan";
        }
        
        // Send email notification
            String to = vehiclechallan.getEmail();
            String subject = "Vehicle challan";
            String body  = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<style>"
                    + "  body { font-family: Arial, sans-serif; color: #333; }"
                    + "  .container { padding: 20px; border: 1px solid #ddd; border-radius: 8px; }"
                    + "  .header { color: #007bff; }"
                    + "  .details { margin-top: 10px; }"
                    + "  .footer { margin-top: 20px; font-size: 0.9em; color: #555; }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='container'>"
                    + "<h2 class='header'>Traffic Challan Notification</h2>"
                    + "<p>Hello " + vehiclechallan.getFirstName() + ",</p>"
                    + "<p>This is to inform you that a challan has been issued to your vehicle.</p>"
                    + "<div class='details'>"
                    + "<p><strong>Vehicle Number:</strong> " + vehiclechallan.getVehicleNumber() + "</p>"
                    + "<p><strong>Violation Location:</strong> " + vehiclechallan.getLocation() + "</p>"
                    + "<p><strong>Fine Amount:</strong> ₹" + vehiclechallan.getFineAmount() + "</p>"
                    + "</div>"
                    + "<p>Please visit the dashboard to view or pay the challan.</p>"
                    + "<div class='footer'>"
                    + "<p>Thank you,<br>Traffic Police Department</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";


            try {
                emailService.sendEmail(to, subject, body);
            } catch (MessagingException e) {
                e.printStackTrace(); // Handle error appropriately
            }
        
        

        

        // Save the vehicle challan
        vehiclechallanService.saveChallan(vehiclechallan);

        model.addAttribute("message", "Challan submitted successfully!");
        return "police/newchallan";
    }

    @GetMapping("/search-vehicle")
    public String searchByVehicleNumber(@RequestParam("vehicleNumber") String vehicleNumber, Model model) {
        Optional<User> optionalUser = userRepository.findByVehicleNumber(vehicleNumber);
        Vehiclechallan newChallan = new Vehiclechallan(); // Empty form initially

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Fill user info into new challan
            newChallan.setFirstName(user.getFirstName());
            newChallan.setLastName(user.getLastName());
            newChallan.setEmail(user.getEmail());
            newChallan.setGender(user.getGender());
            newChallan.setVehicleNumber(user.getVehicleNumber());
            System.out.println("hhhhhhhhkkkkkkk"+user.getFirstName());
        }

        
        model.addAttribute("police", newChallan); // send to form
        return "police/newchallan"; // make sure this template exists
    }

    

    @PostMapping("/appointment/{id}/check")
    public String markAppointmentChecked(@PathVariable Integer id,
                                         @RequestParam("doctorName") String doctorName) {
        Appointment appointment = appointmentServiceImplementation.findById(id);
        appointment.setChecked(true);
        appointmentServiceImplementation.save(appointment);

        // Send email notification
//        if (appointment.getEmail() != null) {
//            String to = appointment.getEmail();
//            String subject = "";
//            String body = "<h3>Hello " + appointment.getName() + ",</h3>"
//                        + "<p>Your appointment scheduled for <b>" + appointment.getDate() + "</b> at <b>" + appointment.getTime() + "</b> has been reviewed.</p>"
//                        + "<p><b>:</b> " +  + "</p>"
//                        + "<br><p>Thank you!</p>";
//
//            try {
//                emailService.sendEmail(to, subject, body);
//            } catch (MessagingException e) {
//                e.printStackTrace(); // Handle error appropriately
//            }
//        }

        return "redirect:/police/index";
    }

    // Utility method to get logged-in username
    private String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
