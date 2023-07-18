package com.effi.EffiApp.controllers;

import com.effi.EffiApp.endpoints.Endpoints;
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

    @GetMapping(Endpoints.EMPLOYEES_LIST)
    public String getMainPage(Model model){
        //get logged user
        PrincipalInformation principalInformation = getPrincipalInformation();

        //get all users from logged users company
        List<User> companyUsers = companyService.findCompanyById(principalInformation.getCompany().getId()).getUsers();

        model.addAttribute("users", companyUsers);

        return "employees-list-page";
    }

    @GetMapping(Endpoints.USER_PROFILE)
    public String getUserTasks(@RequestParam("userId") int userId, Model model){
        PrincipalInformation principalInformation = getPrincipalInformation();

        checkProfileAccess(principalInformation, userId);

        User user = userService.findUserAndHisTasksById(userId);

        model.addAttribute("user", user);
        model.addAttribute("tasks", user.getTasks());

        return "users-tasks-page";
    }

    @GetMapping(Endpoints.TASK_DETAILS)
    public String getTaskDetails(@RequestParam("taskId") int taskId, Model model) {
        PrincipalInformation principalInformation = getPrincipalInformation();

        checkTaskAccess(principalInformation, taskId);

        Task task = taskService.findTaskById(taskId);

        model.addAttribute("task", task);
        model.addAttribute("taskUpdated", task);

        return "task-details";
    }

    @PostMapping(Endpoints.TASK_UPDATE)
    public String updateTask(@Valid @ModelAttribute("taskUpdated") Task taskUpdated){
        Task task = taskService.findTaskById(taskUpdated.getId());

        task.setDeadline(taskUpdated.getDeadline());
        task.setStatus(taskUpdated.getStatus());

        taskService.save(task);
        return "redirect:" + Endpoints.TASK_DETAILS + "?taskId=" + task.getId();
    }

    private PrincipalInformation getPrincipalInformation(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object loggedUser = authentication.getPrincipal();

        String email = null;
        PrincipalInformation principalInformation = null;

        if (loggedUser instanceof UserDetails) {
            email = ((PrincipalInformation)loggedUser).getUsername();
        } else {
            email = loggedUser.toString();
        }

        principalInformation = (PrincipalInformation) userService.loadUserByUsername(email);

        return principalInformation;
    }

    @GetMapping(Endpoints.ACCESS_DENIED)
    public String getAccessDeniedPage(){
        return "access-denied";
    }

    @GetMapping(Endpoints.USER_DELETE)
    public String deleteUser(@RequestParam("userId") int userId){
        User user = userService.findUserById(userId);

        //check if we aredeleting company owner
        if(user.getRoles().contains(roleService.findRoleByName("ROLE_ADMIN"))){
            throw new RuntimeException("Can not delete company owner");
        }

        userService.deleteUserById(userId);

        return "redirect:" + Endpoints.EMPLOYEES_LIST;
    }

    @GetMapping(Endpoints.TASK_DELETE)
    public String deleteTask(@RequestParam("taskId") int taskId){
        PrincipalInformation principalInformation = getPrincipalInformation();

        checkTaskAccess(principalInformation, taskId);

        //find user id to redirect to users tasks
        Long userId = taskService.findTaskById(taskId).getUser().getId();

        taskService.deleteTaskById(taskId);

        return "redirect:" + Endpoints.USER_PROFILE + "?userId=" + userId;
    }


    private void checkTaskAccess(PrincipalInformation principalInformation, int taskId) throws AccessDeniedException{
        //check if task user is trying to access is from the users company
        boolean isTaskFromTheSameCompany = taskService.findTaskByCompanyId(principalInformation.getCompany().getId())
                .stream()
                .map(task -> task.getId())
                .anyMatch(id -> id.equals(taskId));

        if(!isTaskFromTheSameCompany){
            throw new AccessDeniedException("Access denied");
        }

        //check if task is users task
        boolean isTaskUsersTask = taskService.findTaskByUserId(principalInformation.getId().intValue())
                .stream()
                .map(task -> task.getId())
                .anyMatch(id -> id.equals(taskId));

        boolean isUserManager =
                principalInformation.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"));

        //check if user is a manager -> manager can access all tasks among company
        //if user is not manager -> he can only access his own tasks
        if(!isUserManager && !isTaskUsersTask){
            throw new AccessDeniedException("Access denied");
        }
    }

    private void checkProfileAccess(PrincipalInformation principalInformation, int userId) throws AccessDeniedException{
        //check if user is trying to access his own profile
        boolean isProfileUsersProfile = (userId == principalInformation.getId());

        if(isProfileUsersProfile){
            return;
        }

        //check if accessed userId is from the same company our logged user is
        boolean isProfileFromTheSameCompany = userService.findUserByCompanyId(principalInformation.getCompany().getId())
                .stream()
                .map(user -> user.getId())
                .anyMatch(id -> id.equals(userId));

        if(!isProfileFromTheSameCompany) {
            throw new AccessDeniedException("Access denied");
        }

        //if we are at that point, it means that user wants to access not his profile, but somebodys from
        //the same company, only manager  can access profiles of other employees
        boolean isUserManager =
                principalInformation.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"));

        if(!isUserManager){
            throw new AccessDeniedException("Access denied");
        }
    }


    @GetMapping(Endpoints.MAIN_PAGE)
    public String getTmpMain(Model model){
        PrincipalInformation principalInformation = getPrincipalInformation();

        List<Task> todaysTasks = taskService.findTaskByUserIdAndDeadline(principalInformation.getId().intValue(),
                Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        model.addAttribute("tasks", todaysTasks);

        return "main-page";
    }

    @GetMapping(Endpoints.MY_PROFILE)
    public String redirectToUsersTasks(){
        PrincipalInformation principalInformation = getPrincipalInformation();

        return "redirect:" + Endpoints.USER_PROFILE +"?userId=" + principalInformation.getId();
    }
}
