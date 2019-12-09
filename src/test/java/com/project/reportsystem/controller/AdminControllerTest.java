package com.project.reportsystem.controller;

import com.project.reportsystem.configuration.LoginSuccessHandler;
import com.project.reportsystem.domain.Inspector;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.service.InspectorService;
import com.project.reportsystem.service.RequestService;
import com.project.reportsystem.service.UserService;
import com.project.reportsystem.service.impl.CustomUserDetailsServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(AdminController.class)
@WithMockUser(username = "admin@gmail.com", authorities = "ADMIN")
public class AdminControllerTest {
    private static final User USER = MOCK_USER;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RequestService requestService;

    @MockBean
    private InspectorService inspectorService;

    @MockBean
    private LoginSuccessHandler loginSuccessHandler;

    @MockBean
    private CustomUserDetailsServiceImpl userDetailsService;

    @Test
    public void createInspectorShouldReturnCreateInspectorPage() throws Exception {
        final ModelAndView modelAndView = mockMvc.perform(get("/admin/create-inspector")
                .sessionAttr("user", USER))
                .andExpect(view().name("create-inspector"))
                .andReturn().getModelAndView();

        final Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();

        assertThat(model.get("inspector"), is(notNullValue()));
    }

    @Test
    public void confirmCreationInspectorShouldReturnCreateInspectorPage() throws Exception {
        Inspector inspector = Inspector.builder()
                .name("Name")
                .build();


        mockMvc.perform(post("/admin/confirm-create")
                .sessionAttr("user", USER)
                .flashAttr("inspector", inspector)
                .param("repeatedPassword", "userpass1"))
                .andExpect(view().name("create-inspector"));
    }

    @Test
    public void confirmCreationInspectorShouldReturnErrorPage() throws Exception {
        mockMvc.perform(post("/admin/confirm-create")
                .sessionAttr("user", USER))
                .andExpect(view().name("error"));
    }

    @Test
    public void confirmCreationInspectorShouldCreateInspector() throws Exception {
        Inspector inspector = MOCK_INSPECTOR;

        mockMvc.perform(post("/admin/confirm-create")
                .sessionAttr("user", USER)
                .flashAttr("inspector", inspector)
                .param("repeatedPassword", "userpass1"))
                .andExpect(view().name("redirect:/admin/inspectors"));

        ArgumentCaptor<Inspector> inspectorCaptor = ArgumentCaptor.forClass(Inspector.class);

        verify(inspectorService).createInspector(inspectorCaptor.capture());

        assertThat(inspectorCaptor.getValue(), is(inspector));
    }

    @Test
    public void applyRequestShouldReturnRequestPage() throws Exception {
        when(userService.findById(anyLong())).thenReturn(USER);

        mockMvc.perform(get("/admin/apply-request/{id}/user/{userId}", "1", "1")
                .sessionAttr("user", USER))
                .andExpect(view().name("redirect:/admin/requests"));

        verify(userService).changeInspectorForUser(any(User.class));
        verify(requestService).deleteRequestById(anyLong());
    }

    @Test
    public void rejectRequestShouldReturnRequestsPage() throws Exception {
        mockMvc.perform(get("/admin/reject-request/{id}", "1")
                .sessionAttr("user", USER))
                .andExpect(view().name("redirect:/admin/requests"));

        verify(requestService).deleteRequestById(anyLong());
    }

    @Test
    public void showUsersShouldReturnUsersPage() throws Exception {
        when(userService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(singletonList(USER)));

        final ModelAndView modelAndView = mockMvc.perform(get("/admin/users")
                .sessionAttr("user", USER))
                .andExpect(view().name("users-list"))
                .andReturn().getModelAndView();

        final Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();

        assertThat(model.get("users"), is(not(emptyList())));
    }

    @Test
    public void showRequestShouldReturnRequestsPage() throws Exception {
        when(requestService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(singletonList(MOCK_REQUEST)));

        final ModelAndView modelAndView = mockMvc.perform(get("/admin/requests")
                .sessionAttr("user", USER))
                .andExpect(view().name("request-list"))
                .andReturn().getModelAndView();

        final Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();

        assertThat(model.get("requests"), is(not(emptyList())));
    }

    @Test
    public void showInspectorsShouldReturnInspectorsPage() throws Exception {
        when(inspectorService.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(singletonList(MOCK_INSPECTOR)));

        final ModelAndView modelAndView = mockMvc.perform(get("/admin/inspectors")
                .sessionAttr("user", USER))
                .andExpect(view().name("inspectors-list"))
                .andReturn().getModelAndView();

        final Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();

        assertThat(model.get("inspectors"), is(not(emptyList())));
    }

}
