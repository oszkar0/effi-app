package com.effi.EffiApp.controllers;

import com.effi.EffiApp.registration.RegistartionObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    @GetMapping("/show-registration-form")
    public String getRegistrationForm(Model model){
        model.addAttribute("registrationObject", new RegistartionObject());

        return "registration-form";
    }
}
