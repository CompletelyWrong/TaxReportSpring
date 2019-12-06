package com.project.reportsystem.controller;


import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.Request;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.Role;
import com.project.reportsystem.exception.NotEqualsPasswordException;
import com.project.reportsystem.service.InspectorService;
import com.project.reportsystem.service.RequestService;
import com.project.reportsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Objects;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RequestService requestService;
    private final InspectorService inspectorService;

    @GetMapping("")
    public String mainPage() {
        return "redirect:/admin/users";
    }

    @GetMapping("/create-inspector")
    public ModelAndView createInspector() {
        ModelAndView modelAndView = new ModelAndView("create-inspector");
        modelAndView.addObject("inspector", Inspector.builder()
                .role(Role.INSPECTOR)
                .build());

        return modelAndView;
    }

    @PostMapping("/confirm-create")
    public String confirmCreatingInspector(@Valid Inspector inspector, BindingResult result,
                                           @RequestParam("password") String password,
                                           @RequestParam("repeatedPassword") String repeatedPassword) {
        if (result.hasErrors()) {
            return "create-inspector";
        }

        if (!Objects.equals(password, repeatedPassword)) {
            throw new NotEqualsPasswordException("Your password was not equals");
        }

        inspectorService.createInspector(inspector);

        return "redirect:/admin/inspectors";
    }

    @GetMapping("/apply-request/{id}/user/{userId}")
    public String applyRequest(@PathVariable Long id, @PathVariable("userId") Long userId) {
        User user = userService.findById(userId);
        userService.changeInspectorForUser(user);
        requestService.deleteRequestById(id);

        return "redirect:/admin/requests";
    }

    @GetMapping("/reject-request/{id}")
    public String rejectRequest(@PathVariable Long id) {
        requestService.deleteRequestById(id);

        return "redirect:/admin/requests";
    }

    @GetMapping("/users")
    public ModelAndView showUsers(Model model,
                                  @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("users-list");
        Page<User> users = userService.findAll(pageable);
        modelAndView.addObject("users", users);

        return modelAndView;
    }

    @GetMapping("/requests")
    public ModelAndView showRequest(Model model,
                                    @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("request-list");
        Page<Request> requests = requestService.findAll(pageable);
        modelAndView.addObject("requests", requests);

        return modelAndView;
    }

    @GetMapping("/inspectors")
    public ModelAndView showInspectors(Model model,
                                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("inspectors-list");
        Page<Inspector> inspectors = inspectorService.findAll(pageable);
        modelAndView.addObject("inspectors", inspectors);

        return modelAndView;
    }
}
