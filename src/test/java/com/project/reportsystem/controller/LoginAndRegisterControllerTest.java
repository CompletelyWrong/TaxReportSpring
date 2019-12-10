package com.project.reportsystem.controller;

import com.project.reportsystem.configuration.LoginSuccessHandler;
import com.project.reportsystem.domain.User;
import com.project.reportsystem.service.UserService;
import com.project.reportsystem.service.impl.CustomUserDetailsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Objects;

import static com.project.reportsystem.MockData.MOCK_USER;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginAndRegisterController.class)
public class LoginAndRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private LoginSuccessHandler loginSuccessHandler;

    @MockBean
    private CustomUserDetailsServiceImpl userDetailsService;

    @Test
    public void mainShouldReturnMainPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(view().name("index"));
    }

    @Test
    public void loginFromShouldReturnLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(view().name("login"));
    }

    @Test
    public void registerFormShouldReturnRegisterForm() throws Exception {
        final ModelAndView modelAndView = mockMvc.perform(get("/register"))
                .andExpect(view().name("register"))
                .andReturn().getModelAndView();

        final Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();

        assertThat(model.get("user"), is(notNullValue()));
    }

    @Test
    public void signUpShouldReturnErrorPage() throws Exception {
        User user = User.builder()
                .name("Name")
                .build();

        mockMvc.perform(post("/signUp")
                .flashAttr("user", user))
                .andExpect(view().name("error"));
    }

    @Test
    public void signUpShouldReturnRegisterPage() throws Exception {
        User user = User.builder()
                .name("Name")
                .password("userpass2")
                .build();

        mockMvc.perform(post("/signUp")
                .flashAttr("user", user)
                .param("repeatedPassword", "userpass2"))
                .andExpect(view().name("register"));
    }

    @Test
    public void signUpShouldRegisterUser() throws Exception {
        User user = MOCK_USER;

        mockMvc.perform(post("/signUp")
                .flashAttr("user", user)
                .param("repeatedPassword", "userpass1"))
                .andExpect(view().name("redirect:/"));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userService).register(userCaptor.capture());

        assertThat(userCaptor.getValue(), is(user));
    }

}
