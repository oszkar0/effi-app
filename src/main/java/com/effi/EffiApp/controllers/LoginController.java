package com.effi.EffiApp.controllers;

import com.effi.EffiApp.endpoints.Endpoints;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping(Endpoints.LOGIN)
    public String getLoginPage(){
        return "login";
    }
}
