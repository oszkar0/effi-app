package com.effi.EffiApp.exceptionhandling;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = Logger.getLogger(getClass().getName());
    public static final String DEFAULT_ERROR_VIEW = "error";

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
