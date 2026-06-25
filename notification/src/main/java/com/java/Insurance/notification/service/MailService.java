package com.java.Insurance.notification.service;

import com.java.Insurance.notification.model.MailStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for sending emails.
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    /**
     * Sends an email using the provided mail structure.
     * @param mail The recipient email address.
     * @param mailStructure The structure of the email including subject and message.
     */
    public void sendMail(String mail , MailStructure mailStructure){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject(mailStructure.getSubject());

        // Construct the email message including additional details
        String messageBuilder = mailStructure.getMessage() +
                "\n\nApplication Number: " + mailStructure.getApplicationNumber() +
                "\nApplication Status: " + mailStructure.getApplicationStatus() +
                "\nUser Name: " + mailStructure.getUserName();
        simpleMailMessage.setText(messageBuilder);
        simpleMailMessage.setTo(mail);

        javaMailSender.send(simpleMailMessage);
    }
}
