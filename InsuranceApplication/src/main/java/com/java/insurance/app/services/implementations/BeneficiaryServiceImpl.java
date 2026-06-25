package com.java.insurance.app.services.implementations;

import com.java.insurance.app.models.Beneficiary;
import com.java.insurance.app.services.BeneficiaryService;
import com.java.insurance.app.constants.ErrorCode;
import com.java.insurance.app.exception.InsuranceCustomException;
import com.java.insurance.app.models.User;
import com.java.insurance.app.repositories.BeneficiaryRepository;
import com.java.insurance.app.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.java.insurance.app.constants.AppConstant.BENEFICIARY_CREATED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.BENEFICIARY_DELETED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.BENEFICIARY_NOT_FOUND_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.BENEFICIARY_UPDATED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.COLON;
import static com.java.insurance.app.constants.AppConstant.USER_NOT_FOUND_WITH_ID;

@Service
@AllArgsConstructor
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private static final Logger logger = LoggerFactory.getLogger(BeneficiaryServiceImpl.class);
    private final BeneficiaryRepository beneficiaryRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves all beneficiaries.
     *
     * @return A list of all beneficiaries.
     */
    @Override
    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();

    }

    /**
     * Retrieves a beneficiary by its ID.
     *
     * @param id The ID of the beneficiary to retrieve.
     * @return The beneficiary with the specified ID.
     * @throws InsuranceCustomException If no beneficiary is found with the given ID.
     */
    @Override
    public Beneficiary getBeneficiaryById(Integer id) {
        return beneficiaryRepository.findById(id).orElseThrow(() -> new InsuranceCustomException(BENEFICIARY_NOT_FOUND_WITH_ID + COLON + id, ErrorCode.BENEFICIARY_NOT_FOUND));
    }

    /**
     * Retrieves all beneficiaries belonging to a specific user.
     *
     * @param userId The ID of the user whose beneficiaries are to be retrieved.
     * @return A list of beneficiaries belonging to the specified user.
     */
    @Override
    public List<Beneficiary> findByUser(int userId) {
        return beneficiaryRepository.findByUserId(userId);
    }

    /**
     * Creates a new beneficiary for a user.
     *
     * @param beneficiary The beneficiary to create.
     * @param userId      The ID of the user for whom the beneficiary is being created.
     * @return The created beneficiary.
     * @throws InsuranceCustomException If the specified user is not found.
     */
    @Override
    public Beneficiary createBeneficiary(Beneficiary beneficiary, int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InsuranceCustomException(USER_NOT_FOUND_WITH_ID + COLON + userId, ErrorCode.USER_NOT_FOUND));
        beneficiary.setUser(user);
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);
        logger.info(BENEFICIARY_CREATED_WITH_ID + COLON + savedBeneficiary.getId());
        return savedBeneficiary;
    }

    /**
     * Updates an existing beneficiary.
     *
     * @param beneficiary The updated beneficiary information.
     * @return The updated beneficiary.
     * @throws InsuranceCustomException If the beneficiary with the given ID is not found.
     */
    @Override
    public Beneficiary updateBeneficiary(Beneficiary beneficiary) {
        Beneficiary beneficiary1 = beneficiaryRepository.findById(beneficiary.getId()).orElseThrow(() -> new InsuranceCustomException("Beneficiary not found with id : " + beneficiary.getId(), "BENEFICIARY_NOT_FOUND"));
        beneficiary1.setBeneficiaryName(beneficiary.getBeneficiaryName());
        beneficiary1.setDob(beneficiary.getDob());
        beneficiary1.setRelation(beneficiary.getRelation());
        Beneficiary updatedBeneficiary = beneficiaryRepository.save(beneficiary1);
        logger.info(BENEFICIARY_UPDATED_WITH_ID + COLON + updatedBeneficiary.getId());
        return beneficiary1;
    }

    /**
     * Deletes a beneficiary by its ID.
     *
     * @param beneficiaryId The ID of the beneficiary to delete.
     */
    @Override
    public void deleteBeneficiary(int beneficiaryId) {
        try {
            this.getBeneficiaryById(beneficiaryId);
        } catch (InsuranceCustomException ie) {
            throw new InsuranceCustomException(BENEFICIARY_NOT_FOUND_WITH_ID + COLON + beneficiaryId, ErrorCode.BENEFICIARY_NOT_FOUND);
        }
        beneficiaryRepository.deleteById(beneficiaryId);
        logger.info(BENEFICIARY_DELETED_WITH_ID + COLON + beneficiaryId);
    }
}
