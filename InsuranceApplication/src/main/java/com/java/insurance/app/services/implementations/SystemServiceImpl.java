package com.java.insurance.app.services.implementations;

import com.java.insurance.app.models.Application;
import com.java.insurance.app.models.Disease;
import com.java.insurance.app.models.HealthDetails;
import com.java.insurance.app.models.Policy;
import com.java.insurance.app.mail.NotificationService;
import com.java.insurance.app.services.SystemService;
import com.java.insurance.app.constants.ErrorCode;
import com.java.insurance.app.exception.InsuranceCustomException;
import com.java.insurance.app.models.PolicyRule;
import com.java.insurance.app.models.Role;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.UserPolicy;
import com.java.insurance.app.models.enums.ApplicationStatus;
import com.java.insurance.app.models.enums.PolicyStatus;
import com.java.insurance.app.models.enums.PolicyType;
import com.java.insurance.app.models.enums.RoleType;
import com.java.insurance.app.repositories.ApplicationRepository;
import com.java.insurance.app.repositories.RoleRepository;
import com.java.insurance.app.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.java.insurance.app.constants.AppConstant.APPLICATION_APPROVED;
import static com.java.insurance.app.constants.AppConstant.APPLICATION_APPROVED_MESSAGE;
import static com.java.insurance.app.constants.AppConstant.APPLICATION_PENDING;
import static com.java.insurance.app.constants.AppConstant.APPLICATION_PENDING_MESSAGE;
import static com.java.insurance.app.constants.AppConstant.APPLICATION_REJECTED;
import static com.java.insurance.app.constants.AppConstant.APPLICATION_REJECTION_MESSAGE;
import static com.java.insurance.app.constants.AppConstant.COLON;
import static com.java.insurance.app.constants.AppConstant.COMMA;
import static com.java.insurance.app.constants.AppConstant.ERROR_WHILE_SENDING_NOTIFICATION;
import static com.java.insurance.app.constants.AppConstant.MESSAGE;

