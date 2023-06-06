package com.effi.EffiApp.service;

import com.effi.EffiApp.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findUserByEmail(String email);
}
