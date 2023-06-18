package com.effi.EffiApp.controllers;

import com.effi.EffiApp.entity.User;
import com.effi.EffiApp.security.PrincipalInformation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;

@Controller
public class MainController {
    @GetMapping("/main-page")
    public String getMainPage(Model model){
        //get the logged user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalInformation loggedUser = (PrincipalInformation) authentication.getPrincipal();

        //get all users from logged users company 
        List<User> companyUsers = loggedUser.getCompany().getUsers();

        model.addAttribute("users", companyUsers);

        return "main-page";
    }

}
