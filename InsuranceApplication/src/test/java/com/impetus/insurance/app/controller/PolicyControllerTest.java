package com.java.insurance.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.insurance.app.constants.PolicyUrls;
import com.java.insurance.app.models.Disease;
import com.java.insurance.app.models.Policy;
import com.java.insurance.app.models.PolicyRule;
import com.java.insurance.app.models.Premium;
import com.java.insurance.app.models.enums.DiseaseType;
import com.java.insurance.app.models.enums.PolicyStatus;
import com.java.insurance.app.models.enums.PolicyType;
import com.java.insurance.app.models.enums.PremiumType;
import com.java.insurance.app.services.PolicyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PolicyService policyService;


    @Test
    @WithMockUser(roles = {"ADMIN","CUSTOMER"})
    void getAllPolicies_Test_SUCCESS() throws Exception {
        // Create sample policies
        PolicyRule policyRule = new PolicyRule();
        policyRule.setNumberOfBeneficiaries(2);
        policyRule.setMaxAge(50);
        policyRule.setMinAge(20);
        Policy policy = new Policy();
        policy.setPolicyName("P1");
        policy.setId(1);
        policy.setPolicyDesc("DESc");
        policy.setPolicyStatus(PolicyStatus.ACTIVE);
        policy.setPolicyRule(policyRule);
        Premium premium = new Premium();
        premium.setId(1);
        premium.setPremiumAmount(2000);
        premium.setPremiumType(PremiumType.MONTHLY);
        policy.setPremium(premium);
        List<Policy> expectedPolicies = List.of(policy);


        when(policyService.getAllPolicies()).thenReturn(expectedPolicies);


        mockMvc.perform(get(PolicyUrls.GET_ALL_POLICIES).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles = {"ADMIN","CUSTOMER"})
    void getAllActivePolicies_Test_SUCCESS() throws Exception {

        when(policyService.getAllActivePolicies()).thenReturn(Arrays.asList(new Policy(), new Policy()));

        // Perform GET request to the endpoint
        mockMvc.perform(get(PolicyUrls.GET_ALL_ACTIVE_POLICIES).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        // Add additional assertions as needed
    }

    @Test
    @WithMockUser(roles = {"ADMIN","CUSTOMER"})
    void getPolicyById_Test_SUCCESS() throws Exception {
        int policyId = 1;
        // Mock the behavior of policyService.getPolicyById() method
        when(policyService.getPolicyById(policyId)).thenReturn(new Policy());

        // Perform GET request to the endpoint
        mockMvc.perform(get(PolicyUrls.GET_POLICY_BY_POLICY_ID, policyId).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        // Add additional assertions as needed
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPolicy_Test_SUCCESS() throws Exception {
        // Create a sample Policy object
        Disease disease = new Disease();
        disease.setDiseaseType(DiseaseType.CANCER);
        List<Disease> diseases = new ArrayList<Disease>();
        diseases.add(disease);
        PolicyRule policyRule = new PolicyRule();
        policyRule.setNumberOfBeneficiaries(2);
        policyRule.setMaxAge(50);
        policyRule.setMinAge(20);
        policyRule.setUncoveredDiseases(diseases);
        Policy policy = new Policy();
        policy.setPolicyName("Policy kishkin ");
        policy.setId(1);
        policy.setPolicyDesc("DESc");
        policy.setPolicyStatus(PolicyStatus.ACTIVE);
        policy.setPolicyRule(policyRule);
        Premium premium = new Premium();
        premium.setId(1);
        premium.setPremiumAmount(2000);
        premium.setPremiumType(PremiumType.MONTHLY);
        policy.setPremium(premium);
        policy.setBenefits("Benefits");
        policy.setPolicyType(PolicyType.DENTAL);
        policy.setExpiry(LocalDate.now());


        mockMvc.perform(post(PolicyUrls.CREATE_POLICY).with(csrf()) // Add CSRF token to prevent CSRF attacks
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(policy))).andExpect(status().isCreated());


        verify(policyService, times(1)).createPolicy(any(Policy.class));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePolicy_Test_SUCCESS() throws Exception {
        int policyId = 1;
        doNothing().when(policyService).deletePolicy(policyId);


        mockMvc.perform(delete(PolicyUrls.DELETE_POLICY, policyId).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    /* Failure test */


    @Test
    @WithMockUser(roles = {"ADMIN","CUSTOMER"})
    void getPolicyById_Test_Failure() throws Exception {
        int policyId = 1;

        when(policyService.getPolicyById(policyId)).thenReturn(null);

        mockMvc.perform(get(PolicyUrls.GET_POLICY_BY_POLICY_ID, policyId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "CUSTOMER")
    void createPolicy_Test_Failure() throws Exception {
        Policy invalidPolicy = new Policy();

        mockMvc.perform(post(PolicyUrls.CREATE_POLICY).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidPolicy)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    void deletePolicy_Test_Failure() throws Exception {
        int policyId = 1;
        doNothing().when(policyService).deletePolicy(policyId);


        mockMvc.perform(delete(PolicyUrls.DELETE_POLICY, policyId).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());

    }

}
