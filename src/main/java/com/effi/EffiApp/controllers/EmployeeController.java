package com.effi.EffiApp.controllers;

import com.effi.EffiApp.endpoints.Endpoints;
import com.effi.EffiApp.entity.User;
import com.effi.EffiApp.registration.employee.EmployeeRegistrationObject;
import com.effi.EffiApp.security.PrincipalInformation;
import com.effi.EffiApp.service.CompanyService;
import com.effi.EffiApp.service.RoleService;
import com.effi.EffiApp.service.UserService;
import com.effi.EffiApp.utils.common.PrincipalGetter;
import com.effi.EffiApp.utils.passwords.PasswordGenerator;
import com.effi.EffiApp.utils.pdfs.PdfGeneration;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.util.List;

@Controller
@RequestMapping(Endpoints.EMPLOYEE_PREFIX)
public class EmployeeController {
    private UserService userService;
    private RoleService roleService;
    private CompanyService companyService;

    @Autowired
    public EmployeeController(UserService userService, RoleService roleService, CompanyService companyService) {
        this.userService = userService;
        this.roleService = roleService;
        this.companyService = companyService;
    }

    @GetMapping(Endpoints.USER_DELETE)
    public String deleteUser(@RequestParam("userId") int userId){
        User user = userService.findUserById(userId);

        //check if we are deleting company owner
        if(user.getRoles().contains(roleService.findRoleByName("ROLE_ADMIN"))){
            throw new RuntimeException("Can not delete company owner");
        }

        userService.deleteUserById(userId);

        return "redirect:" + Endpoints.EMPLOYEE_EMPLOYEES_LIST;
    }

    @GetMapping(Endpoints.EMPLOYEES_LIST)
    public String getMainPage(Model model){
        //get logged user
        PrincipalInformation principalInformation = PrincipalGetter.getPrincipalInformation(userService);

        //get all users from logged users company
        List<User> companyUsers = companyService.findCompanyById(principalInformation.getCompany().getId()).getUsers();

        model.addAttribute("users", companyUsers);

        return "employees-list-page";
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
            return "redirect:" + Endpoints.TASKS_AND_PROFILES_MAIN_PAGE;
        }

        //generate and set random password which can be changed by employee later
        String password = PasswordGenerator.generatePassword(10);
        employeeRegistrationObject.setPassword(password);

        //get the logged user
        PrincipalInformation principalInformation = PrincipalGetter.getPrincipalInformation(userService);

        //set new employee's company to admins(owners) company and add user to company
        employeeRegistrationObject.setCompany(principalInformation.getCompany());

        userService.save(employeeRegistrationObject);

        redirectAttributes.addFlashAttribute("employee", employeeRegistrationObject);
        return "redirect:" + Endpoints.EMPLOYEE_NEW_EMPLOYEE_INFO;
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
}
