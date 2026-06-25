package com.java.insurance.app.mail;

import lombok.Data;

@Data
public class MailStructure {
    private String subject;
    private String message;
    private String applicationNumber;
    private String applicationStatus;
    private String userName;
}
