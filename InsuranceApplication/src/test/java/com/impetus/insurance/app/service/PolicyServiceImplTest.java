package com.java.insurance.app.service;

import com.java.insurance.app.models.Disease;
import com.java.insurance.app.models.Policy;
import com.java.insurance.app.models.PolicyRule;
import com.java.insurance.app.models.Premium;
import com.java.insurance.app.models.enums.DiseaseType;
import com.java.insurance.app.models.enums.PolicyStatus;
import com.java.insurance.app.models.enums.PolicyType;
import com.java.insurance.app.models.enums.PremiumType;
import com.java.insurance.app.repositories.PolicyRepository;
import com.java.insurance.app.services.DiseaseService;
import com.java.insurance.app.services.PolicyService;
import com.java.insurance.app.services.implementations.PolicyServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PolicyServiceImplTest {

    AutoCloseable autoCloseable;
    Policy policy;
    @Mock
    private PolicyRepository policyRepository;
    private PolicyService policyService;
    @Mock
    private DiseaseService diseaseService;


    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        List<Disease> diseases = new ArrayList<>();
        policyService = new PolicyServiceImpl(policyRepository, diseaseService);
        PolicyRule policyRule = new PolicyRule();
        policyRule.setNumberOfBeneficiaries(2);
        policyRule.setMaxAge(50);
        policyRule.setMinAge(20);
        policyRule.setUncoveredDiseases(diseases);
        policy = new Policy();
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

    }

    @Test
    void createPolicy_Test() {

        // Create a sample policy object
        Disease disease = new Disease();
        disease.setDiseaseType(DiseaseType.CANCER);
        List<Disease> diseases = new ArrayList<Disease>();
        diseases.add(disease);
        PolicyRule policyRule = new PolicyRule();
        policyRule.setNumberOfBeneficiaries(2);
        policyRule.setMaxAge(50);
        policyRule.setMinAge(20);
        policyRule.setUncoveredDiseases(diseases);
        policy = new Policy();
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

        // Mock the behavior of dependencies
        when(diseaseService.getDiseaseByType(any())).thenReturn(new Disease());

        // Mock the behavior of policyRepository.save to return the policy object with an ID
        when(policyRepository.save(any(Policy.class))).thenAnswer(invocation -> {
            Policy savedPolicy = invocation.getArgument(0);
            savedPolicy.setId(1); // Setting an ID
            return savedPolicy;
        });

        // Call the method under test
        Policy createdPolicy = policyService.createPolicy(policy);

        // Assert that the created policy is not null
        assertNotNull(createdPolicy);

        // Assert that the ID of the created policy is not null
        assertNotNull(createdPolicy.getId());


        // Assert other properties of the created policy as needed...

        // Verify that policyRepository.save was called
        verify(policyRepository, times(1)).save(any(Policy.class));
    }

    @Test
    void getAllPolicies_Test() {
        // Mock the PolicyRepository
        PolicyRepository policyRepository = mock(PolicyRepository.class);
        // Mock the response from findAll method
        List<Policy> expectedPolicies = Arrays.asList(new Policy(), new Policy());
        when(policyRepository.findAll()).thenReturn(expectedPolicies);

        // Create PolicyService instance with mocked PolicyRepository
        PolicyService policyService = new PolicyServiceImpl(policyRepository, diseaseService);

        // Call the method under test
        List<Policy> actualPolicies = policyService.getAllPolicies();

        // Assert the result
        assertEquals(expectedPolicies.size(), actualPolicies.size());
        assertTrue(actualPolicies.containsAll(expectedPolicies));
    }

    @Test
    void getAllActivePolicies_Test() {
        // Mock the PolicyRepository
        PolicyRepository policyRepository = mock(PolicyRepository.class);
        // Mock the response from findAll method
        Premium premium = new Premium();
        premium.setPremiumType(PremiumType.MONTHLY);
        premium.setPremiumAmount(1000);
        LocalDate expiryDate = LocalDate.now(); // Current date and time
        int validityInMonths = 3;
        PolicyRule policyRule = new PolicyRule();
        policyRule.setNumberOfBeneficiaries(2);
        policyRule.setMaxAge(50);
        policyRule.setMinAge(20);
        policyRule.setUncoveredDiseases(null);

        List<Policy> allPolicies = Arrays.asList(new Policy(1, PolicyType.LIFE, policyRule, premium, expiryDate, validityInMonths, PolicyStatus.ACTIVE, "benefits", "Desc", "name"), new Policy(2, PolicyType.DENTAL, policyRule, premium, expiryDate, 4, PolicyStatus.INACTIVE, "benefits2", "Desc2", "name2"));
        when(policyRepository.findAll()).thenReturn(allPolicies);

        // Create PolicyService instance with mocked PolicyRepository
        PolicyService policyService = new PolicyServiceImpl(policyRepository, diseaseService);

        // Call the method under test
        List<Policy> activePolicies = policyService.getAllActivePolicies();

        // Assert the result
        assertEquals(1, activePolicies.size()); // Only 2 policies are active
        assertTrue(activePolicies.stream().allMatch(policy -> policy.getPolicyStatus() == PolicyStatus.ACTIVE));
    }

    @Test
    void getPolicyById_Test() {
        // Mock the PolicyRepository
        PolicyRepository policyRepository = mock(PolicyRepository.class);
        // Mock the response from findById method
        int policyId = 1;
        Premium premium = new Premium();
        premium.setPremiumType(PremiumType.MONTHLY);
        premium.setPremiumAmount(1000);
        LocalDate expiryDate = LocalDate.now(); // Current date and time
        int validityInMonths = 3;
        PolicyRule policyRule = new PolicyRule();
        policyRule.setNumberOfBeneficiaries(2);
        policyRule.setMaxAge(50);
        policyRule.setMinAge(20);
        policyRule.setUncoveredDiseases(null);
        Policy expectedPolicy = new Policy(1, PolicyType.LIFE, policyRule, premium, expiryDate, validityInMonths, PolicyStatus.ACTIVE, "benefits", "Desc", "name");
        when(policyRepository.findById(policyId)).thenReturn(Optional.of(expectedPolicy));

        // Create PolicyService instance with mocked PolicyRepository
        PolicyService policyService = new PolicyServiceImpl(policyRepository, diseaseService);

        // Call the method under test
        Policy actualPolicy = policyService.getPolicyById(policyId);

        // Assert the result
        assertEquals(expectedPolicy, actualPolicy);
    }


    @Test
    void deletePolicy_Test() {
        int policyId = 1;
        Premium premium = new Premium();
        premium.setPremiumType(PremiumType.MONTHLY);
        premium.setPremiumAmount(1000);
        LocalDate expiryDate = LocalDate.now(); // Current date and time
        int validityInMonths = 3;
        PolicyRule policyRule = new PolicyRule();
        policyRule.setNumberOfBeneficiaries(2);
        policyRule.setMaxAge(50);
        policyRule.setMinAge(20);
        policyRule.setUncoveredDiseases(null);
        Policy expectedPolicy = new Policy(1, PolicyType.LIFE, policyRule, premium, expiryDate, validityInMonths, PolicyStatus.ACTIVE, "benefits", "Desc", "name");

        // Mock the PolicyRepository
        PolicyRepository policyRepository = mock(PolicyRepository.class);
        when(policyRepository.findById(policyId)).thenReturn(Optional.of(policy));

        // Create PolicyService instance with mocked PolicyRepository
        PolicyService policyService = new PolicyServiceImpl(policyRepository, diseaseService);

        // Call the method under test
        policyService.deletePolicy(policyId);

        // Verify that save method is called with updated policy status
        verify(policyRepository, times(1)).save(policy);
        assertEquals(PolicyStatus.INACTIVE, policy.getPolicyStatus());
    }

    @Test
    void getPolicyByUserId_Test() {
        int userId = 1;
        List<Policy> expectedPolicies = Arrays.asList(new Policy(), new Policy());

        // Mock the PolicyRepository
        PolicyRepository policyRepository = mock(PolicyRepository.class);
        when(policyRepository.findByUserId(userId)).thenReturn(expectedPolicies);

        // Create PolicyService instance with mocked PolicyRepository
        PolicyService policyService = new PolicyServiceImpl(policyRepository, diseaseService);

        // Call the method under test
        List<Policy> actualPolicies = policyService.getPolicyByUserId(userId);

        // Assert the result
        assertEquals(expectedPolicies.size(), actualPolicies.size());
        assertTrue(actualPolicies.containsAll(expectedPolicies));
    }

    @Test
    void testUpdatePolicyStatusIfExpired() {
        // Mock today's date
        LocalDate today = LocalDate.now();
        Premium premium = new Premium();
        premium.setPremiumType(PremiumType.MONTHLY);
        premium.setPremiumAmount(1000);
        LocalDate expiryDate = LocalDate.now(); // Current date and time
        int validityInMonths = 3;
        PolicyRule policyRule = new PolicyRule();
        policyRule.setNumberOfBeneficiaries(2);
        policyRule.setMaxAge(50);
        policyRule.setMinAge(20);
        policyRule.setUncoveredDiseases(null);

        // Mock the PolicyRepository
        PolicyRepository policyRepository = mock(PolicyRepository.class);
        List<Policy> activePolicies = Arrays.asList(new Policy(1, PolicyType.LIFE, policyRule, premium, LocalDate.now().plusDays(1), validityInMonths, PolicyStatus.ACTIVE, "benefits", "Desc", "name"), new Policy(1, PolicyType.DENTAL, policyRule, premium, LocalDate.now().plusDays(2), validityInMonths, PolicyStatus.INACTIVE, "benefits2", "Desc2", "name2"));
        when(policyRepository.findByExpiryBeforeAndPolicyStatus(today, PolicyStatus.ACTIVE)).thenReturn(activePolicies);

        // Create PolicyService instance with mocked PolicyRepository
        PolicyService policyService = new PolicyServiceImpl(policyRepository, diseaseService);

        // Call the method under test
        policyService.updatePolicyStatusIfExpired();

        // Verify that save method is called with updated policy status
        verify(policyRepository, times(2)).save(any(Policy.class)); // Assuming 2 policies are expired
        // You may add more assertions to verify specific behavior based on your implementation
    }
}


