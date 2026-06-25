package com.java.insurance.app.service;


import com.java.insurance.app.mail.NotificationClient;
import com.java.insurance.app.mail.NotificationService;
import com.java.insurance.app.models.Application;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.enums.ApplicationStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NotificationServiceImpTest {

    @Autowired
    private NotificationClient notificationClient;
    @Mock
    private NotificationService notificationService;

    @Test
    public void sendNotification_Test() {
        // Given
        Application application = new Application();
        application.setId(123);
        application.setApplicationStatus(ApplicationStatus.APPROVED);

        User user = new User();
        user.setName("testUser");
        user.setEmail("joshi.kishkin10@gmail.com");

        String subject = "Test Subject";
        String message = "Test Message";

        notificationService.sendNotification(application, user, subject, message);



    }
    }

