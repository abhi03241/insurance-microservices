package com.java.insurance.app.services.implementations;

import com.java.insurance.app.constants.ErrorCode;
import com.java.insurance.app.exception.InsuranceCustomException;
import com.java.insurance.app.mail.NotificationService;
import com.java.insurance.app.models.Application;
import com.java.insurance.app.models.Policy;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.enums.ApplicationStatus;
import com.java.insurance.app.repositories.ApplicationRepository;
import com.java.insurance.app.services.ApplicationService;
import com.java.insurance.app.services.PolicyService;
import com.java.insurance.app.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.java.insurance.app.constants.AppConstant.APPLICATION_CREATED_MESSAGE;
import static com.java.insurance.app.constants.AppConstant.APPLICATION_CREATED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.APPLICATION_DELETED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.APPLICATION_NOT_FOUND_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.COLON;
import static com.java.insurance.app.constants.AppConstant.COMMA;
import static com.java.insurance.app.constants.AppConstant.ERROR_WHILE_SENDING_NOTIFICATION;
import static com.java.insurance.app.constants.AppConstant.MESSAGE;
import static com.java.insurance.app.constants.AppConstant.NEW_APPLICATION_CREATED;
import static com.java.insurance.app.constants.AppConstant.NO_POLICES_ARE_SPECIFIED_IN_APPLICATION;


@Service
@RequiredArgsConstructor
public class ApplicationServiceImp implements ApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImp.class);

    private final PolicyService policyService;
    private final ApplicationRepository applicationRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    /**
     * Retrieves all applications.
     *
     * @return A list of all applications.
     */
    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    /**
     * Retrieves applications by user ID.
     *
     * @param userId The ID of the user.
     * @return A list of applications associated with the specified user ID.
     */
    @Override
    public List<Application> getApplicationByUserId(int userId) {
        return applicationRepository.findByUserId(userId);
    }

    /**
     * Creates a new application.
     *
     * @param application The application to be created.
     * @param userId      The ID of the user associated with the application.
     * @return The newly created application.
     */
    @Override
    @Transactional
    public Application createApplication(Application application, int userId) throws InsuranceCustomException {
        if (application.getPolicies().isEmpty()) {
            throw new InsuranceCustomException(NO_POLICES_ARE_SPECIFIED_IN_APPLICATION, ErrorCode.NO_POLICY_SPECIFIED);
        }

        User user = userService.getUserById(userId);
        List<Policy> allPolicies = application.getPolicies().stream().map(policy -> policyService.getPolicyById(policy.getId())).collect(Collectors.toList());

        application.setPolicies(allPolicies);

        application.setCreationDateTime(LocalDateTime.now());
        application.setUser(user);
        application.setApplicationStatus(ApplicationStatus.NEW);
        application.setTotalMonthlyPremium(calculateTotalPremiumAmount(allPolicies));

        Application app = applicationRepository.save(application);
        StringBuilder policiesDetails= new StringBuilder();
        application.getPolicies().forEach(policy -> {
            policiesDetails.append("\n\nPolicy name : ").append(policy.getPolicyName()).append("\nDescription : ").append(policy.getPolicyDesc()).append("\nType : ").append(policy.getPolicyType().name());
        });

        logger.info(APPLICATION_CREATED_WITH_ID + COLON + app.getId());
        try {
            notificationService.sendNotification(app, user, NEW_APPLICATION_CREATED, APPLICATION_CREATED_MESSAGE+"\n"+"Policies In the application are: \n"+policiesDetails);
        } catch (Exception se) {
            logger.error(ErrorCode.NOTIFICATION_ERROR + COMMA + MESSAGE + COLON + ERROR_WHILE_SENDING_NOTIFICATION);
        }
        return app;


    }

    /**
     * Calculates the total premium amount for the given list of policies.
     *
     * @param allPolicies The list of policies.
     * @return The total premium amount.
     */
    private double calculateTotalPremiumAmount(List<Policy> allPolicies) {
        return allPolicies.stream().mapToDouble(policy -> switch (policy.getPremium().getPremiumType()) {
            case MONTHLY -> policy.getPremium().getPremiumAmount();
            case QUARTERLY -> policy.getPremium().getPremiumAmount() / 4d;
            case YEARLY -> policy.getPremium().getPremiumAmount() / 12d;
        }).sum();
    }

    /**
     * Deletes an application by its ID.
     *
     * @param applicationId The ID of the application to be deleted.
     */
    @Override
    public void deleteApplication(int applicationId) {
        applicationRepository.findById(applicationId).orElseThrow(() -> new InsuranceCustomException(APPLICATION_NOT_FOUND_WITH_ID + COLON + applicationId, ErrorCode.APPLICATION_NOT_FOUND));
        applicationRepository.deleteById(applicationId);
        logger.info(APPLICATION_DELETED_WITH_ID + COLON + applicationId);
    }
}
