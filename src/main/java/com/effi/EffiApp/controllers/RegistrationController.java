package com.effi.EffiApp.controllers;

import com.effi.EffiApp.endpoints.Endpoints;
import com.effi.EffiApp.entity.Task;
import com.effi.EffiApp.entity.User;
import com.effi.EffiApp.registration.task.TaskRegistrationObject;
import com.effi.EffiApp.utils.passwords.PasswordGenerator;
import com.effi.EffiApp.registration.employee.EmployeeRegistrationObject;
import com.effi.EffiApp.registration.owner.OwnerRegistrationObject;
import com.effi.EffiApp.security.PrincipalInformation;
import com.effi.EffiApp.service.UserService;
import com.effi.EffiApp.utils.pdfs.PdfGeneration;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.util.logging.Logger;

@Controller
@RequestMapping(Endpoints.REGISTRATION_PREFIX)
public class RegistrationController {
    //create logger for information purposes
    private Logger logger = Logger.getLogger(getClass().getName());
    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(Endpoints.NEW_COMPANY_FORM)
    public String getCompanyRegistrationForm(Model model){
        model.addAttribute("registrationObject", new OwnerRegistrationObject());

        return "registration-form";
    }

    @PostMapping(Endpoints.NEW_COMPANY_PROCESSING)
    public String processCompanyRegistration(
            @Valid @ModelAttribute("registrationObject") OwnerRegistrationObject registrationObject,
            BindingResult bindingResult){

        //check if errors occured, if occurred return filled form with error messages
        if(bindingResult.hasErrors()){
            return "registration-form";
        }

        if(userService.userExists(registrationObject.getUserEmail())){
            return "redirect:" + Endpoints.LOGIN_LOGGING;
        }

        userService.save(registrationObject);

        return "redirect:" + Endpoints.LOGIN_LOGGING;
    }
}
