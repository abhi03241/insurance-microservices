package com.java.insurance.app.controllers;

import com.java.insurance.app.constants.AccessConstants;
import com.java.insurance.app.constants.ApplicationUrls;
import com.java.insurance.app.models.Application;
import com.java.insurance.app.models.User;
import com.java.insurance.app.services.ApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@EnableMethodSecurity
@RequiredArgsConstructor
@Tag(name = "Application APIs")
public class ApplicationController {

    private final ApplicationService applicationService;

    /**
     * Retrieves all applications.
     *
     * @return ResponseEntity containing a list of all applications.
     */

    @GetMapping(ApplicationUrls.GET_ALL_APPLICATIONS)
    @PreAuthorize(AccessConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<List<Application>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    /**
     * Retrieves applications of the authenticated user.
     *
     * @return ResponseEntity containing a list of applications belonging to the authenticated user.
     */
    @PreAuthorize(AccessConstants.HAS_ROLE_CUSTOMER)
    @GetMapping(ApplicationUrls.APPLICATION_URL)
    public ResponseEntity<List<Application>> getApplicationsByUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(applicationService.getApplicationByUserId(user.getId()));
    }

    /**
     * Creates a new application for the authenticated user.
     *
     * @param application The application to create.
     * @return ResponseEntity containing the created application.
     */
    @PreAuthorize(AccessConstants.HAS_ROLE_CUSTOMER)
    @PostMapping(ApplicationUrls.CREATE_APPLICATION)
    public ResponseEntity<Application> createApplication(@RequestBody Application application) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Application app = applicationService.createApplication(application, user.getId());
        return new ResponseEntity<>(app, HttpStatus.CREATED);
    }
}
