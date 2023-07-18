package com.effi.EffiApp.service;

import com.effi.EffiApp.entity.User;
import com.effi.EffiApp.registration.employee.EmployeeRegistrationObject;
import com.effi.EffiApp.registration.owner.OwnerRegistrationObject;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findUserByEmail(String email);
    boolean userExists(String email);
    void save(OwnerRegistrationObject registrationObject);
    void save(EmployeeRegistrationObject employeeRegistrationObject);
    User findUserAndHisTasksById(int id);
    void save(User user);
    void deleteUserById(int id);
    User findUserById(int id);
    List<User> findUserByCompanyId(int companyId);
}
