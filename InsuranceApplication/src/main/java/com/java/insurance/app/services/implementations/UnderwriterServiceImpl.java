package com.java.insurance.app.services.implementations;

import com.java.insurance.app.models.Application;
import com.java.insurance.app.mail.NotificationService;
import com.java.insurance.app.services.UnderwriterService;
import com.java.insurance.app.constants.ErrorCode;
import com.java.insurance.app.exception.InsuranceCustomException;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.UserPolicy;
import com.java.insurance.app.models.enums.ApplicationStatus;
import com.java.insurance.app.repositories.ApplicationRepository;
import com.java.insurance.app.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.java.insurance.app.constants.AppConstant.APPLICATION_APPROVED;
import static com.java.insurance.app.constants.AppConstant.APPLICATION_APPROVED_MESSAGE;
import static com.java.insurance.app.constants.AppConstant.APPLICATION_NOT_PENDING_MESSAGE;
import static com.java.insurance.app.constants.AppConstant.APPLICATION_REJECTED;
import static com.java.insurance.app.constants.AppConstant.APPLICATION_REJECTION_MESSAGE;
import static com.java.insurance.app.constants.AppConstant.COLON;
import static com.java.insurance.app.constants.AppConstant.COMMA;
import static com.java.insurance.app.constants.AppConstant.ERROR_WHILE_SENDING_NOTIFICATION;
import static com.java.insurance.app.constants.AppConstant.MESSAGE;

@Service
@AllArgsConstructor
@EnableMethodSecurity
public class UnderwriterServiceImpl implements UnderwriterService {

    private final static Logger logger = LoggerFactory.getLogger(UnderwriterServiceImpl.class);
    private final ApplicationRepository applicationRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    /**
     * Approves the application with the given ID.
     * If the application is not in the pending stage, an exception is thrown.
     * After approval, user policies are updated, and a notification is sent to the user.
     *
     * @param applicationId The ID of the application to be approved.
     * @throws InsuranceCustomException If the application is not found with the given ID or if the action cannot be performed.
     */
    @Override
    @Transactional
    public void approveApplications(int applicationId) {

        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new InsuranceCustomException("Application not found with id : " + applicationId, "APPLICATION_NOT_FOUND"));
        if (application.getApplicationStatus() != ApplicationStatus.PENDING) {
            throw new InsuranceCustomException(APPLICATION_NOT_PENDING_MESSAGE, ErrorCode.APPLICATION_NOT_PENDING);
        }
        application.setApplicationStatus(ApplicationStatus.APPROVED);
        User user = application.getUser();
        if (user.getUserPolicies().stream().anyMatch(userPolicy ->
            application.getPolicies().contains(userPolicy.getPolicy())
        )) {
            throw new InsuranceCustomException("User already has a policy which is mentioned in application", "DUPLICATE_POLICY");
        }
        user.getUserPolicies().addAll(application.getPolicies().stream().map(policy -> new UserPolicy(user, policy, LocalDate.now(), LocalDate.now().plusMonths(policy.getValidityInMonths()))).toList());
        userRepository.save(user);
        applicationRepository.save(application);
        StringBuilder policiesDetails= new StringBuilder();
        application.getPolicies().forEach(policy -> {
            policiesDetails.append("\n\nPolicy name : ").append(policy.getPolicyName()).append("\nDescription : ").append(policy.getPolicyDesc()).append("\nType : ").append(policy.getPolicyType().name());
        });

        try {
            notificationService.sendNotification(application, user, APPLICATION_APPROVED, APPLICATION_APPROVED_MESSAGE+"\n"+policiesDetails);
        } catch (Exception e) {
            logger.error(ErrorCode.NOTIFICATION_ERROR + COMMA + MESSAGE + COLON + ERROR_WHILE_SENDING_NOTIFICATION);
        }
    }


    /**
     * Rejects the application with the given ID.
     * If the application is not in the pending stage, an exception is thrown.
     * After rejection, a notification is sent to the user.
     *
     * @param applicationId The ID of the application to be rejected.
     * @throws InsuranceCustomException If the application is not found with the given ID or if the action cannot be performed.
     */
    @Override
    @Transactional
    public void rejectApplication(int applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new InsuranceCustomException("Application not found with id : " + applicationId, "APPLICATION_NOT_FOUND"));
        if (application.getApplicationStatus() != ApplicationStatus.PENDING) {
            throw new InsuranceCustomException(APPLICATION_NOT_PENDING_MESSAGE, ErrorCode.APPLICATION_NOT_PENDING);
        }
        application.setApplicationStatus(ApplicationStatus.REJECTED);
        User user = application.getUser();
        applicationRepository.save(application);
        StringBuilder policiesDetails= new StringBuilder();
        application.getPolicies().forEach(policy -> {
            policiesDetails.append("\n\nPolicy name : ").append(policy.getPolicyName()).append("\nDescription : ").append(policy.getPolicyDesc()).append("\nType : ").append(policy.getPolicyType().name());
        });

        try {
            notificationService.sendNotification(application, user, APPLICATION_REJECTED, APPLICATION_REJECTION_MESSAGE+"\n"+policiesDetails);
        } catch (Exception e) {
            logger.error(ErrorCode.NOTIFICATION_ERROR + COMMA + MESSAGE + COLON + ERROR_WHILE_SENDING_NOTIFICATION);
        }
    }

    /**
     * Retrieves all applications associated with the specified underwriter ID.
     *
     * @param underwriterId The ID of the underwriter.
     * @return A list of applications associated with the specified underwriter.
     */
    @Override
    public List<Application> getAllApplications(int underwriterId) {
        return applicationRepository.findByUnderwriterId(underwriterId);
    }

    /**
     * Retrieves all applications with the specified status.
     *
     * @param status The status of the applications to retrieve.
     * @return A list of applications with the specified status.
     */
    @Override
    public List<Application> getApplicationsByStatus(ApplicationStatus status) {
        return applicationRepository.findByApplicationStatus(status);
    }

    /**
     * Retrieves all applications associated with the specified underwriter ID and status.
     *
     * @param underwriterId The ID of the underwriter.
     * @param status        The status of the applications to retrieve.
     * @return A list of applications associated with the specified underwriter and status.
     */
    @Override
    public List<Application> getApplicationsByStatusForUnderwriter(int underwriterId, ApplicationStatus status) {
        return applicationRepository.findByUnderwriterIdAndApplicationStatus(underwriterId, status);
    }


}
