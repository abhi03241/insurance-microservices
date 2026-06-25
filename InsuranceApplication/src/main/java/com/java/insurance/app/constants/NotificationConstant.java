package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationConstant {
    public static final String FEIGN_CLIENT_URL = "http://localhost:9009";
    public static final String CLIENT_NAME = "Notification-Client";
    public static final String SEND_ENDPOINT = "/send/{email}";
}
