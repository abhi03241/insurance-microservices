package com.java.insurance.app.services;

import com.java.insurance.app.models.Application;
import com.java.insurance.app.models.enums.ApplicationStatus;

import java.util.List;

public interface UnderwriterService {
    void approveApplications(int applicationId);

    void rejectApplication(int applicationId);

    List<Application> getAllApplications(int underwriterId);

//    Application getApplicationById(int applicationId);

    List<Application> getApplicationsByStatus(ApplicationStatus status);

    List<Application> getApplicationsByStatusForUnderwriter(int underwriterId, ApplicationStatus status);

}
