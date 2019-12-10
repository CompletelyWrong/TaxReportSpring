package com.project.reportsystem.controller;

import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.Role;
import com.project.reportsystem.exception.NotEqualsPasswordException;
import com.project.reportsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Objects;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Controller
@RequestMapping("/")
public class LoginAndRegisterController {
    private final UserService userService;

    @GetMapping("")
    public String mainPage() {
        String viewName = isAuthenticated();
        return viewName == null ? "index" : viewName;
    }

    @GetMapping("/login")
    public String loginForm() {
        String viewName = isAuthenticated();
        return viewName == null ? "login" : viewName;
    }

    @GetMapping("/register")
    public ModelAndView registerForm() {
        String viewName = isAuthenticated();
        ModelAndView modelAndView = new ModelAndView(viewName == null ? "register" : viewName);
        modelAndView.addObject("user", new User());

        return modelAndView;
    }

    @GetMapping("/signOut")
    public String signOut(HttpSession session) {
        session.invalidate();

        return "redirect:/";
    }

    @PostMapping("/signUp")
    public String signUp(@Valid User user,
                         BindingResult result,
                         @RequestParam("repeatedPassword") String repeatedPassword) {
        if (result.hasErrors()) {
            return "register";
        }

        if (!Objects.equals(user.getPassword(), repeatedPassword)) {
            throw new NotEqualsPasswordException("Your password was not equals");
        }

        userService.register(user);

        return "redirect:/";
    }

    private String isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            if (roles.contains(Role.INSPECTOR)) {
                return "redirect:/inspector/";
            }
            if (roles.contains(Role.ADMIN)) {
                return "redirect:/admin/";
            }
            if (roles.contains(Role.INDIVIDUAL_TAXPAYER) || roles.contains(Role.LEGAL_TAXPAYER)) {
                return "redirect:/user/";
            }
        }
        return null;
    }
}
