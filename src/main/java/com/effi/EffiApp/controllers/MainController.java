package com.effi.EffiApp.controllers;

import com.effi.EffiApp.entity.Task;
import com.effi.EffiApp.entity.User;
import com.effi.EffiApp.security.PrincipalInformation;
import com.effi.EffiApp.service.TaskService;
import com.effi.EffiApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class MainController {
    private UserService userService;
    private TaskService taskService;
    @Autowired
    public MainController(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/main-page")
    public String getMainPage(Model model){
        //get logged user
        PrincipalInformation principalInformation = getPrincipalInformation();

        //get all users from logged users company 
        List<User> companyUsers = principalInformation.getCompany().getUsers();

        model.addAttribute("users", companyUsers);

        return "main-page";
    }

    @GetMapping("/view-user-tasks")
    public String getUserTasks(@RequestParam("userId") int userId, Model model){
        PrincipalInformation principalInformation = getPrincipalInformation();

        User user = userService.findUserAndHisTasksById(userId);

        model.addAttribute("user", user);
        model.addAttribute("tasks", user.getTasks());

        return "profile-tasks";
    }

    @GetMapping("/task-details")
    public String getTaskDetails(@RequestParam("taskId") int taskId, Model model) {
        Task task = taskService.findTaskById(taskId);

        model.addAttribute("task", task);
        model.addAttribute("taskUpdated", task);

        return "task-details";
    }

    @PostMapping("/task-update")
    public String updateTask(@Valid @ModelAttribute("taskUpdated") Task taskUpdated){
        Task task = taskService.findTaskById(taskUpdated.getId());

        task.setDeadline(taskUpdated.getDeadline());
        task.setStatus(taskUpdated.getStatus());

        taskService.save(task);
        return "redirect:/task-details?taskId=" + task.getId();
    }

    private PrincipalInformation getPrincipalInformation(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object loggedUser = authentication.getPrincipal();
        PrincipalInformation principalInformation = null;

        if (loggedUser instanceof UserDetails) {
            principalInformation = ((PrincipalInformation)loggedUser);
        } else {
            String email = loggedUser.toString();
            principalInformation = (PrincipalInformation) userService.loadUserByUsername(email);
        }

        return principalInformation;
    }

    @GetMapping("/access-denied")
    public String getAccessDeniedPage(){
        return "access-denied";
    }
}
