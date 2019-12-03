package com.project.reportsystem.controller;

import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.Role;
import com.project.reportsystem.service.InspectorService;
import com.project.reportsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Controller
public class LoginAndRegisterController {
    private final UserService userService;
    private final InspectorService inspectorService;

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @GetMapping("/signOut")
    public String signOut(HttpSession session) {
        session.invalidate();

        return "index";
    }

    @PostMapping("/signIn")
    public String signIn(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Role role;
        if (Objects.isNull(request.getParameter("worker"))) {
            User user = userService.login(email, password);
            role = user.getRole();
            request.getSession().setAttribute("user", user);
        } else {
            Inspector inspector = inspectorService.login(email, password);
            role = inspector.getRole();
            request.getSession().setAttribute("user", inspector);
        }

        switch (role) {
            case ADMIN:
                return "redirect:/admin";
            case INSPECTOR:
                return "redirect:/inspector";
            case INDIVIDUAL_TAXPAYER:
            case LEGAL_TAXPAYER:
                return "redirect:/user";
            default:
                return "index";
        }
    }

    @PostMapping("/signUp")
    public String signUp(HttpServletRequest request) {
        String surname = request.getParameter("fullName");
        String name = request.getParameter("name1");
        String patronymic = request.getParameter("patron");
        String email = request.getParameter("email");
        String password = request.getParameter("password1");
        String repeatedPassword = request.getParameter("password2");
        Integer innCode = Integer.parseInt(request.getParameter("number"));
        Role role = Role.valueOf(request.getParameter("role_type"));

        if (!Objects.equals(password, repeatedPassword)) {
            return "register";
        }

        User user = User.builder()
                .name(name)
                .surname(surname)
                .patronymic(patronymic)
                .email(email)
                .password(password)
                .role(role)
                .identificationCode(innCode)
                .build();

        userService.register(user);

        return "index";
    }
}
