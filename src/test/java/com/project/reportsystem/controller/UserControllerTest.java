package com.project.reportsystem.controller;

import com.project.reportsystem.configuration.LoginSuccessHandler;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.service.*;
import com.project.reportsystem.service.impl.CustomUserDetailsServiceImpl;
import com.project.reportsystem.util.JsonHelper;
import com.project.reportsystem.util.XmlHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@WithMockUser(username = "user@gmail.com", authorities = "LEGAL_TAXPAYER")
public class UserControllerTest {
    private static final User USER = MOCK_USER;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private InspectorService inspectorService;

    @MockBean
    private RequestService requestService;

    @MockBean
    private ReportService reportService;

    @MockBean
    private ActionService actionService;

    @MockBean
    private JsonHelper jsonHelper;

    @MockBean
    private XmlHelper xmlHelper;

    @MockBean
    private LoginSuccessHandler loginSuccessHandler;

    @MockBean
    private CustomUserDetailsServiceImpl userDetailsService;

    @Test
    public void showProfileShouldReturnProfilePage() throws Exception {
        mockMvc.perform(get("/user/profile")
                .sessionAttr("user", USER))
                .andExpect(view().name("u-profile"));
    }

    @Test
    public void showUpdateProfileShouldReturnUpdateProfilePage() throws Exception {
        final ModelAndView modelAndView = mockMvc.perform(get("/user/update-profile")
                .sessionAttr("user", USER))
                .andExpect(view().name("u-update"))
                .andReturn().getModelAndView();

        final Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();

        assertThat(model.get("currentUser"), is(USER));
    }

    @Test
    public void confirmUpdateProfileShouldReturnErrorPage() throws Exception {
        mockMvc.perform(post("/user/update-profile")
                .sessionAttr("user", USER)
                .flashAttr("user", USER)
                .param("password", "user54")
                .param("repeatedPassword", "userpass1"))
                .andExpect(view().name("error"));
    }

    @Test
    public void showReportShouldReturnReportsList() throws Exception {
        when(reportService.findAllByUserId(anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(singletonList(MOCK_REPORT)));

        final ModelAndView modelAndView = mockMvc.perform(get("/user/reports")
                .sessionAttr("user", USER))
                .andExpect(view().name("report-list"))
                .andReturn().getModelAndView();

        final Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();

        assertThat(model.get("reports"), is(not(emptyList())));
    }

    @Test
    public void showReportHistoryShouldShowActions() throws Exception {
        when(actionService.findAllForReportById(anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(singletonList(MOCK_ACTION)));

        final ModelAndView modelAndView = mockMvc.perform(get("/user/report-history/{id}", "3")
                .sessionAttr("user", USER))
                .andExpect(view().name("report-history"))
                .andReturn().getModelAndView();

        final Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();

        assertThat(model.get("actions"), is(not(emptyList())));
        assertThat(model.get("reportId"), is(3L));
    }

}