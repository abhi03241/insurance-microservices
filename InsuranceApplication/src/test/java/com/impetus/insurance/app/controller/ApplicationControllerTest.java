package com.java.insurance.app.controller;

import com.java.insurance.app.config.security.JwtAuthenticationFilter;
import com.java.insurance.app.constants.ApplicationUrls;
import com.java.insurance.app.services.ApplicationService;
import com.java.insurance.app.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationControllerTest {


    @MockBean
    private ApplicationService applicationService;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;



    @Test
    @WithMockUser(roles = "CUSTOMER")
    void  createApplication_Test_SUCCESS() throws Exception {

        String json = "{ \"policies\": [ { \"id\": 2 } ] }";

        mockMvc.perform(MockMvcRequestBuilders.post(ApplicationUrls.CREATE_APPLICATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getAllApplications_Test_SUCCESS() throws Exception {
        mockMvc.perform(get(ApplicationUrls.GET_ALL_APPLICATIONS))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getApplicationsByUserId_Test_SUCCESS() throws Exception {
        mockMvc.perform(get(ApplicationUrls.APPLICATION_URL))
                .andExpect(status().isOk());
    }

    /* Failure Test*/
    @Test
    @WithMockUser(roles = "ADMIN")
    void  createApplication_Test_FAILURE() throws Exception {

        String json = "{ \"policies\": [ { \"id\": a } ] }";

        mockMvc.perform(post(ApplicationUrls.CREATE_APPLICATION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getAllApplications_Test_FAILURE() throws Exception {

        when(applicationService.getAllApplications()).thenThrow(new RuntimeException("Internal Server Error"));

        mockMvc.perform(get(ApplicationUrls.GET_ALL_APPLICATIONS))
                .andExpect(status().isOk());
    }

}