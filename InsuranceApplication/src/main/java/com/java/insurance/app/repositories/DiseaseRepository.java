package com.java.insurance.app.repositories;

import com.java.insurance.app.models.Disease;
import com.java.insurance.app.models.enums.DiseaseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Integer> {
    Disease findByDiseaseType(DiseaseType diseaseType);
}
