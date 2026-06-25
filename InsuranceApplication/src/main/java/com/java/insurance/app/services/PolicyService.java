package com.java.insurance.app.services;

import com.java.insurance.app.models.Policy;

import java.util.List;

public interface PolicyService {

    List<Policy> getAllPolicies();

    List<Policy> getAllActivePolicies();

    Policy getPolicyById(int id);

    Policy createPolicy(Policy policy);

    void deletePolicy(int policyId);

    List<Policy> getPolicyByUserId(int userId);

    void updatePolicyStatusIfExpired();
}
