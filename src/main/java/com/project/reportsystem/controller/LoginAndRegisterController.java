package com.project.reportsystem.controller;

import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.Role;
import com.project.reportsystem.exception.NotEqualsPasswordException;
import com.project.reportsystem.service.InspectorService;
import com.project.reportsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Controller
@RequestMapping("/")
public class LoginAndRegisterController {
    private final UserService userService;
    private final InspectorService inspectorService;

    @GetMapping("")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/register")
    public ModelAndView registerForm() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("user", new User());

        return modelAndView;
    }

    @GetMapping("/signOut")
    public String signOut(HttpSession session) {
        session.invalidate();

        return "redirect:/";
    }

    @PostMapping("/login")
    public String signIn(@RequestParam("email") String email,
                         @RequestParam("password") String password,
                         @RequestParam(value = "worker", required = false) String isWorker,
                         HttpSession session) {
        Role role;

        if (Objects.isNull(isWorker)) {
            User user = userService.login(email, password);
            role = user.getRole();
            session.setAttribute("user", user);
        } else {
            Inspector inspector = inspectorService.login(email, password);
            role = inspector.getRole();
            session.setAttribute("user", inspector);
        }
        switch (role) {
            case ADMIN:
                return "redirect:/admin/";
            case INSPECTOR:
                return "redirect:/inspector/";
            case INDIVIDUAL_TAXPAYER:
            case LEGAL_TAXPAYER:
                return "redirect:/user/";
            default:
                return "redirect:/";
        }
    }

    @PostMapping("/signUp")
    public String signUp(@Valid User user,
                         BindingResult result,
                         @RequestParam("password") String password,
                         @RequestParam("repeatedPassword") String repeatedPassword) {
        if (result.hasErrors()) {
            return "register";
        }

        if (!Objects.equals(password, repeatedPassword)) {
            throw new NotEqualsPasswordException("Your password was not equals");
        }

        userService.register(user);

        return "redirect:/";
    }
}
