package com.effi.EffiApp.service;

import com.effi.EffiApp.entity.User;
import com.effi.EffiApp.registration.RegistartionObject;
import jakarta.servlet.Registration;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findUserByEmail(String email);
    boolean userExists(String email);
    void save(RegistartionObject registrationObject);
}
