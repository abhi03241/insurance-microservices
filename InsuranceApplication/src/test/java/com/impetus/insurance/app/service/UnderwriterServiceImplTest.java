package com.java.insurance.app.service;

import com.java.insurance.app.exception.InsuranceCustomException;
import com.java.insurance.app.mail.NotificationService;
import com.java.insurance.app.models.Application;
import com.java.insurance.app.models.Policy;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.UserPolicy;
import com.java.insurance.app.models.enums.ApplicationStatus;
import com.java.insurance.app.models.enums.Gender;
import com.java.insurance.app.repositories.ApplicationRepository;
import com.java.insurance.app.repositories.UserRepository;
import com.java.insurance.app.services.UserService;
import com.java.insurance.app.services.implementations.UnderwriterServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.java.insurance.app.constants.AppConstant.APPLICATION_REJECTED;

import static com.java.insurance.app.constants.AppConstant.APPLICATION_REJECTION_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnderwriterServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserService userService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UnderwriterServiceImpl underwriterServiceImpl;

    @Test
    void approveApplications_Test() {
        // Create a sample application
        Application application = new Application();
        application.setId(1);
        application.setApplicationStatus(ApplicationStatus.PENDING);
        // create User
        User user = new User();
        user.setId(22);
        user.setName("Kishkin");
        user.setGender(Gender.MALE);
        user.setEmail("joshi.kishkin10@gmail.com");


        Policy policy1 = new Policy();
        policy1.setId(12);
        policy1.setPolicyName("Policy 1");
        policy1.setPolicyDesc("policy1 desc");

        Policy policy2 = new Policy();
        policy1.setId(32);
        policy1.setPolicyName("Policy 2");
        policy2.setPolicyDesc("policy2 desc");

        // Add the policy to the application
        application.getPolicies().add(policy1);
        application.getPolicies().add(policy2);
        List<UserPolicy> userPolicies = new ArrayList<>();
        UserPolicy userPolicy1 = new UserPolicy();
        userPolicy1.setPolicy(policy1);
        UserPolicy userPolicy2 = new UserPolicy();
        userPolicy2.setPolicy(policy2);
        userPolicies.add(userPolicy1);
        userPolicies.add(userPolicy2);
        user.setUserPolicies(userPolicies);
        application.setUser(user);
        // Mock the behavior of applicationRepository.findById to return the sample application
        when(applicationRepository.findById(1)).thenReturn(Optional.of(application));

        // Mock the behavior of userRepository.save to return the user after saving
        // Call the method under test
        assertThrows(InsuranceCustomException.class, () -> {
            underwriterServiceImpl.approveApplications(1);
        });

        // Verify that the application status is updated to APPROVED
        assertEquals(ApplicationStatus.APPROVED, application.getApplicationStatus());
    }

    @Test
    void rejectApplication_Test() {
        // Create a sample application
        Application application = new Application();
        application.setId(1);
        application.setApplicationStatus(ApplicationStatus.PENDING);

        // Create a sample user
        User user = new User();
        user.setId(1); // Set a valid user ID
        user.setName("Kishkin");
        user.setEmail("joshi.kishkin10@gmail.com");
        user.setGender(Gender.MALE);
        application.setUser(user);

        // Mock the behavior of applicationRepository.findById to return the sample application
        when(applicationRepository.findById(1)).thenReturn(Optional.of(application));

        underwriterServiceImpl.rejectApplication(1);

        // Verify that the application status is updated to REJECTED
        assertEquals(ApplicationStatus.REJECTED, application.getApplicationStatus());

        // Verify that applicationRepository.save is called once with the updated application
        verify(applicationRepository, times(1)).save(application);

        // Verify that notificationService.sendNotification is called once
        // Verify that notificationService.sendNotification is called once with the correct parameters

    }

    @Test
    void getAllApplications_Test() {
        // Given
        int underwriterId = 1; // Set a valid underwriter ID

        // Mock the behavior of applicationRepository.findByUnderwriterId to return a list of applications
        List<Application> applications = List.of(new Application(), new Application());
        when(applicationRepository.findByUnderwriterId(underwriterId)).thenReturn(applications);

        // When
        List<Application> result = underwriterServiceImpl.getAllApplications(underwriterId);

        // Then
        assertEquals(2, result.size()); // Verify that the correct number of applications is returned
        assertEquals(applications, result); // Verify that the returned list matches the mocked list
    }

//    @Test
//    void getApplicationById_Test() {
//        // Given
//        int applicationId = 1; // Set a valid application ID
//
//        // Mock the behavior of applicationRepository.findById to return an optional containing an application
//        Application application = new Application();
//        when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(application));
//
//        // When
//        Application result = underwriterServiceImp.getApplicationById(applicationId);
//
//        // Then
//        Assertions.assertThat(result).isNotNull(); // Verify that the optional contains a value
//        Assertions.assertThat(result).isEqualTo(application); // Verify that the returned application matches the mocked application
//    }

    @Test
    void getApplicationsByStatus_Test() {
        // Given
        ApplicationStatus status = ApplicationStatus.PENDING; // Set a valid application status

        // Mock the behavior of applicationRepository.findByApplicationStatus to return a list of applications
        List<Application> applications = List.of(new Application(), new Application());
        when(applicationRepository.findByApplicationStatus(status)).thenReturn(applications);

        // When
        List<Application> result = underwriterServiceImpl.getApplicationsByStatus(status);

        // Then
        assertEquals(2, result.size()); // Verify that the correct number of applications is returned
        assertEquals(applications, result); // Verify that the returned list matches the mocked list
    }

    @Test
    void getApplicationsByStatusForUnderwriter_Test() {
        // Given
        int underwriterId = 1; // Set a valid underwriter ID
        ApplicationStatus status = ApplicationStatus.PENDING; // Set a valid application status

        // Mock the behavior of applicationRepository.findByUnderwriterIdAndApplicationStatus to return a list of applications
        List<Application> applications = List.of(new Application(), new Application());
        when(applicationRepository.findByUnderwriterIdAndApplicationStatus(underwriterId, status)).thenReturn(applications);

        // When
        List<Application> result = underwriterServiceImpl.getApplicationsByStatusForUnderwriter(underwriterId, status);

        // Then
        assertEquals(2, result.size()); // Verify that the correct number of applications is returned
        assertEquals(applications, result); // Verify that the returned list matches the mocked list
    }

    /* Failure Test Cases */


}
