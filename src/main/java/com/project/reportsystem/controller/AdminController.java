package com.project.reportsystem.controller;


import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.Request;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.entity.Role;
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

    @GetMapping("/create_inspector")
    public ModelAndView createInspector() {
        ModelAndView modelAndView = new ModelAndView("create-inspector");
        modelAndView.addObject("inspector", Inspector.builder().role(Role.INSPECTOR).build());
        return modelAndView;
    }

    @PostMapping("/confirm_create")
    public String confirmCreatingInspector(@Valid Inspector inspector,
                                           BindingResult result) {
        if (result.hasErrors()) {
            return "create-inspector";
        }
        inspectorService.createInspector(inspector);
        return "redirect:/admin/create_inspector";
    }

    @GetMapping("/apply_request/{id}/user/{user_id}")
    public String applyRequest(@PathVariable Long id,
                               @PathVariable("user_id") Long userId) {
        User user = userService.findById(userId);
        userService.changeInspectorForUser(user);
        requestService.deleteRequestById(id);
        return "redirect:/admin/requests";
    }

    @GetMapping("/reject_request/{id}")
    public String rejectRequest(@PathVariable Long id) {
        requestService.deleteRequestById(id);
        return "redirect:/admin/requests";
    }

    @GetMapping("/users")
    public String showUsers(Model model,
                            @PageableDefault(sort = {"id"},
                                    direction = Sort.Direction.DESC) Pageable pageable) {
        Page<User> all = userService.findAll(pageable);
        model.addAttribute("users", all);
        return "users-list";
    }

    @GetMapping("/requests")
    public String showRequest(Model model,
                              @PageableDefault(sort = {"id"},
                                      direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Request> all = requestService.findAll(pageable);
        model.addAttribute("requests", all);
        return "request-list";
    }

    @GetMapping("/inspectors")
    public String showInspectors(Model model,
                                 @PageableDefault(sort = {"id"},
                                         direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Inspector> all = inspectorService.findAll(pageable);
        model.addAttribute("inspectors", all);
        return "inspectors-list";
    }
}
