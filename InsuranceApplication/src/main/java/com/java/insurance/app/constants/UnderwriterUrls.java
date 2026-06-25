package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.java.insurance.app.constants.BaseUrl.BASE_URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UnderwriterUrls {
    public static final String UNDERWRITER_URL = BASE_URL + "/underwriter";
    public static final String APPROVE_APPLICATION = UNDERWRITER_URL + "/approve-application/{applicationId}";

    public static final String REJECT_APPLICATION = UNDERWRITER_URL + "/reject-application/{applicationId}";
    public static final String GET_ALL_APPLICATIONS = UNDERWRITER_URL + "/all";
    public static final String GET_APPLICATION_OF_UNDERWRITER_BY_STATUS = UNDERWRITER_URL + "/applications/{status}";

}
