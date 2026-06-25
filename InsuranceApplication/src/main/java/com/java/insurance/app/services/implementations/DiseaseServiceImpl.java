package com.java.insurance.app.services.implementations;

import com.java.insurance.app.models.Disease;
import com.java.insurance.app.services.DiseaseService;
import com.java.insurance.app.constants.ErrorCode;
import com.java.insurance.app.exception.InsuranceCustomException;
import com.java.insurance.app.models.enums.DiseaseType;
import com.java.insurance.app.repositories.DiseaseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.java.insurance.app.constants.AppConstant.COLON;
import static com.java.insurance.app.constants.AppConstant.DISEASE_CREATED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.DISEASE_DELETED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.DISEASE_NOT_FOUND_WITH_ID;

@Service
@RequiredArgsConstructor
public class DiseaseServiceImpl implements DiseaseService {
    private static final Logger logger = LoggerFactory.getLogger(DiseaseServiceImpl.class);
    private final DiseaseRepository diseaseRepository;

    /**
     * Saves a disease in the database.
     *
     * @param disease The disease to save.
     * @return The saved disease.
     */
    @Override
    public Disease saveDisease(Disease disease) {
        Disease savedDisease = diseaseRepository.save(disease);
        logger.info(DISEASE_CREATED_WITH_ID + COLON + savedDisease.getId());
        return savedDisease;
    }

    /**
     * Retrieves a disease by its ID.
     *
     * @param diseaseId The ID of the disease to retrieve.
     * @return The retrieved disease.
     * @throws InsuranceCustomException if the disease with the specified ID is not found.
     */
    @Override
    public Disease getDisease(int diseaseId) {
        return diseaseRepository.findById(diseaseId).orElseThrow(() -> new InsuranceCustomException(DISEASE_NOT_FOUND_WITH_ID + COLON + diseaseId, ErrorCode.DISEASE_NOT_FOUND));
    }

    /**
     * Retrieves a disease by its type.
     *
     * @param diseaseType The type of the disease to retrieve.
     * @return The retrieved disease.
     */
    @Override
    public Disease getDiseaseByType(DiseaseType diseaseType) {
        return diseaseRepository.findByDiseaseType(diseaseType);
    }

    /**
     * Deletes a disease by its ID.
     *
     * @param diseaseId The ID of the disease to delete.
     */
    @Override
    public void deleteDisease(int diseaseId) {
        try {
            this.getDisease(diseaseId);
        } catch (InsuranceCustomException ie) {
            throw new InsuranceCustomException(DISEASE_NOT_FOUND_WITH_ID + COLON + diseaseId, ErrorCode.DISEASE_NOT_FOUND);
        }
        diseaseRepository.deleteById(diseaseId);
        logger.info(DISEASE_DELETED_WITH_ID + COLON + diseaseId);
    }

    /**
     * Saves a list of diseases in the database.
     *
     * @param diseases The list of diseases to save.
     * @return The saved list of diseases.
     */
    @Override
    public List<Disease> saveAllDisease(List<Disease> diseases) {
        return diseaseRepository.saveAll(diseases);
    }

    /**
     * Retrieves all diseases from the database.
     *
     * @return A list containing all diseases.
     */
    @Override
    public List<Disease> getAllDisease() {
        return diseaseRepository.findAll();
    }

}
