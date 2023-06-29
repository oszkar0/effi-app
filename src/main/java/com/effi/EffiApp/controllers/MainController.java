package com.effi.EffiApp.controllers;

import com.effi.EffiApp.entity.User;
import com.effi.EffiApp.security.PrincipalInformation;
import com.effi.EffiApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class MainController {
    private UserService userService;
    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/main-page")
    public String getMainPage(Model model){
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

        //get all users from logged users company 
        List<User> companyUsers = principalInformation.getCompany().getUsers();

        model.addAttribute("users", companyUsers);

        return "main-page";
    }

    @GetMapping("/view-user-tasks")
    public String getUserTasks(@RequestParam("userId") int userId, Model model){
        User user = userService.findUserAndHisTasksById(userId);

        model.addAttribute("user", user);
        model.addAttribute("tasks", user.getTasks());

        return "profile-tasks";
    }

}
