package com.effi.EffiApp.controllers;

import com.effi.EffiApp.entity.Task;
import com.effi.EffiApp.entity.User;
import com.effi.EffiApp.security.PrincipalInformation;
import com.effi.EffiApp.service.CompanyService;
import com.effi.EffiApp.service.RoleService;
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


import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Controller
public class MainController {
    private UserService userService;
    private TaskService taskService;
    private CompanyService companyService;
    private RoleService roleService;
    @Autowired
    public MainController(UserService userService, TaskService taskService, CompanyService companyService,
                          RoleService roleService) {
        this.userService = userService;
        this.taskService = taskService;
        this.companyService = companyService;
        this.roleService = roleService;
    }

    @GetMapping("/employees-list")
    public String getMainPage(Model model){
        //get logged user
        PrincipalInformation principalInformation = getPrincipalInformation();

        //get all users from logged users company
        List<User> companyUsers = companyService.findCompanyById(principalInformation.getCompany().getId()).getUsers();

        model.addAttribute("users", companyUsers);

        return "employees-list-page";
    }

    @GetMapping("/view-user-tasks")
    public String getUserTasks(@RequestParam("userId") int userId, Model model){
        PrincipalInformation principalInformation = getPrincipalInformation();

        if(principalInformation.getId() != userId &&
                !principalInformation.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))){
            throw new AccessDeniedException("Access denied");
        }

        User user = userService.findUserAndHisTasksById(userId);

        model.addAttribute("user", user);
        model.addAttribute("tasks", user.getTasks());

        return "users-tasks-page";
    }

    @GetMapping("/task-details")
    public String getTaskDetails(@RequestParam("taskId") int taskId, Model model) {
        PrincipalInformation principalInformation = getPrincipalInformation();

        checkNormalEmployeeAccessingHisTask(principalInformation, taskId);

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

    @GetMapping("/delete-user")
    public String deleteUser(@RequestParam("userId") int userId){
        User user = userService.findUserById(userId);

        //check if we aredeleting company owner
        if(user.getRoles().contains(roleService.findRoleByName("ROLE_ADMIN"))){
            throw new RuntimeException("Can not delete company owner");
        }

        userService.deleteUserById(userId);

        return "redirect:/main-page";
    }

    @GetMapping("/delete-task")
    public String deleteTask(@RequestParam("taskId") int taskId){
        PrincipalInformation principalInformation = getPrincipalInformation();

        checkNormalEmployeeAccessingHisTask(principalInformation, taskId);

        //find user id to redirect to users tasks
        Long userId = taskService.findTaskById(taskId).getUser().getId();

        taskService.deleteTaskById(taskId);

        return "redirect:/view-user-tasks?userId=" + userId;
    }

    private void checkNormalEmployeeAccessingHisTask(PrincipalInformation principalInformation,int taskId)
            throws AccessDeniedException{
        //check condition that normal employee can access only his tasks
        if(!principalInformation.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))
                && !taskService.findTaskByUserId(principalInformation.getId().intValue())
                .stream()
                .map(task -> task.getId())
                .anyMatch(id -> id.equals(taskId))){
            throw new AccessDeniedException("Access denied");
        }
    }

    @GetMapping("/main-page")
    public String getTmpMain(Model model){
        PrincipalInformation principalInformation = getPrincipalInformation();

        List<Task> todaysTasks = taskService.findTaskByUserIdAndDeadline(principalInformation.getId().intValue(),
                Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        model.addAttribute("tasks", todaysTasks);

        return "main-page";
    }

    @GetMapping("/my-profile")
    public String redirectToUsersTasks(){
        PrincipalInformation principalInformation = getPrincipalInformation();

        return "redirect:/view-user-tasks?userId=" + principalInformation.getId();
    }
}
