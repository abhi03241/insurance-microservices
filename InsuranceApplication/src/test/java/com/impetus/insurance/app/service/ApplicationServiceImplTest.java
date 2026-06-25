package com.java.insurance.app.service;

import com.java.insurance.app.mail.NotificationService;
import com.java.insurance.app.exception.InsuranceCustomException;
import com.java.insurance.app.models.*;
import com.java.insurance.app.models.enums.*;
import com.java.insurance.app.repositories.ApplicationRepository;
import com.java.insurance.app.repositories.PolicyRepository;
import com.java.insurance.app.services.ApplicationService;
import com.java.insurance.app.services.DiseaseService;
import com.java.insurance.app.services.PolicyService;
import com.java.insurance.app.services.UserService;
import com.java.insurance.app.services.implementations.ApplicationServiceImp;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ApplicationServiceImplTest {

    AutoCloseable autoCloseable;
    @Mock
    private PolicyService policyService;
    @Mock
    private DiseaseService diseaseService;
    @Mock
    private PolicyRepository policyRepository;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private UserService userService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private ApplicationService applicationService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        Application application = new Application();

        // Mock the PolicyRepository using @Mock annotation
        policyRepository = mock(PolicyRepository.class);

        // Create a Policy object with ID 12
        Policy policy = new Policy();
        policy.setId(12);

        // Configure the behavior of findById method to return the policy with ID 12
        when(policyRepository.findById(12)).thenReturn(Optional.of(policy));

        // Initialize other mocks and the applicationService
        policyService = new PolicyServiceImpl(policyRepository, diseaseService);

        applicationService = new ApplicationServiceImp(policyService, applicationRepository, userService, notificationService);

        // creating user
        User user = new User();
        user.setId(1);
        user.setName("Kishkin");
        user.setEmail("joshi.kishkin10@gmail.com");

        // diseases
        List<Disease> diseases = new ArrayList<>();
        // creating policy
        PolicyRule policyRule = new PolicyRule();
        policyRule.setNumberOfBeneficiaries(2);
        policyRule.setMaxAge(50);
        policyRule.setMinAge(20);
        policyRule.setUncoveredDiseases(diseases);
        policy.setPolicyName("P1");
        policy.setPolicyDesc("DESc");
        policy.setPolicyStatus(PolicyStatus.ACTIVE);
        policy.setPolicyRule(policyRule);
        Premium premium = new Premium();
        premium.setId(13);
        premium.setPremiumAmount(1000);
        premium.setPremiumType(PremiumType.MONTHLY);
        policy.setPremium(premium);
        application.setApplicationStatus(ApplicationStatus.NEW);
        application.setUser(user);
        // creating list of policy
        List<Policy> policies = new ArrayList<>();
        policies.add(policy);
        application.setPolicies(policies);
        application.setTotalMonthlyPremium(1000);
        application.setId(200);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testCreateApplication() throws InsuranceCustomException {
        Role role = new Role();
        role.setId(1);
        role.setRoleType(RoleType.CUSTOMER);

        Address address2 = new Address();
        address2.setCity("Oxford");
        address2.setPinCode("Pin Code");
        address2.setState("MD");
        address2.setStreet("Street");

        Role role2 = new Role();
        role2.setId(1);
        role2.setRoleType(RoleType.CUSTOMER);

        User user = new User();
        user.setAadharNo("Aadhar No");
        user.setAddress(address2);
        user.setApplications(new ArrayList<>());
        user.setBeneficiaries(new ArrayList<>());
        user.setDob(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setGender(Gender.MALE);
        user.setId(1);
        user.setName("Bella");
        user.setPassword("iloveyou");
        user.setPhoneNumber("6625550144");
        user.setRole(role2);
        user.setUserPolicies(new ArrayList<>());

        Application application = new Application();
        application.setApplicationStatus(ApplicationStatus.APPROVED);
        application.setCreationDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        application.setId(1);
        application.setPolicies(new ArrayList<>());
        application.setTotalMonthlyPremium(10.0d);
        application.setUser(user);

        // Act and Assert
        assertThrows(InsuranceCustomException.class, () -> applicationService.createApplication(application, 1));
    }

    /**
     * Method under test:
     * {@link ApplicationServiceImp#createApplication(Application, int)}
     */


    @Test
    void deleteApplication_Test() {
        // Given
        int applicationId = 1;

        // When
        try {
            applicationService.deleteApplication(applicationId);
        }
        catch (InsuranceCustomException ignored){}

        // Then
        verify(applicationRepository, times(0)).deleteById(applicationId);
    }

    @Test
    void getAllApplications_Test() {
        // Given
        Application application1 = new Application();
        Application application2 = new Application();
        when(applicationRepository.findAll()).thenReturn(Arrays.asList(application1, application2));

        // When
        List<Application> allApplications = applicationService.getAllApplications();

        // Then
        assertNotNull(allApplications);
        assertEquals(2, allApplications.size());
        assertTrue(allApplications.contains(application1));
        assertTrue(allApplications.contains(application2));
    }

    @Test
    void getApplicationByUserId_Test() {
        // Given
        int userId = 1;
        Application application1 = new Application();
        Application application2 = new Application();
        when(applicationRepository.findByUserId(userId)).thenReturn(Arrays.asList(application1, application2));

        // When
        List<Application> userApplications = applicationService.getApplicationByUserId(userId);

        // Then
        assertNotNull(userApplications);
        assertEquals(2, userApplications.size());
        assertTrue(userApplications.contains(application1));
        assertTrue(userApplications.contains(application2));
    }


}
