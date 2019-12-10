package com.project.reportsystem.controller;

import com.project.reportsystem.domain.*;
import com.project.reportsystem.entity.ReportStatus;
import com.project.reportsystem.exception.NotEqualsPasswordException;
import com.project.reportsystem.exception.ReportFileException;
import com.project.reportsystem.service.*;
import com.project.reportsystem.util.JsonHelper;
import com.project.reportsystem.util.XmlHelper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Controller()
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final RequestService requestService;
    private final ReportService reportService;
    private final ActionService actionService;
    private final InspectorService inspectorService;
    private final JsonHelper jsonHelper;
    private final XmlHelper xmlHelper;

    @GetMapping("")
    public String mainPage(HttpSession session) {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        session.setAttribute("user", user);
        return "redirect:/user/reports";
    }

    @GetMapping("/profile")
    public String showProfile() {
        return "u-profile";
    }

    @PostMapping("/confirm-profile")
    public ModelAndView confirmUpdateProfile(@Valid User user, BindingResult result, HttpSession session,
                                             @RequestParam("repeatedPassword") String repeatedPassword) {
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName("u-update");
            User currentUser = getFromSession(session);
            modelAndView.addObject("currentUser", currentUser);
            return modelAndView;
        }

        if (!Objects.equals(user.getPassword(), repeatedPassword)) {
            throw new NotEqualsPasswordException("Your password was not equals");
        }

        user.setInspector(inspectorService.findByUserId(user.getId()));
        userService.updateInfo(user);
        session.setAttribute("user", user);
        modelAndView.setViewName("redirect:/user/profile");

        return modelAndView;
    }

    @GetMapping("/update-profile")
    public ModelAndView updateProfile(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("u-update");
        User user = getFromSession(session);
        modelAndView.addObject("currentUser", user);

        return modelAndView;
    }

    @GetMapping("/create-request")
    public ModelAndView createRequest() {
        ModelAndView modelAndView = new ModelAndView("create-request");
        modelAndView.addObject("request", new Request());

        return modelAndView;
    }

    @GetMapping("/inspector")
    public ModelAndView showInspector(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("show-inspector");
        User user = getFromSession(session);
        Inspector inspector = inspectorService.findByUserId(user.getId());
        modelAndView.addObject("inspector", inspector);

        return modelAndView;
    }

    @PostMapping("/confirm-request")
    public String confirmCreateRequest(@Valid Request request,
                                       BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "create-request";
        }

        User user = getFromSession(session);
        request.setUser(user);
        requestService.createRequest(request);

        return "redirect:/user/reports";
    }

    @PostMapping("/confirm-report")
    public String confirmCreateReport(@Valid ReportStructure reportStructure,
                                      BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "create-report";
        }

        createReport(session, reportStructure);

        return "redirect:/user/reports";
    }

    @PostMapping("/confirm-report-file")
    public String confirmCreateReportByFile(@RequestParam("file") MultipartFile file, HttpSession session) {
        validate(file);

        try {
            switch (file.getContentType()) {
                case "text/json":
                case "application/json": {
                    String fileUrl = jsonHelper.createJsonFileByFileContent(new String(file.getBytes()));
                    createReport(session, fileUrl);
                    break;
                }
                case "application/xml":
                case "text/xml": {
                    String fileUrl = xmlHelper.createXmlFileByFileContent(new String(file.getBytes()));
                    createReport(session, fileUrl);
                    break;
                }
                default: {
                    throw new ReportFileException("Your file has wrong structure");
                }
            }
        } catch (IOException e) {
            throw new ReportFileException("Your file was corrupted");
        }

        return "redirect:/user/reports";
    }

    @PostMapping("/confirm-update-report-by-file/{id}")
    public String confirmUpdateReportByFile(@PathVariable Long id,
                                            @RequestParam("file") MultipartFile file) {
        validate(file);
        Report report = reportService.findById(id);

        try {
            File transferFile = new File(report.getFileLink());
            switch (file.getContentType()) {
                case "text/json":
                case "application/json": {
                    jsonHelper.updateJsonFileByFileContent(transferFile, new String(file.getBytes()));
                    break;
                }
                case "application/xml":
                case "text/xml": {
                    xmlHelper.updateXmlFileByFileContent(transferFile, new String(file.getBytes()));
                    break;
                }
                default: {
                    throw new ReportFileException("Your file has wrong structure");
                }
            }
        } catch (IOException e) {
            throw new ReportFileException("Your file has wrong structure");
        }


        updateReport(report);

        return "redirect:/user/reports";
    }

    @GetMapping("/create-report")
    public ModelAndView createReport() {
        ModelAndView modelAndView = new ModelAndView("create-report");
        modelAndView.addObject("reportStructure", new ReportStructure());

        return modelAndView;
    }

    @GetMapping("/create-report-file")
    public String createReportByFile() {
        return "create-report-f";
    }

    @GetMapping("/reports")
    public ModelAndView showReports(HttpSession session, @PageableDefault(sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("report-list");
        User user = getFromSession(session);
        Page<Report> reports = reportService.findAllByUserId(user.getId(), pageable);
        modelAndView.addObject("reports", reports);

        return modelAndView;
    }

    @GetMapping("/report/{id}")
    public ModelAndView showReportById(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("view_report");
        setReportInView(id, modelAndView);

        return modelAndView;
    }

    @GetMapping("/update-report/{id}")
    public ModelAndView showUpdateReportById(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("update-report");
        setReportInView(id, modelAndView);

        return modelAndView;
    }

    @GetMapping("/update-report-by-file/{id}")
    public ModelAndView showUpdateReportByFile(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("update-report-f");
        Report report = reportService.findById(id);
        modelAndView.addObject("reportId", report.getId());

        return modelAndView;
    }


    @PostMapping("/confirm-update-report/{id}")
    public String confirmUpdateReportById(@PathVariable Long id,
                                          @Valid ReportStructure reportStructure,
                                          BindingResult result) {
        if (result.hasErrors()) {
            return "update-report";
        }

        Report report = reportService.findById(id);
        jsonHelper.updateJsonFileByForm(new File(report.getFileLink()), reportStructure);
        updateReport(report);
        return "redirect:/user/report/" + id;
    }

    @GetMapping("/report-history/{id}")
    public ModelAndView showReportHistoryById(@PathVariable Long id, @PageableDefault(sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("report-history");
        Page<Action> actions = actionService.findAllForReportById(id, pageable);
        modelAndView.addObject("actions", actions);
        modelAndView.addObject("reportId", id);

        return modelAndView;
    }

    private void createReport(HttpSession session, ReportStructure reportStructure) {
        User user = getFromSession(session);
        String filePath = jsonHelper.createJsonFileByForm(reportStructure);
        Report report = Report.builder()
                .user(user)
                .status(ReportStatus.NEW)
                .creationDate(LocalDateTime.now())
                .fileLink(filePath)
                .build();

        reportService.addReportToUser(report, user);
    }

    private void createReport(HttpSession session, String fileUrl) {
        User user = getFromSession(session);
        Report report = Report.builder()
                .user(user)
                .status(ReportStatus.NEW)
                .creationDate(LocalDateTime.now())
                .fileLink(fileUrl)
                .build();
        reportService.addReportToUser(report, user);
    }

    private void updateReport(Report report) {
        Report updatedReport = Report.builder()
                .user(report.getUser())
                .status(ReportStatus.UPDATED)
                .creationDate(report.getCreationDate())
                .fileLink(report.getFileLink())
                .id(report.getId())
                .build();
        reportService.updateReport(updatedReport);
    }

    private void setReportInView(Long reportId, ModelAndView modelAndView) {
        Report report = reportService.findById(reportId);
        ReportStructure reportStructure = jsonHelper.readFromJson(report.getFileLink());
        modelAndView.addObject("reportStructure", reportStructure);
        modelAndView.addObject("reportId", report.getId());
    }

    private User getFromSession(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    private void validate(MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new ReportFileException("Your file was corrupted");
        }

        if (Objects.isNull(file.getContentType())) {
            throw new ReportFileException("Your file was corrupted");
        }
    }
}
