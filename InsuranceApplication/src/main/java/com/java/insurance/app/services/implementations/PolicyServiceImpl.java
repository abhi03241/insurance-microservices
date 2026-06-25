package com.java.insurance.app.services.implementations;

import com.java.insurance.app.constants.ErrorCode;
import com.java.insurance.app.exception.InsuranceCustomException;
import com.java.insurance.app.models.Policy;
import com.java.insurance.app.models.PolicyRule;
import com.java.insurance.app.models.enums.PolicyStatus;
import com.java.insurance.app.repositories.PolicyRepository;
import com.java.insurance.app.services.DiseaseService;
import com.java.insurance.app.services.PolicyService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.java.insurance.app.constants.AppConstant.COLON;
import static com.java.insurance.app.constants.AppConstant.POLICY_CREATED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.POLICY_NOT_FOUND_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.POLICY_UPDATED_WITH_ID;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {

    private static final Logger logger = LoggerFactory.getLogger(PolicyServiceImpl.class);
    private final PolicyRepository policyRepository;
    private final DiseaseService diseaseService;


    /**
     * Retrieves all policies.
     *
     * @return A list of all policies.
     */
    @Override
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    /**
     * Retrieves all active policies.
     *
     * @return A list of all active policies.
     */
    public List<Policy> getAllActivePolicies() {
        List<Policy> allPolicies = policyRepository.findAll();
        return allPolicies.stream().filter(policy -> policy.getPolicyStatus() == PolicyStatus.ACTIVE).toList();
    }


    /**
     * Retrieves a policy entity by its ID.
     *
     * @param policyId The ID of the policy to retrieve.
     * @return The policy entity with the specified ID.
     * @throws InsuranceCustomException If the policy with the specified ID is not found.
     */
    @Override
    public Policy getPolicyById(int policyId) {
        return policyRepository.findById(policyId).orElseThrow(() -> new InsuranceCustomException(POLICY_NOT_FOUND_WITH_ID + COLON + policyId, ErrorCode.POLICY_NOT_FOUND));
    }

    /**
     * Creates a new policy entity.
     *
     * @param policy The policy to be created.
     * @return The created policy entity.
     */
    @Override
    public Policy createPolicy(Policy policy) {
        policy.setPolicyStatus(PolicyStatus.ACTIVE);
        PolicyRule policyRule = policy.getPolicyRule();
        policyRule.setUncoveredDiseases(policyRule.getUncoveredDiseases().stream().map(disease -> diseaseService.getDiseaseByType(disease.getDiseaseType())).toList());
        Policy savedPolicy = policyRepository.save(policy);
        logger.info(POLICY_CREATED_WITH_ID + COLON + savedPolicy.getId());
        return savedPolicy;
    }

    /**
     * Deletes a policy entity by its ID.
     *
     * @param policyId The ID of the policy to delete.
     */
    @Override
    public void deletePolicy(int policyId) {
        Policy policy = policyRepository.findById(policyId).orElseThrow(() -> new InsuranceCustomException(POLICY_NOT_FOUND_WITH_ID + COLON + policyId, ErrorCode.POLICY_NOT_FOUND));
        policy.setPolicyStatus(PolicyStatus.INACTIVE);
        policyRepository.save(policy);
        logger.info(POLICY_UPDATED_WITH_ID + COLON + policy.getId());
    }

    /**
     * Retrieves policies associated with a user by the user's ID.
     *
     * @param userId The ID of the user.
     * @return A list of policies associated with the user.
     */
    @Override
    public List<Policy> getPolicyByUserId(int userId) {
        return policyRepository.findByUserId(userId);
    }

    /**
     * Updates the status of policies if they have expired.
     */
    @Scheduled(fixedRate = 5 * 60 * 1000)
    @Override
    public void updatePolicyStatusIfExpired() {
        LocalDate today = LocalDate.now();
        List<Policy> policies = policyRepository.findByExpiryBeforeAndPolicyStatus(today, PolicyStatus.ACTIVE);
        for (Policy policy : policies) {
            policy.setPolicyStatus(PolicyStatus.INACTIVE);
            policyRepository.save(policy);
            logger.info(POLICY_UPDATED_WITH_ID + COLON + policy.getId());
        }
    }

}
