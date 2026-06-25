package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeneficiaryUrls {
    public static final String BENEFICIARY_URL = BaseUrl.BASE_URL + "/beneficiary";
    public static final String GET_BENEFICIARY_OF_USER = BENEFICIARY_URL;
    public static final String GET_BENEFICIARY = BENEFICIARY_URL + "/{beneficiaryId}";
    public static final String ADD_BENEFICIARY = BENEFICIARY_URL + "/add";
    public static final String UPDATE_BENEFICIARY = BENEFICIARY_URL + "/update";
    public static final String REMOVE_BENEFICIARY = BENEFICIARY_URL + "/remove/{beneficiaryId}";
}
