package com.effi.EffiApp.exceptionhandling;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = Logger.getLogger(getClass().getName());
    public static final String DEFAULT_ERROR_VIEW = "error";
    public static final String ACCESS_DENIED_VIEW = "access-denied";
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView accessDeniedExceptionHandler(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName(ACCESS_DENIED_VIEW);
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultExceptionHandler(HttpServletRequest req,Exception e) throws Exception{
        //we might log exception here
        logger.info("Exception occurred: " + e.getMessage() + "\n");

        //if exception is annotated with response status then rethrow it and let framework handle it
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
            throw e;
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName(DEFAULT_ERROR_VIEW);

        return mav;
    }
}
