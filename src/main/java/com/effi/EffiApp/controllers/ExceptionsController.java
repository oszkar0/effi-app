package com.effi.EffiApp.controllers;

import com.effi.EffiApp.endpoints.Endpoints;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(Endpoints.EXCEPTION_PREFIX)
public class ExceptionsController {
    @GetMapping(Endpoints.ACCESS_DENIED)
    public String getAccessDeniedPage(){
        return "access-denied";
    }
}
