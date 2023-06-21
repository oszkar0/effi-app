package com.effi.EffiApp.controllers;

import com.effi.EffiApp.registration.owner.OwnerRegistrationObject;
import com.effi.EffiApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
}
