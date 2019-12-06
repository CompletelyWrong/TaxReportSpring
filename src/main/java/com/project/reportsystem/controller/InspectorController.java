package com.project.reportsystem.controller;

import com.project.reportsystem.domain.*;
import com.project.reportsystem.entity.ActionType;
import com.project.reportsystem.entity.ReportStatus;
import com.project.reportsystem.exception.NotEqualsPasswordException;
import com.project.reportsystem.service.ActionService;
import com.project.reportsystem.service.InspectorService;
import com.project.reportsystem.service.ReportService;
import com.project.reportsystem.service.UserService;
import com.project.reportsystem.util.JsonHelper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Controller()
@RequestMapping("/inspector")
public class InspectorController {
    private final UserService userService;
    private final InspectorService inspectorService;
    private final ReportService reportService;
    private final ActionService actionService;
    private final JsonHelper jsonHelper;

    @GetMapping("")
    public String mainPage() {
        return "redirect:/inspector/users";
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "i-profile";
    }

    @GetMapping("/update-profile")
    public ModelAndView showUpdateProfile(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("i-update");
        Inspector inspector = getFormSession(session);
        modelAndView.addObject("currentInspector", inspector);

        return modelAndView;
    }

    @PostMapping("/update-profile")
    public String confirmUpdateProfile(@Valid Inspector inspector, BindingResult result, HttpSession session,
                                       @RequestParam("password") String password,
                                       @RequestParam("repeatedPassword") String repeatedPassword) {
        if (result.hasErrors()) {
            return "i-update";
        }

        if (!Objects.equals(password, repeatedPassword)) {
            throw new NotEqualsPasswordException("Your password was not equals");
        }

        inspectorService.updateInfo(inspector);
        session.setAttribute("user", inspector);

        return "redirect:/inspector/profile";
    }

    @GetMapping("/users")
    public ModelAndView showAllUsers(HttpSession session, @PageableDefault(sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("current-user-list");
        Inspector user = getFormSession(session);
        Page<User> users = userService.findAllByInspectorId(user.getId(), pageable);
        modelAndView.addObject("users", users);

        return modelAndView;
    }

    @GetMapping("/user-reports/{id}")
    public ModelAndView showUserReports(@PathVariable Long id, @PageableDefault(sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("inspector_report-list");
        Page<Report> reports = reportService.findAllByUserId(id, pageable);
        modelAndView.addObject("reports", reports);
        modelAndView.addObject("userId", id);

        return modelAndView;
    }

    @GetMapping("/read-report/{id}")
    public ModelAndView showReportById(@PathVariable("id") Long reportId) {
        ModelAndView modelAndView = new ModelAndView("read-report");
        Report report = reportService.findById(reportId);
        ReportStructure reportStructure = jsonHelper.readFromJson(report.getFileLink());
        modelAndView.addObject("action", new Action());
        modelAndView.addObject("report", reportStructure);
        modelAndView.addObject("reportId", report.getId());

        return modelAndView;
    }

    @PostMapping("/confirm-report/{reportId}")
    public String confirmReportAssessment(@PathVariable Long reportId, HttpSession session,
                                          @Valid Action action,
                                          BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/inspector/read-report/" + reportId;
        }

        Report report = reportService.findById(reportId);
        Inspector inspector = getFormSession(session);
        action.setInspector(inspector);
        action.setDateTime(LocalDateTime.now());
        actionService.addActionToReport(action, report);
        report.setStatus(getReportStatus(action.getActionType(), report));
        reportService.updateReport(report);

        return "redirect:/inspector/users";
    }

    private ReportStatus getReportStatus(ActionType actionType, Report report) {
        switch (actionType) {
            case REJECT:
            case REQUEST_CHANGES:
                return ReportStatus.REJECTED;
            case ACCEPT:
                return ReportStatus.ACCEPTED;
            default:
                return report.getStatus();
        }
    }

    private Inspector getFormSession(HttpSession session) {
        return (Inspector) session.getAttribute("user");
    }
}
