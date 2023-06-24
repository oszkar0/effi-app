package com.effi.EffiApp.controllers;

import com.effi.EffiApp.passwords.PasswordGenerator;
import com.effi.EffiApp.registration.employee.EmployeeRegistrationObject;
import com.effi.EffiApp.registration.owner.OwnerRegistrationObject;
import com.effi.EffiApp.security.PrincipalInformation;
import com.effi.EffiApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    //create logger for information purposes
    private Logger logger = Logger.getLogger(getClass().getName());
    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/show-registration-form")
    public String getRegistrationForm(Model model){
        model.addAttribute("registrationObject", new OwnerRegistrationObject());

        return "registration-form";
    }

    @PostMapping("/process-registration")
    public String processRegistration(
            @Valid @ModelAttribute("registrationObject") OwnerRegistrationObject registrationObject,
            BindingResult bindingResult){
        logger.info("Processing user: " + registrationObject.getUserEmail());

        //check if errors occured, if occurred return filled form with error messages
        if(bindingResult.hasErrors()){
            return "registration-form";
        }

        if(userService.userExists(registrationObject.getUserEmail())){
            logger.warning("User already exists");
            return "redirect:/login";
        }

        userService.save(registrationObject);

        logger.info("Successfully saved user " + registrationObject.getUserEmail()  + " to db");

        return "redirect:/login";
    }

    @GetMapping("/show-employee-registration-form")
    public String getEmployeeRegistrationForm(Model model){
        model.addAttribute("employeeRegistrationObject", new EmployeeRegistrationObject());

        return "employee-form";
    }

    @PostMapping("/process-employee-registration")
    public String processEmployeeRegistration(
            @Valid @ModelAttribute("registrationObject") EmployeeRegistrationObject employeeRegistrationObject,
            BindingResult bindingResult){

        logger.info("Processing user: " + employeeRegistrationObject.getEmail());

        //check if errors occurred, if occurred return filled form with error messages
        if(bindingResult.hasErrors()){
            return "employee-form";
        }

        //check if user is already in db
        if(userService.userExists(employeeRegistrationObject.getEmail())){
            logger.warning("User already exists");
            return "redirect:/main-page";
        }

        //generate and set random password which can be changed by employee later
        String password = PasswordGenerator.generatePassword(10);
        employeeRegistrationObject.setPassword(password);

        //get the logged user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object loggedUser = authentication.getPrincipal();
        PrincipalInformation principalInformation = null;

        if (loggedUser instanceof UserDetails) {
            principalInformation = ((PrincipalInformation)loggedUser);
        } else {
            String email = loggedUser.toString();
            principalInformation = (PrincipalInformation) userService.loadUserByUsername(email);
        }

        //set new employee's company to admins(owners) company and add user to company
        employeeRegistrationObject.setCompany(principalInformation.getCompany());

        userService.save(employeeRegistrationObject);

        //todo: redirect the admin to new user info page
        return "redirect:/main-page";
    }
}
