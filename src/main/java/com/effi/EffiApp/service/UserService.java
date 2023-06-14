package com.effi.EffiApp.service;

import com.effi.EffiApp.entity.User;
import com.effi.EffiApp.registration.RegistrationObject;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findUserByEmail(String email);
    boolean userExists(String email);
    void save(RegistrationObject registrationObject);
}
