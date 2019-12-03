package com.project.reportsystem.controller;

import com.google.gson.Gson;
import com.project.reportsystem.domain.*;
import com.project.reportsystem.entity.Role;
import com.project.reportsystem.exception.ReportFileException;
import com.project.reportsystem.service.ActionService;
import com.project.reportsystem.service.InspectorService;
import com.project.reportsystem.service.ReportService;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDateTime;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Controller()
@RequestMapping("/inspector")
public class InspectorController {
    private final UserService userService;
    private final InspectorService inspectorService;
    private final ReportService reportService;
    private final ActionService actionService;

    @GetMapping("")
    public String mainPage() {
        return "i-profile";
    }

    @GetMapping("/update_profile")
    public ModelAndView showUpdateProfile(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("i-update");
        Inspector inspector = (Inspector) session.getAttribute("user");
        System.out.println(inspector);
        modelAndView.addObject("current_inspector", inspector);
        return modelAndView;
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "i-profile";
    }

    @PostMapping("/update_profile")
    public String confirmUpdateProfile(@Valid Inspector inspector, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "i-update";
        }
        System.out.println(inspector);
        inspectorService.updateInfo(inspector);
        session.setAttribute("user", inspector);
        return "redirect:/inspector/profile";
    }

    @GetMapping("/inspector/users")
    public String showAllUsers(HttpSession session, Model model,
                               @PageableDefault(sort = {"id"},
                                       direction = Sort.Direction.DESC) Pageable pageable) {
        Inspector user = (Inspector) session.getAttribute("user");
        Page<User> allByInspectorId = userService.findAllByInspectorId(user.getId(), pageable);
        model.addAttribute("users", allByInspectorId);
        return "inspector/current-user-list";
    }

    @GetMapping("/inspector/user_reports{id}")
    public String showUserReports(@PathVariable Long id, HttpSession session, Model model,
                                  @PageableDefault(sort = {"id"},
                                          direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Report> allForUserById = reportService.findAllByUserId(id, pageable);
        model.addAttribute("reports", allForUserById);
        return "inspector/inspector_report-list";
    }

    @GetMapping("/inspector/read_report{id}")
    public String showReportById(@PathVariable Long id, HttpSession session, Model model) {
        Report report = reportService.findById(id);
        ReportStructure reportStructure = readFromJson(report);
        model.addAttribute("report", reportStructure);
        model.addAttribute("report_id", report.getId());

        return "inspector/read-report";
    }

    @PostMapping("/inspector/read_report{id}")
    public String confirmReportAssessment(@PathVariable Long id, HttpSession session,
                                          @ModelAttribute("action")
                                          @Valid Action action,
                                          BindingResult result) {
        if (result.hasErrors()) {
            return "user/create_request";
        }
        Report report = reportService.findById(id);
        Inspector inspector = (Inspector) session.getAttribute("user");
        action.setInspector(inspector);
        action.setDateTime(LocalDateTime.now());
        actionService.addActionToReport(action, report);
        return "inspector/read-report";
    }

    private static ReportStructure readFromJson(Report report) {
        ReportStructure structure;
        try (Reader reader = new FileReader(report.getFileLink())) {
            structure = new Gson().fromJson(reader, ReportStructure.class);
        } catch (IOException e) {
            throw new ReportFileException("Ur file was corrupted");
        }
        return structure;
    }
}
