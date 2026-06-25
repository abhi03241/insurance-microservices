package com.java.insurance.app.controllers;


import com.java.insurance.app.constants.AccessConstants;
import com.java.insurance.app.constants.UnderwriterUrls;
import com.java.insurance.app.models.Application;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.enums.ApplicationStatus;
import com.java.insurance.app.services.UnderwriterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.java.insurance.app.constants.AppConstant.APPLICATION_APPROVED_SUCCESS;
import static com.java.insurance.app.constants.AppConstant.APPLICATION_REJECTED_SUCCESS;
import static com.java.insurance.app.constants.AppConstant.STATUS;

@RestController
@AllArgsConstructor
@PreAuthorize(AccessConstants.HAS_ROLE_UNDERWRITER)
@Tag(name = "Underwriter APIs")
public class UnderwriterController {

    public final UnderwriterService underwriterService;

    /**
     * Approves an application.
     *
     * @param applicationId The ID of the application to approve.
     * @return ResponseEntity with a success message.
     */
    @PutMapping(UnderwriterUrls.APPROVE_APPLICATION)
    public ResponseEntity<String> approveApplication(@PathVariable int applicationId) {
        underwriterService.approveApplications(applicationId);
        return ResponseEntity.ok(APPLICATION_APPROVED_SUCCESS);
    }

    /**
     * Rejects an application.
     *
     * @param applicationId The ID of the application to reject.
     * @return ResponseEntity with a success message.
     */
    @PutMapping(UnderwriterUrls.REJECT_APPLICATION)
    public ResponseEntity<String> rejectApplication(@PathVariable int applicationId) {
        underwriterService.rejectApplication(applicationId);
        return ResponseEntity.ok(APPLICATION_REJECTED_SUCCESS);
    }

    /**
     * Retrieves all applications assigned to the underwriter.
     *
     * @return ResponseEntity with a list of applications.
     */
    @GetMapping(UnderwriterUrls.GET_ALL_APPLICATIONS)
    public ResponseEntity<List<Application>> getAllApplicationsByUnderwriterId() {

        User underWriter = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Application> applications = underwriterService.getAllApplications(underWriter.getId());
        return ResponseEntity.ok(applications);
    }

    /**
     * Retrieves applications of an underwriter by status.
     *
     * @param status The status of the applications to retrieve.
     * @return ResponseEntity with a list of applications.
     */
    @GetMapping(UnderwriterUrls.GET_APPLICATION_OF_UNDERWRITER_BY_STATUS)
    public ResponseEntity<List<Application>> getApplicationsByStatusForUnderwriter(@PathVariable(STATUS) String status) {
        User underWriter = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationStatus applicationStatus;
        try {
            applicationStatus = ApplicationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        List<Application> applications = underwriterService.getApplicationsByStatusForUnderwriter(underWriter.getId(), applicationStatus);
        return ResponseEntity.ok(applications);
    }
}