package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.java.insurance.app.constants.BaseUrl.BASE_URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationUrls {
    public static final String APPLICATION_URL = BASE_URL + "/application";
    public static final String GET_ALL_APPLICATIONS = APPLICATION_URL + "/all";
    public static final String CREATE_APPLICATION = APPLICATION_URL + "/create-application";
}
