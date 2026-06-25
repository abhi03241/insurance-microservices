package com.java.insurance.app.controllers;


import com.java.insurance.app.constants.AccessConstants;
import com.java.insurance.app.constants.DiseaseUrls;
import com.java.insurance.app.models.Disease;
import com.java.insurance.app.services.DiseaseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.java.insurance.app.constants.AppConstant.DISEASE_ID;

@RestController
@RequestMapping
@RequiredArgsConstructor
//@PreAuthorize(AccessConstants.HAS_ROLE_ADMIN)
@Tag(name = "Disease APIs")
public class DiseaseController {
    private final DiseaseService diseaseService;

    /**
     * Retrieves a disease by its ID.
     *
     * @param diseaseId The ID of the disease to retrieve.
     * @return ResponseEntity containing the retrieved disease.
     */
    @GetMapping(DiseaseUrls.GET_DISEASE)
    public ResponseEntity<Disease> getDisease(@PathVariable(DISEASE_ID) int diseaseId) {
        return ResponseEntity.ok(diseaseService.getDisease(diseaseId));
    }

    /**
     * Saves a new disease.
     *
     * @param disease The disease object to save.
     * @return ResponseEntity containing the saved disease.
     */
    @PostMapping(DiseaseUrls.CREATE_DISEASE)
    public ResponseEntity<Disease> saveDisease(@RequestBody @Valid Disease disease) {
        return new ResponseEntity<>(diseaseService.saveDisease(disease), HttpStatus.CREATED);
    }

    /**
     * Deletes a disease by its ID.
     *
     * @param diseaseId The ID of the disease to delete.
     * @return ResponseEntity indicating the success of the operation.
     */
    @DeleteMapping(DiseaseUrls.DELETE_DISEASE)
    public ResponseEntity<Void> deleteDisease(@PathVariable(DISEASE_ID) int diseaseId) {
        diseaseService.deleteDisease(diseaseId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Saves a list of diseases.
     *
     * @param diseases The list of diseases to save.
     * @return ResponseEntity containing the saved diseases.
     */
    @PostMapping(DiseaseUrls.SAVE_ALL_DISEASE)
    public ResponseEntity<List<Disease>> saveAllDiseases(@RequestBody List<Disease> diseases) {
        return ResponseEntity.ok(diseaseService.saveAllDisease(diseases));
    }

    /**
     * Retrieves all diseases.
     *
     * @return ResponseEntity containing the list of all diseases.
     */
    @GetMapping(DiseaseUrls.GET_ALL_DISEASE)
    public ResponseEntity<List<Disease>> getAllDiseases() {
        return ResponseEntity.ok(diseaseService.getAllDisease());
    }
}
