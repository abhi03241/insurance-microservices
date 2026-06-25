package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.java.insurance.app.constants.BaseUrl.BASE_URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PolicyUrls {
    public static final String POLICY_URL = BASE_URL + "/policy";
    public static final String GET_ALL_POLICIES = POLICY_URL + "/all";
    public static final String GET_ALL_ACTIVE_POLICIES = POLICY_URL + "/all-active";


    public static final String GET_POLICY_BY_POLICY_ID = POLICY_URL + "/{policyId}";
    public static final String CREATE_POLICY = POLICY_URL + "/create";
    public static final String DELETE_POLICY = POLICY_URL + "/delete/{policyId}";
}
