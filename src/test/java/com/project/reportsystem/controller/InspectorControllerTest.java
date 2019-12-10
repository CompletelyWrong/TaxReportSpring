package com.project.reportsystem.controller;

import com.project.reportsystem.configuration.LoginSuccessHandler;
import com.project.reportsystem.domain.Action;
import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.Report;
import com.project.reportsystem.domain.ReportStructure;
import com.project.reportsystem.entity.Role;
import com.project.reportsystem.service.ActionService;
import com.project.reportsystem.service.InspectorService;
import com.project.reportsystem.service.ReportService;
import com.project.reportsystem.service.UserService;
import com.project.reportsystem.service.impl.CustomUserDetailsServiceImpl;
import com.project.reportsystem.util.JsonHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Objects;

import static com.project.reportsystem.MockData.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(InspectorController.class)
@WithMockUser(username = "inspector@gmail.com", authorities = "INSPECTOR")
public class InspectorControllerTest {
    private static final Inspector INSPECTOR = MOCK_INSPECTOR;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private InspectorService inspectorService;

    @MockBean
    private ReportService reportService;

    @MockBean
    private ActionService actionService;

    @MockBean
    private JsonHelper jsonHelper;

    @MockBean
    private LoginSuccessHandler loginSuccessHandler;

    @MockBean
    private CustomUserDetailsServiceImpl userDetailsService;

    @Test
    public void showProfileShouldReturnProfilePage() throws Exception {
        mockMvc.perform(get("/inspector/profile")
                .sessionAttr("user", INSPECTOR))
                .andExpect(view().name("i-profile"));
    }

    @Test
    public void showUpdateProfileShouldReturnUpdateProfilePage() throws Exception {
        final ModelAndView modelAndView = mockMvc.perform(get("/inspector/update-profile")
                .sessionAttr("user", INSPECTOR))
                .andExpect(view().name("i-update"))
                .andReturn().getModelAndView();

        final Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();

        assertThat(model.get("currentInspector"), is(INSPECTOR));
    }

    @Test
    public void confirmUpdateProfileShouldReturnUpdateProfilePage() throws Exception {
        Inspector inspector = Inspector.builder()
                .id(4L)
                .role(Role.INSPECTOR)
                .name("Name")
                .build();

        mockMvc.perform(post("/inspector/update-profile")
                .sessionAttr("user", INSPECTOR)
                .flashAttr("inspector", inspector)
                .param("password", "userpass1")
                .param("repeatedPassword", "userpass1"))
                .andExpect(view().name("i-update"));
    }

    @Test
    public void confirmUpdateProfileShouldReturnErrorPage() throws Exception {
        mockMvc.perform(post("/inspector/update-profile")
                .sessionAttr("user", INSPECTOR)
                .flashAttr("inspector", INSPECTOR)
                .param("password", "user54")
                .param("repeatedPassword", "userpass1"))
                .andExpect(view().name("error"));
    }

    @Test
    public void confirmUpdateProfileShouldUpdateProfile() throws Exception {
        mockMvc.perform(post("/inspector/update-profile")
                .sessionAttr("user", INSPECTOR)
                .flashAttr("inspector", INSPECTOR)
                .param("password", "userpass1")
                .param("repeatedPassword", "userpass1"))
                .andExpect(view().name("redirect:/inspector/profile"));

        ArgumentCaptor<Inspector> inspectorCaptor = ArgumentCaptor.forClass(Inspector.class);

        verify(inspectorService).updateInfo(inspectorCaptor.capture());

        assertThat(inspectorCaptor.getValue(), is(INSPECTOR));
    }

    @Test
    public void showAllUsersShouldReturnUsersList() throws Exception {
        when(userService.findAllByInspectorId(anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(singletonList(MOCK_USER)));

        final ModelAndView modelAndView = mockMvc.perform(get("/inspector/users")
                .sessionAttr("user", INSPECTOR))
                .andExpect(view().name("current-user-list"))
                .andReturn().getModelAndView();

        final Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();

        assertThat(model.get("users"), is(not(emptyList())));
    }

    @Test
    public void showUserReportsShouldShowUserReports() throws Exception {
        when(reportService.findAllByUserId(anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(singletonList(MOCK_REPORT)));

        final ModelAndView modelAndView = mockMvc.perform(get("/inspector/user-reports/{id}", "2")
                .sessionAttr("user", INSPECTOR))
                .andExpect(view().name("inspector_report-list"))
                .andReturn().getModelAndView();

        final Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();

        assertThat(model.get("reports"), is(not(emptyList())));
        assertThat(model.get("userId"), is(2L));
    }

    @Test
    public void showReportByIdShouldShowReport() throws Exception {
        when(reportService.findById(anyLong())).thenReturn(MOCK_REPORT);
        when(jsonHelper.readFromJson(anyString())).thenReturn(new ReportStructure());

        final ModelAndView modelAndView = mockMvc.perform(get("/inspector/read-report/{id}", "2")
                .sessionAttr("user", INSPECTOR))
                .andExpect(view().name("read-report"))
                .andReturn().getModelAndView();

        final Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();

        assertThat(model.get("action"), is(notNullValue()));
        assertThat(model.get("report"), is(notNullValue()));
        assertThat(model.get("reportId"), is(MOCK_REPORT.getId()));
    }

    @Test
    public void confirmReportAssessmentShouldUpdateReport() throws Exception {
        when(reportService.findById(anyLong())).thenReturn(MOCK_REPORT);

        mockMvc.perform(post("/inspector/confirm-report/{reportId}", "2")
                .flashAttr("action", MOCK_ACTION)
                .sessionAttr("user", INSPECTOR))
                .andExpect(view().name("redirect:/inspector/users"));

        verify(actionService).addActionToReport(any(Action.class), any(Report.class));
        verify(reportService).updateReport(any(Report.class));
    }

}