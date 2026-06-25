package com.java.insurance.app.mail;

import com.java.insurance.app.models.Application;
import com.java.insurance.app.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationClient notificationClient;

    /**
     * Sends a notification email to the specified user regarding the given application.
     *
     * @param application The application for which the notification is sent.
     * @param user        The user who will receive the notification.
     * @param sub         The subject of the notification email.
     * @param msg         The message content of the notification email.
     */
    @Override
    public void sendNotification(Application application, User user, String sub, String msg) {
        MailStructure mailStructure = new MailStructure();
        mailStructure.setSubject(sub);
        mailStructure.setMessage(msg);
        mailStructure.setApplicationNumber(String.valueOf(application.getId()));
        mailStructure.setUserName(user.getUsername());
        mailStructure.setApplicationStatus(application.getApplicationStatus().toString());
        notificationClient.sendMail(user.getEmail(), mailStructure);
    }
}
