package com.java.insurance.app.controllers;


import com.java.insurance.app.constants.AccessConstants;
import com.java.insurance.app.constants.PolicyUrls;
import com.java.insurance.app.models.Policy;
import com.java.insurance.app.services.PolicyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.java.insurance.app.constants.AppConstant.POLICY_ID;
import static com.java.insurance.app.constants.AppConstant.POLICY_INACTIVE;

@RestController
@RequiredArgsConstructor
@EnableMethodSecurity
@Tag(name = "Policy APIs")
public class PolicyController {
    private final PolicyService policyService;

    /**
     * Retrieves all policies.
     *
     * @return List of all policies.
     */
    @GetMapping(PolicyUrls.GET_ALL_POLICIES)
    @PreAuthorize(AccessConstants.HAS_ROLES_USER_ADMIN)
    public List<Policy> getAllPolicies() {
        return policyService.getAllPolicies();
    }

    /**
     * Retrieves all active policies.
     *
     * @return List of active policies.
     */
    @GetMapping(PolicyUrls.GET_ALL_ACTIVE_POLICIES)
    @PreAuthorize(AccessConstants.HAS_ROLES_USER_ADMIN)
    public List<Policy> getAllActivePolicies() {
        return policyService.getAllActivePolicies();
    }

    /**
     * Retrieves a policy by ID.
     *
     * @param policyId The ID of the policy to retrieve.
     * @return The policy corresponding to the given ID.
     */
    @GetMapping(PolicyUrls.GET_POLICY_BY_POLICY_ID)
    @PreAuthorize(AccessConstants.HAS_ROLES_USER_ADMIN)
    public Policy getPolicyById(@PathVariable(POLICY_ID) int policyId) {
        return policyService.getPolicyById(policyId);
    }

    /**
     * Creates a new policy.
     *
     * @param policy The policy to create.
     * @return The created policy.
     */
    @PreAuthorize(AccessConstants.HAS_ROLE_ADMIN)
    @PostMapping(PolicyUrls.CREATE_POLICY)
    public ResponseEntity<Policy> createPolicy(@RequestBody @Valid Policy policy) {
        return new ResponseEntity<>(policyService.createPolicy(policy), HttpStatus.CREATED);
    }

    /**
     * Deletes a policy by ID.
     *
     * @param policyId The ID of the policy to delete.
     * @return ResponseEntity indicating the success of the operation.
     */
    @PreAuthorize(AccessConstants.HAS_ROLE_ADMIN)
    @DeleteMapping(PolicyUrls.DELETE_POLICY)
    public ResponseEntity<String> deletePolicy(@PathVariable(POLICY_ID) int policyId) {
        policyService.deletePolicy(policyId);
        return ResponseEntity.ok(POLICY_INACTIVE);
    }
}
