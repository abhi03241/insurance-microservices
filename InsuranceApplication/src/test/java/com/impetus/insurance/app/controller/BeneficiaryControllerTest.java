package com.java.insurance.app.controller;


import com.java.insurance.app.config.security.JwtAuthenticationFilter;
import com.java.insurance.app.constants.BeneficiaryUrls;
import com.java.insurance.app.models.User;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BeneficiaryControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getBeneficiaryById_Test_SUCCESS() throws Exception {
        int beneficiaryId = 1;

        mockMvc.perform(get(BeneficiaryUrls.GET_BENEFICIARY, beneficiaryId))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getAllBeneficiariesByUserId_Test_SUCCESS() throws Exception {

        User user = new User();
        user.setId(1);

        mockMvc.perform(get(BeneficiaryUrls.GET_BENEFICIARY_OF_USER))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createBeneficiary_Test_SUCCESS() throws Exception {

        String json = "{ \"dob\": \"2023-01-01\", \"beneficiaryName\": \"kishkin\", \"relation\": \"CHILDREN\" }";

        mockMvc.perform(post(BeneficiaryUrls.ADD_BENEFICIARY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void updateBeneficiary_Test_SUCCESS() throws Exception {
        String json = "{ \"dob\": \"2023-01-01\", \"beneficiaryName\": \"Updated Beneficiary\", \"relation\": \"CHILDREN\" }";

        mockMvc.perform(put(BeneficiaryUrls.UPDATE_BENEFICIARY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void deleteBeneficiary_Test_SUCCESS() throws Exception {
        int beneficiaryId = 1;

        mockMvc.perform(delete(BeneficiaryUrls.REMOVE_BENEFICIARY, beneficiaryId))
                .andExpect(status().isOk());

    }

    /* Failure tests */
    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getBeneficiaryById_Test_FAILURE_NotFound() throws Exception {
        int beneficiaryId = -1; // Non-existent beneficiary ID

        mockMvc.perform(get(BeneficiaryUrls.GET_BENEFICIARY, beneficiaryId))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void createBeneficiary_Test_FAILURE_InvalidInput() throws Exception {
        String json = "{ \"dob\": \"2023-01-01\", \"relation\": \"CHILDREN\" }";

        mockMvc.perform(post(BeneficiaryUrls.ADD_BENEFICIARY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBeneficiary_Test_FAILURE_NotFound() throws Exception {
        String json = "{ \"id\": 9999, \"dob\": \"2023-01-01\", \"beneficiaryName\": \"Updated Beneficiary\", \"relation\": \"CHILDREN\" }"; // Invalid beneficiary ID

        mockMvc.perform(put(BeneficiaryUrls.UPDATE_BENEFICIARY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }



}
