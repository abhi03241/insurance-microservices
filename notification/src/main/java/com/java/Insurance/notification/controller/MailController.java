package com.java.Insurance.notification.controller;

import com.java.Insurance.notification.model.MailStructure;
import com.java.Insurance.notification.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MailController {

    @Autowired
    private MailService mailService;

    /**
     * Endpoint to send mail.
     * @param email The email address to send the mail to.
     * @param mailStructure The structure of the mail to be sent.
     * @return A message indicating the successful sending of the mail.
     */

    @PostMapping("/send/{email}")
    public ResponseEntity<String> sendMail(@PathVariable String email , @RequestBody MailStructure mailStructure){
        mailService.sendMail(email , mailStructure);
        return ResponseEntity.ok("Successfully sent the mail!");
    }

}
