package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUrls {
    public static final String USERS_URL = BaseUrl.BASE_URL + "/user";
    public static final String GET_USER = USERS_URL;
    public static final String GET_ALL_USERS = USERS_URL + "/all";
    public static final String CREATE_USER = USERS_URL + "/create";
    public static final String UPDATE_USER = USERS_URL + "/update";
    public static final String DELETE_USER = USERS_URL + "/delete";
}
