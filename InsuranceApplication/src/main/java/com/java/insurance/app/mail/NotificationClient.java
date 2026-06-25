package com.java.insurance.app.mail;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.java.insurance.app.constants.NotificationConstant.CLIENT_NAME;
import static com.java.insurance.app.constants.NotificationConstant.FEIGN_CLIENT_URL;
import static com.java.insurance.app.constants.NotificationConstant.SEND_ENDPOINT;

/**
 * Feign client interface for communicating with the Notification Service.
 * This interface defines methods for sending notifications via HTTP requests.
 */
@FeignClient(url = FEIGN_CLIENT_URL, value = CLIENT_NAME)
public interface NotificationClient {

    /**
     * Sends a notification email to the specified email address.
     *
     * @param email         The email address of the recipient.
     * @param mailStructure The structure containing email details such as subject, message, etc.
     */
    @PostMapping(SEND_ENDPOINT)
    void sendMail(@PathVariable String email, @RequestBody MailStructure mailStructure);
}