@Service
@AllArgsConstructor
public class SystemServiceImpl implements SystemService {
    private static final Logger logger = LoggerFactory.getLogger(SystemServiceImpl.class);
    private final ApplicationRepository applicationRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    /**
     * Removes expired policies for all users.
     * This method runs at a fixed rate of 20 seconds.
     */
    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void removeUserExpiredPolicies() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> user.setUserPolicies(user.getUserPolicies().stream().filter(userPolicy -> userPolicy.getEndsOn().equals(LocalDate.now())).toList()));
        userRepository.saveAll(users);
    }

    /**
     * Automatically approves or rejects applications based on certain conditions.
     * If the user has already subscribed to any policy in the application, the application is rejected.
     * If any policy in the application is inactive, the application is rejected.
     * If the policy rule is not satisfied, an underwriter is assigned, and the application status is set to pending.
     * Otherwise, the application is approved, and the corresponding policies are assigned to the user.
     * This method runs at a fixed rate of 15 seconds using spring scheduler.
     */
    @Transactional
    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void AutoApproval() {
        List<Application> applications = applicationRepository.findByApplicationStatus(ApplicationStatus.NEW);
        AtomicBoolean flag = new AtomicBoolean(false);
        applications.forEach(application -> {
            User user = application.getUser();
            List<UserPolicy> userPolicies = user.getUserPolicies();
            if (userPolicies.stream().anyMatch(userPolicy -> application.getPolicies().contains(userPolicy.getPolicy()))) {
                application.setApplicationStatus(ApplicationStatus.REJECTED);
                applicationRepository.save(application);
                StringBuilder policiesDetails= new StringBuilder();
                application.getPolicies().forEach(policy -> {
                    policiesDetails.append("\n\nPolicy name : ").append(policy.getPolicyName()).append("\nDescription : ").append(policy.getPolicyDesc()).append("\nType : ").append(policy.getPolicyType().name());
                });

                try {
                    notificationService.sendNotification(application, user, APPLICATION_REJECTED, APPLICATION_REJECTION_MESSAGE+"\n"+policiesDetails);
                } catch (InsuranceCustomException e) {
                    logger.error(ErrorCode.NOTIFICATION_ERROR + COMMA + MESSAGE + COLON + ERROR_WHILE_SENDING_NOTIFICATION);
                }
                return;
            }
            for (Policy policy : application.getPolicies()) {
                if (policy.getPolicyStatus() == PolicyStatus.INACTIVE) {
                    application.setApplicationStatus(ApplicationStatus.REJECTED);
                    applicationRepository.save(application);
                    StringBuilder policiesDetails= new StringBuilder();
                    application.getPolicies().forEach(policy1 -> {
                        policiesDetails.append("\n\nPolicy name : ").append(policy1.getPolicyName()).append("\nDescription : ").append(policy1.getPolicyDesc()).append("\nType : ").append(policy1.getPolicyType().name());
                    });

                    try {
                        notificationService.sendNotification(application, user, APPLICATION_REJECTED, APPLICATION_REJECTION_MESSAGE+"\n"+policiesDetails);
                    } catch (InsuranceCustomException e) {
                        logger.error(ErrorCode.NOTIFICATION_ERROR + COMMA + MESSAGE + COLON + ERROR_WHILE_SENDING_NOTIFICATION);
                    }
                    return;
                }
                PolicyRule policyRule = policy.getPolicyRule();
                if (policyRule != null && !isPolicyRuleSatisfied(policyRule, application.getUser(), policy.getPolicyType())) {
                    flag.set(true);
                }
            }
            if (flag.get()) {
                assignUnderwriter(application);
                application.setApplicationStatus(ApplicationStatus.PENDING);
                applicationRepository.save(application);
                StringBuilder policiesDetails= new StringBuilder();
                application.getPolicies().forEach(policy -> {
                    policiesDetails.append("\n\nPolicy name : ").append(policy.getPolicyName()).append("\nDescription : ").append(policy.getPolicyDesc()).append("\nType : ").append(policy.getPolicyType().name());
                });

                try {
                    notificationService.sendNotification(application, user, APPLICATION_PENDING, APPLICATION_PENDING_MESSAGE+"\n"+policiesDetails);
                } catch (InsuranceCustomException e) {
                    logger.error(ErrorCode.NOTIFICATION_ERROR + COMMA + MESSAGE + COLON + ERROR_WHILE_SENDING_NOTIFICATION);
                }

                return;
            }
            application.setApplicationStatus(ApplicationStatus.APPROVED);
            applicationRepository.save(application);
            StringBuilder policiesDetails= new StringBuilder();
            application.getPolicies().forEach(policy -> {
                policiesDetails.append("\n\nPolicy name : ").append(policy.getPolicyName()).append("\nDescription : ").append(policy.getPolicyDesc()).append("\nType : ").append(policy.getPolicyType().name());
            });


            try {
                notificationService.sendNotification(application, user, APPLICATION_APPROVED, APPLICATION_APPROVED_MESSAGE+"\n"+policiesDetails);
            } catch (InsuranceCustomException e) {
                logger.error(ErrorCode.NOTIFICATION_ERROR + COMMA + MESSAGE + COLON + ERROR_WHILE_SENDING_NOTIFICATION);
            }
            List<Policy> approvedPolicies = application.getPolicies().stream().filter(policy -> policy.getPolicyStatus() == PolicyStatus.ACTIVE).toList();

            user.getUserPolicies().addAll(approvedPolicies.stream().map(policy -> new UserPolicy(user, policy, LocalDate.now(), LocalDate.now().plusMonths(policy.getValidityInMonths()))).toList());
            userRepository.save(user);
        });
    }


    /**
     * Assigns an underwriter to the given application.
     *
     * @param application The application to which an underwriter will be assigned.
     */
    public void assignUnderwriter(Application application) {
        Role role = roleRepository.findByRoleType(RoleType.UNDERWRITER);
        List<User> underwriters = userRepository.findAllByRole(role);
        application.setUnderwriter(underwriters.get(new Random().nextInt(underwriters.size())));
        applicationRepository.save(application);

    }


    /**
     * Checks if the given user satisfies the policy rule criteria.
     *
     * @param policyRule The policy rule to be satisfied.
     * @param user       The user for whom the policy rule is being checked.
     * @param policyType The type of policy (e.g., LIFE, DENTAL).
     * @return True if the user satisfies the policy rule criteria, otherwise false.
     */
    public boolean isPolicyRuleSatisfied(PolicyRule policyRule, User user, PolicyType policyType) {
        int userAge = calculateAge(user.getDob());
        int noOfBeneficiaries = user.getBeneficiaries().size();
        List<Disease> userDiseases = user.getHealthDetails().getDiseases();
        boolean hasDisease = policyRule.getUncoveredDiseases().stream().anyMatch(userDiseases::contains);
        boolean ageInRange = userAge >= policyRule.getMinAge() && userAge <= policyRule.getMaxAge();
        boolean beneficiariesInRange = noOfBeneficiaries <= policyRule.getNumberOfBeneficiaries();
        boolean hasHealthScoreInRange = calculateHealthScore(policyType, user.getHealthDetails()) >= policyRule.getMinHealthScore();
        return !hasDisease && hasHealthScoreInRange && ageInRange && beneficiariesInRange;
    }

    /**
     * Calculates the health score based on the policy type and health details.
     *
     * @param policyType    The type of policy (e.g., LIFE, DENTAL).
     * @param healthDetails The health details of the individual.
     * @return The calculated health score.
     */
    public int calculateHealthScore(PolicyType policyType, HealthDetails healthDetails) {
        int healthScore = 100;
        switch (policyType) {
            case LIFE -> {
                if (healthDetails.getHasAlcoholConsumption()) {
                    healthScore = healthScore - 15;
                }
                if (healthDetails.getHasRootCanalTreatment()) {
                    healthScore = healthScore - 15;
                }
                if (healthDetails.getHasSmokingStatus()) {
                    healthScore = healthScore - 15;
                }
                if (healthDetails.getHasTobaccoConsumption()) {
                    healthScore = healthScore - 10;
                }
            }
            case DENTAL -> {
                if (healthDetails.getHasToothExtraction()) {
                    healthScore = healthScore - 15;
                }
                if (healthDetails.getHasTobaccoConsumption()) {
                    healthScore = healthScore - 15;
                }
            }
        }
        return healthScore;
    }

    /**
     * Calculates the age based on the given date of birth.
     *
     * @param dob The date of birth to calculate the age from.
     * @return The calculated age in years.
     */
    public int calculateAge(LocalDate dob) {
        LocalDate CurrentDate = LocalDate.now();
        Period period = Period.between(dob, CurrentDate);
        return period.getYears();
    }
}

