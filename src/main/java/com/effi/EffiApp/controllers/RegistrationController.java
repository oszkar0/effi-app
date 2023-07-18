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
@RequestMapping(Endpoints.REGISTRATION)
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
            return "redirect:/login";
        }

        userService.save(registrationObject);

        return "redirect:" + Endpoints.LOGIN;
    }

    @GetMapping(Endpoints.NEW_EMPLOYEE_FORM)
    public String getEmployeeRegistrationForm(Model model){
        model.addAttribute("employeeRegistrationObject", new EmployeeRegistrationObject());

        return "employee-form";
    }

    @PostMapping(Endpoints.NEW_EMPLOYEE_PROCESSING)
    public String processEmployeeRegistration(
            @Valid @ModelAttribute("registrationObject") EmployeeRegistrationObject employeeRegistrationObject,
            BindingResult bindingResult, RedirectAttributes redirectAttributes){

        //check if errors occurred, if occurred return filled form with error messages
        if(bindingResult.hasErrors()){
            return "employee-form";
        }

        //check if user is already in db
        if(userService.userExists(employeeRegistrationObject.getEmail())){
            return "redirect:" + Endpoints.MAIN_PAGE;
        }

        //generate and set random password which can be changed by employee later
        String password = PasswordGenerator.generatePassword(10);
        employeeRegistrationObject.setPassword(password);

        //get the logged user
        PrincipalInformation principalInformation = getPrincipalInformation();

        //set new employee's company to admins(owners) company and add user to company
        employeeRegistrationObject.setCompany(principalInformation.getCompany());

        userService.save(employeeRegistrationObject);

        redirectAttributes.addFlashAttribute("employee", employeeRegistrationObject);
        return "redirect:" + Endpoints.REGISTRATION_NEW_EMPLOYEE_INFO;
    }

    @GetMapping(Endpoints.NEW_EMPLOYEE_INFO)
    public String getNewEmployeeInfo(Model model, @ModelAttribute("employee") EmployeeRegistrationObject employee){
        model.addAttribute("employee", employee);

        return "new-employee-info";
    }

    @PostMapping(value = Endpoints.NEW_EMPLOYEE_PDF,  produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getNewEmployeeInfoPdf(
            @ModelAttribute("employee") EmployeeRegistrationObject employee){
        ByteArrayInputStream bis = PdfGeneration.generateNewEmployeeAccountInfo(employee);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=new_employee.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping(Endpoints.NEW_TASK_FORM)
    public String showNewTaskForm(@RequestParam("userId") int userId, Model model){
        TaskRegistrationObject task = new TaskRegistrationObject();
        task.setUserId(userId);

        model.addAttribute("task", task);

        return "task-form";
    }

    @PostMapping(Endpoints.NEW_TASK_PROCESSING)
    public String processNewTask(
            @Valid @ModelAttribute("task") TaskRegistrationObject task,
            BindingResult bindingResult){

        //check if errors occurred, if occurred return filled form with error messages
        if(bindingResult.hasErrors()){
            return "task-form";
        }

        //retrieve the user
        User user = userService.findUserAndHisTasksById(task.getUserId());

        //create new task
        Task newTask = new Task();
        newTask.setTitle(task.getTitle());
        newTask.setDescription(task.getDescription());
        newTask.setDeadline(task.getDeadline());
        newTask.setStatus(Task.TaskStatus.NOT_DONE);

        //create connection user-task
        newTask.setUser(user);
        user.addTask(newTask);

        //save
        userService.save(user);

        return "redirect:" + Endpoints.USER_PROFILE + "?userId=" + task.getUserId();
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
}
