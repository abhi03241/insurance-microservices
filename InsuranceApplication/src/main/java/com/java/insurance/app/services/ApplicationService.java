package com.java.insurance.app.services;

import com.java.insurance.app.models.Application;

import java.util.List;

public interface ApplicationService {

    List<Application> getAllApplications();

    List<Application> getApplicationByUserId(int userId);

    Application createApplication(Application application, int userId);

    void deleteApplication(int applicationId);
}
