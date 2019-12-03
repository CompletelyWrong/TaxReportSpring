package com.project.reportsystem.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.reportsystem.domain.*;
import com.project.reportsystem.entity.ReportStatus;
import com.project.reportsystem.exception.ReportFileException;
import com.project.reportsystem.service.ActionService;
import com.project.reportsystem.service.ReportService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Controller("/user")
public class UserController {

    private static final String PATH_TO_STORAGE = "C:\\spring-app\\src\\main\\webapp\\WEB-INF\\jsp\\files\\";
    private final UserService userService;
    private final RequestService requestService;
    private final ReportService reportService;
    private final ActionService actionService;

    @GetMapping("/user")
    public String mainPage() {
        return "redirect:/user/reports";
    }

    @GetMapping("/user/profile")
    public String showProfile() {
        return "user/u-profile";
    }

    @PostMapping("user/confirm_profile")
    public String confirmUpdateProfile(@ModelAttribute("user") @Valid User user,
                                       BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "user/u-update";
        }
        userService.updateInfo(user);
        session.setAttribute("user", user);
        return "redirect:/user/update_profile";
    }

    @GetMapping("user/update_profile")
    public String updateProfile() {
        return "user/u-update";
    }

    @GetMapping("user/create_request")
    public String createRequest() {
        return "user/create-request";
    }

    @PostMapping("user/confirm_request")
    public String confirmCreateRequest(@ModelAttribute("request") @Valid Request request,
                                       BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "user/create_request";
        }

        User user = (User) session.getAttribute("user");
        request.setUser(user);
        requestService.createRequest(request);
        return "redirect:/user/create_request";
    }

    @PostMapping("user/confirm_report")
    public String confirmCreateReport(@ModelAttribute("report") @Valid ReportStructure reportStructure,
                                      BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "user/create_request";
        }
        LocalDateTime time = LocalDateTime.now();
        File gsonFile = new File(PATH_TO_STORAGE + time.toLocalDate() +
                "-" + time.toLocalTime().getNano() + ".json");
        createReport(session, gsonFile, reportStructure);
        return "redirect:/user/create_request";
    }

    @PostMapping("user/confirm_report_file")
    public String confirmCreateReportByFile(@RequestParam("file") MultipartFile file, HttpSession session) {
        if (Objects.isNull(file) || !file.isEmpty()) {
            try {
                LocalDateTime time = LocalDateTime.now();
                File transferFile = new File(PATH_TO_STORAGE + time.toLocalDate()
                        + "-" + time.toLocalTime().getNano() + ".json");
                String contentType = file.getContentType();
                switch (contentType) {
                    case "text/json":
                    case "application/json": {
                        forJson(transferFile, new String(file.getBytes()));
                        createReport(session, transferFile);
                        break;
                    }
                    case "application/xml":
                    case "text/xml": {
                        forXMl(transferFile, new String(file.getBytes()));
                        createReport(session, transferFile);
                        break;
                    }
                    default: {
                        throw new ReportFileException("Your file was corrupted");
                    }
                }
                return "redirect:/user/create_request";
            } catch (IOException e) {
                throw new ReportFileException("Your file was corrupted");
            }
        }
        return "redirect:/user/create_request";
    }

    @GetMapping("/user/create_report")
    public String createReport() {
        return "user/create-report";
    }

    @GetMapping("/user/create_report_file")
    public String createReportByFile() {
        return "user/create-report-f";
    }

    @GetMapping("/user/reports")
    public String showReports(HttpSession session, Model model,
                              @PageableDefault(sort = {"id"},
                                      direction = Sort.Direction.DESC) Pageable pageable) {
        User user = (User) session.getAttribute("user");
        Page<Report> all = reportService.findAllByUserId(user.getId(), pageable);
        model.addAttribute("reports", all);
        return "user/report-list";
    }

    @GetMapping("/user/report{id}")
    public String showReportById(@PathVariable Long id, Model model) {
        Report report = reportService.findById(id);
        ReportStructure reportStructure = readFromJson(report);
        model.addAttribute("report", reportStructure);
        model.addAttribute("report_id", report.getId());
        return "user/view_report";
    }

    @GetMapping("/user/update_report{id}")
    public String showUpdateReportById(@PathVariable Long id, Model model) {
        Report report = reportService.findById(id);
        ReportStructure reportStructure = readFromJson(report);
        model.addAttribute("report", reportStructure);
        model.addAttribute("report_id", report.getId());
        return "user/update-report";
    }

    @GetMapping("/user/update_report_by_file{id}")
    public String showUpdateReportByFile(@PathVariable Long id, Model model) {
        Report report = reportService.findById(id);
        model.addAttribute("report_id", report.getId());
        return "user/update-report-f";
    }

    @PostMapping("/user/confirm_update_report{id}")
    public String confirmUpdateReportById(@PathVariable Long id,
                                          @RequestParam("file") MultipartFile file) {
        Report report = reportService.findById(id);
        if (Objects.isNull(file) || !file.isEmpty()) {
            try {
                File transferFile = new File(report.getFileLink());
                String contentType = file.getContentType();
                switch (contentType) {
                    case "text/json":
                    case "application/json": {
                        forJson(transferFile, new String(file.getBytes()));
                        break;
                    }
                    case "application/xml":
                    case "text/xml": {
                        forXMl(transferFile, new String(file.getBytes()));
                        break;
                    }
                    default: {
                        throw new ReportFileException("Your file was corrupted");
                    }
                }
                return "redirect:/user/create_request";
            } catch (IOException e) {
                throw new ReportFileException("Your file was corrupted");
            }
        }
        updateReport(report);
        return "redirect:/user/create_request";
    }

    @PostMapping("/user/confirm_update_report_by_file{id}")
    public String confirmUpdateReportByFile(@PathVariable Long id,
                                            @ModelAttribute("report") @Valid ReportStructure reportStructure,
                                            BindingResult result) {
        if (result.hasErrors()) {
            return "user/create_request";
        }
        Report report = reportService.findById(id);
        writeJsonFile(new File(report.getFileLink()), reportStructure);
        updateReport(report);
        return "redirect:/user/create_request";
    }

    @GetMapping("/user/report_history{id}")
    public String showReportHistoryById(@PathVariable Long id, Model model,
                                        @PageableDefault(sort = {"id"},
                                                direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Action> allForReportById = actionService.findAllForReportById(id, pageable);
        model.addAttribute("actions", allForReportById);
        model.addAttribute("report_id", id);
        return "user/report-history";
    }

    private void writeJsonFile(File file, ReportStructure reportStructure) {
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(reportStructure, writer);
        } catch (IOException e) {
            throw new ReportFileException("Ur file was corrupted");
        }
    }

    private void forXMl(File file, String content) {
        try (Writer writer = new FileWriter(file)) {
            JAXBContext jaxbContext = JAXBContext.newInstance(ReportStructure.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ReportStructure reportStructure = (ReportStructure) jaxbUnmarshaller.unmarshal(new StringReader(content));
            Gson gson = new GsonBuilder().create();
            gson.toJson(reportStructure, writer);
        } catch (JAXBException | IOException e) {
            throw new ReportFileException("Ur file was corrupted");
        }
    }

    private void forJson(File file, String content) {
        Gson gson = new Gson();
        ReportStructure reportStructure = gson.fromJson(content, ReportStructure.class);
        try (Writer writer = new FileWriter(file)) {
            gson = new GsonBuilder().create();
            gson.toJson(reportStructure, writer);
        } catch (IOException e) {
            throw new ReportFileException("Ur file was corrupted");
        }
    }

    private void createReport(HttpSession session, File file, ReportStructure reportStructure) {
        User user = (User) session.getAttribute("user");
        Report report = Report.builder()
                .user(user)
                .status(ReportStatus.NEW)
                .creationDate(LocalDateTime.now())
                .fileLink(file.getAbsolutePath())
                .build();
        writeJsonFile(file, reportStructure);
        reportService.addReportToUser(report, user);
    }

    private void createReport(HttpSession session, File file) {
        User user = (User) session.getAttribute("user");
        Report report = Report.builder()
                .user(user)
                .status(ReportStatus.NEW)
                .creationDate(LocalDateTime.now())
                .fileLink(file.getAbsolutePath())
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

    private ReportStructure readFromJson(Report report) {
        ReportStructure structure;
        try (Reader reader = new FileReader(report.getFileLink())) {
            structure = new Gson().fromJson(reader, ReportStructure.class);
        } catch (IOException e) {
            throw new ReportFileException("Ur file was corrupted");
        }
        return structure;
    }
}
