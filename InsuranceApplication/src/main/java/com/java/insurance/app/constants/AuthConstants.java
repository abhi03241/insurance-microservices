package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.java.insurance.app.constants.BaseUrl.BASE_URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthConstants {
    public static final String AUTH = BASE_URL + "/auth";
    public static final String REGISTER = AUTH + "/register";
    public static final String LOGIN = AUTH + "/login";
}
