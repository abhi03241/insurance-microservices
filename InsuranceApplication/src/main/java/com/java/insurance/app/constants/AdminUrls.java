package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.java.insurance.app.constants.BaseUrl.BASE_URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminUrls {
    public static final String ADMIN_URL = BASE_URL + "/admin";

    public static final String CREATE_ADMIN = ADMIN_URL + "/create";

    public static final String CREATE_UNDERWRITER = ADMIN_URL + "/create-underwriter";

    public static final String DELETE_ADMIN = ADMIN_URL + "/delete/{adminId}";

    public static final String DELETE_UNDERWRITER = ADMIN_URL + "/delete-underwriter/{underwriterId}";
}
