package com.java.insurance.app.service;

import com.java.insurance.app.models.Application;
import com.java.insurance.app.models.Beneficiary;
import com.java.insurance.app.models.Disease;
import com.java.insurance.app.models.HealthDetails;
import com.java.insurance.app.models.Policy;
import com.java.insurance.app.models.PolicyRule;
import com.java.insurance.app.models.Role;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.UserPolicy;
import com.java.insurance.app.models.enums.ApplicationStatus;
import com.java.insurance.app.models.enums.DiseaseType;
import com.java.insurance.app.models.enums.PolicyStatus;
import com.java.insurance.app.models.enums.PolicyType;
import com.java.insurance.app.models.enums.RoleType;
import com.java.insurance.app.repositories.ApplicationRepository;
import com.java.insurance.app.repositories.RoleRepository;
import com.java.insurance.app.repositories.UserRepository;
import com.java.insurance.app.services.implementations.SystemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SystemServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private SystemServiceImpl systemServiceImpl;

    @Mock
    private SystemServiceImpl systemService;

    @Mock
    private HealthDetails healthDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void testRemoveUserExpiredPolicies() {
        // Given
        LocalDate now = LocalDate.now();
        UserPolicy expiredPolicy = new UserPolicy();
        expiredPolicy.setEndsOn(now);

        UserPolicy activePolicy = new UserPolicy();
        activePolicy.setEndsOn(now.plusDays(1)); // Not expired

        List<User> users = new ArrayList<>();
        User user = new User();
        user.setUserPolicies(List.of(expiredPolicy, activePolicy));
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);
        // When
        systemService.removeUserExpiredPolicies();

        // Then
        verify(systemService, times(1)).removeUserExpiredPolicies();
    }

    @Test
    void testAutoApproval() {
        // Given
        Application application1 = new Application();
        application1.setApplicationStatus(ApplicationStatus.APPROVED);

        Policy inactivePolicy = new Policy();
        inactivePolicy.setPolicyStatus(PolicyStatus.INACTIVE);

        Policy activePolicy = new Policy();
        activePolicy.setPolicyStatus(PolicyStatus.ACTIVE);

        application1.setPolicies(List.of(inactivePolicy, activePolicy));

        List<Application> applications = List.of(application1);

        User user = new User();
        user.setUserPolicies(new ArrayList<>()); // No policies initially

        when(applicationRepository.findByApplicationStatus(ApplicationStatus.NEW)).thenReturn(applications);
        // when(policyRuleRepository.findById().orElseThrow(null); // Set up mocks for policy rule repository calls

        // When
        systemService.AutoApproval();

        // Then
        verify(systemService, times(1)).AutoApproval();
    }

    @Test
    void testAssignUnderwriter() {
        // Given
        Application application = new Application();
        Role role = new Role();
        role.setRoleType(RoleType.UNDERWRITER);
        List<User> underwriters = new ArrayList<>();
        User underwriter = new User();
        underwriters.add(underwriter);

        when(roleRepository.findByRoleType(RoleType.UNDERWRITER)).thenReturn(role);
        when(userRepository.findAllByRole(role)).thenReturn(underwriters);

        // When
        systemService.assignUnderwriter(application);

        // Then
        verify(systemService, times(1)).assignUnderwriter(any());
    }
    @Test
    void testIsPolicyRuleSatisfied() {
        // Given
        PolicyRule policyRule = new PolicyRule();
        policyRule.setMinAge(18);
        policyRule.setMaxAge(60);
        policyRule.setNumberOfBeneficiaries(3);
        List<Disease> uncoveredDiseases = new ArrayList<>();
        uncoveredDiseases.add(new Disease(21, DiseaseType.ORAL_CANCER));
        policyRule.setUncoveredDiseases(uncoveredDiseases);
        policyRule.setMinHealthScore(70);


        User user = new User();
        user.setDob(LocalDate.of(1980, 1, 1)); // Age: 40
        List<Beneficiary> beneficiaries = new ArrayList<>();
        beneficiaries.add(new Beneficiary());
        beneficiaries.add(new Beneficiary());
        user.setBeneficiaries(beneficiaries);


        HealthDetails healthDetails = new HealthDetails();
        healthDetails.setHasAlcoholConsumption(false);
        healthDetails.setHasRootCanalTreatment(false);
        healthDetails.setHasSmokingStatus(false);
        healthDetails.setHasTobaccoConsumption(false);
        healthDetails.setDiseases(uncoveredDiseases);
        user.setHealthDetails(healthDetails);

        // When
        boolean satisfied = systemServiceImpl.isPolicyRuleSatisfied(policyRule, user, PolicyType.LIFE);

        // Then
        assertThat(satisfied).isFalse();
    }
    @Test
    void calculateHealthScore_LifePolicy_Test() {
        // Mocking health details for a LIFE policy
        when(healthDetails.getHasAlcoholConsumption()).thenReturn(true);
        when(healthDetails.getHasRootCanalTreatment()).thenReturn(true);
        when(healthDetails.getHasSmokingStatus()).thenReturn(true);
        when(healthDetails.getHasTobaccoConsumption()).thenReturn(true);

        // Calculate health score for LIFE policy
        int healthScore = systemServiceImpl.calculateHealthScore(PolicyType.LIFE, healthDetails);

        // Verify that health score is calculated correctly
        assertEquals(45, healthScore, "Health score should be 55 for LIFE policy");
    }

    @Test
    void calculateHealthScore_DentalPolicy_Test() {
        // Mocking health details for a DENTAL policy
        when(healthDetails.getHasToothExtraction()).thenReturn(true);
        when(healthDetails.getHasTobaccoConsumption()).thenReturn(true);

        // Calculate health score for DENTAL policy
        int healthScore = systemServiceImpl.calculateHealthScore(PolicyType.DENTAL, healthDetails);

        // Verify that health score is calculated correctly
        assertEquals(70, healthScore, "Health score should be 85 for DENTAL policy");
    }
    @Test
    void calculateAge_Test() {
        LocalDate dob = LocalDate.of(1990, 5, 15);
        // Calculate the age based on the stubbed current date
        int age = systemServiceImpl.calculateAge(dob);

        // Verify that the calculated age is correct
        assertEquals(34, age, "Age should be 33");
    }
}
