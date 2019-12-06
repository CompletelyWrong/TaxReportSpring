package com.project.reportsystem.controller;

import com.project.reportsystem.domain.User;
import com.project.reportsystem.exception.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({SQLException.class,
            NullPointerException.class,
            IllegalArgumentException.class,
            EntityNotFoundException.class,
            NotEqualsPasswordException.class,
            InvalidAddEntityException.class,
            ReportFileException.class,
            InspectorNotFoundException.class,
            Throwable.class})
    public String handleException() {
        return "error";
    }

    @ExceptionHandler({AlreadyExistUserException.class})
    public ModelAndView handleRegisteredException() {
        ModelAndView modelAndView = new ModelAndView("redirect:/register");
        modelAndView.addObject("registerError", true);
        modelAndView.addObject("user", new User());

        return modelAndView;
    }

}
