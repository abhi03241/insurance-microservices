package com.java.insurance.app.controllers;

import com.java.insurance.app.constants.AccessConstants;
import com.java.insurance.app.constants.BeneficiaryUrls;
import com.java.insurance.app.models.Beneficiary;
import com.java.insurance.app.models.User;
import com.java.insurance.app.services.BeneficiaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@EnableMethodSecurity
@PreAuthorize(AccessConstants.HAS_ROLE_CUSTOMER)
@Tag(name = "Beneficiary APIs")
public class BeneficiaryController {
    private final BeneficiaryService beneficiaryService;

    /**
     * Retrieves a beneficiary by ID.
     *
     * @param beneficiaryId The ID of the beneficiary to retrieve.
     * @return ResponseEntity containing the retrieved beneficiary.
     */
    @GetMapping(BeneficiaryUrls.GET_BENEFICIARY)
    public ResponseEntity<Beneficiary> getBeneficiaryById(@PathVariable Integer beneficiaryId) {
        Beneficiary beneficiary = beneficiaryService.getBeneficiaryById(beneficiaryId);
        return new ResponseEntity<>(beneficiary, HttpStatus.OK);
    }

    /**
     * Retrieves all beneficiaries of the currently authenticated user.
     *
     * @return ResponseEntity containing a list of beneficiaries.
     */
    @GetMapping(BeneficiaryUrls.GET_BENEFICIARY_OF_USER)
    public ResponseEntity<List<Beneficiary>> getAllBeneficiariesByUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Beneficiary> beneficiaries = beneficiaryService.findByUser(user.getId());
        return new ResponseEntity<>(beneficiaries, HttpStatus.OK);
    }

    /**
     * Adds a new beneficiary.
     *
     * @param beneficiary The beneficiary to add.
     * @return ResponseEntity containing the created beneficiary.
     */
    @PostMapping(BeneficiaryUrls.ADD_BENEFICIARY)
    public ResponseEntity<Beneficiary> createBeneficiary(@RequestBody @Valid Beneficiary beneficiary) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Beneficiary createdBeneficiary = beneficiaryService.createBeneficiary(beneficiary, user.getId());
        return new ResponseEntity<>(createdBeneficiary, HttpStatus.CREATED);
    }

    /**
     * Updates an existing beneficiary.
     *
     * @param beneficiary The beneficiary to update.
     * @return ResponseEntity containing the updated beneficiary.
     */
    @PutMapping(BeneficiaryUrls.UPDATE_BENEFICIARY)
    public ResponseEntity<Beneficiary> updateBeneficiary(@RequestBody @Valid Beneficiary beneficiary) {
        try {
            Beneficiary updatedBeneficiary = beneficiaryService.updateBeneficiary(beneficiary);
            return new ResponseEntity<>(updatedBeneficiary, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes a beneficiary by ID.
     *
     * @param beneficiaryId The ID of the beneficiary to delete.
     * @return ResponseEntity indicating the success of the operation.
     */
    @DeleteMapping(BeneficiaryUrls.REMOVE_BENEFICIARY)
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Integer beneficiaryId) {
        beneficiaryService.deleteBeneficiary(beneficiaryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
