package com.java.insurance.app.mail;

import com.java.insurance.app.models.Application;
import com.java.insurance.app.models.User;


public interface NotificationService {
    void sendNotification(Application application, User user, String sub, String msg);

}
