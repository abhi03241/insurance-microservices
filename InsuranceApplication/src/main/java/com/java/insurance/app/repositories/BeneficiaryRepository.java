package com.java.insurance.app.repositories;

import com.java.insurance.app.models.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Integer> {
    List<Beneficiary> findByUserId(int userId);
}
