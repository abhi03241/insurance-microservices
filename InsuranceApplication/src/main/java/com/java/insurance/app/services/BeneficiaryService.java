package com.java.insurance.app.services;

import com.java.insurance.app.models.Beneficiary;

import java.util.List;

public interface BeneficiaryService {

    List<Beneficiary> getAllBeneficiaries();

    Beneficiary getBeneficiaryById(Integer id);

    List<Beneficiary> findByUser(int userId);

    Beneficiary createBeneficiary(Beneficiary beneficiary, int userId);

    Beneficiary updateBeneficiary(Beneficiary beneficiary);

    void deleteBeneficiary(int beneficiaryId);
}
