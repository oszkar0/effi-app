package com.effi.EffiApp.controllers;

import com.effi.EffiApp.endpoints.Endpoints;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(Endpoints.LOGIN_PREFIX)
public class LoginController {
    @GetMapping(Endpoints.LOGGING)
    public String getLoginPage(){
        return "login";
    }
}
