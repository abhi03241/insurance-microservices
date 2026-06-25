package com.java.insurance.app.controller;

import com.java.insurance.app.constants.UnderwriterUrls;
import com.java.insurance.app.services.UnderwriterService;
import com.java.insurance.app.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UnderwriterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UnderwriterService underwriterService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "UNDERWRITER")
    public void testApproveApplication_Success() throws Exception {
        int applicationId = 1;
        doNothing().when(underwriterService).approveApplications(applicationId);
        mockMvc.perform(put(UnderwriterUrls.APPROVE_APPLICATION, applicationId))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "UNDERWRITER")
    void rejectApplication_Success() throws Exception {
        int applicationId = 1; // Example application ID
        Mockito.doNothing().when(underwriterService).rejectApplication(applicationId);
        mockMvc.perform(put(UnderwriterUrls.REJECT_APPLICATION, applicationId))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().string("Application Rejected Successfully!"));
    }


//    @Test
//    @WithMockUser(roles = "UNDERWRITER")
//    public void testGetApplicationById_Success() throws Exception {
//        int applicationId = 1;
//
//        Application expectedApplication = new Application();
//        expectedApplication.setId(applicationId);
//        given(underwriterService.getApplicationById(applicationId)).willReturn(expectedApplication);
//
//        // Perform GET request
//        mockMvc.perform(MockMvcRequestBuilders.get(UnderwriterUrls.GET_APPLICATION, applicationId))
//                .andExpect(status().isOk());
//
//    }
//    @Test
//    @WithMockUser(roles = "UNDERWRITER")
//    public void testGetApplicationById_NotFound() throws Exception {
//        int applicationId = 1; // Replace with a valid application ID
//
//        // Mock application not found
//        given(underwriterService.getApplicationById(applicationId)).willThrow(InsuranceCustomException.class);
//
//        // Perform GET request
//        mockMvc.perform(MockMvcRequestBuilders.get(UnderwriterUrls.GET_APPLICATION, applicationId))
//                .andExpect(status().isNotFound());
//    }

}


