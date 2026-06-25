package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.java.insurance.app.constants.BaseUrl.BASE_URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleUrls {
    public static final String ROLE_URL = BASE_URL + "/role";

    public static final String CREATE_ROLE = ROLE_URL + "/create";

    public static final String GET_ROLE = ROLE_URL + "/{roleId}";

    public static final String GET_ALL_ROLE = ROLE_URL + "/all";
}
