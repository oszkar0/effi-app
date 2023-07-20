package com.effi.EffiApp.utils.common;

import com.effi.EffiApp.security.PrincipalInformation;
import com.effi.EffiApp.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class PrincipalGetter {

    public static PrincipalInformation getPrincipalInformation(UserService userService){
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
