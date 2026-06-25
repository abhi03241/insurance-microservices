package com.java.Insurance.notification.model;

import lombok.Data;

/**
 * Model class representing the structure of an email.
 */
@Data
public class MailStructure {
    private String subject;
    private String message;
    private String applicationNumber;
    private String applicationStatus;
    private String userName;
}
