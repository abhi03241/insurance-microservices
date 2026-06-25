package com.java.insurance.app.repository;

import com.java.insurance.app.models.Disease;
import com.java.insurance.app.models.enums.DiseaseType;
import com.java.insurance.app.repositories.DiseaseRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class DiseaseRepositoryTest {
    @Autowired
    private DiseaseRepository diseaseRepository;

    @BeforeEach
    public void setUp() {
        List<Disease> diseaseList = List.of(Disease.builder().diseaseType(DiseaseType.HEART_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.CANCER).build(), Disease.builder().diseaseType(DiseaseType.THYROID_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.KIDNEY_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.LIVER_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.ALZHEIMER).build(), Disease.builder().diseaseType(DiseaseType.ASTHMA).build(), Disease.builder().diseaseType(DiseaseType.DENTAL_ABSCESS).build(), Disease.builder().diseaseType(DiseaseType.DENTAL_CAVITIES).build(), Disease.builder().diseaseType(DiseaseType.DIABETES).build(), Disease.builder().diseaseType(DiseaseType.TUBERCULOSIS).build(), Disease.builder().diseaseType(DiseaseType.ORAL_CANCER).build());
        diseaseRepository.saveAll(diseaseList);
    }

    @Test
    public void DiseaseRepository_FindByDiseaseType_ReturnsDisease() {
        Disease heartDisease = diseaseRepository.findByDiseaseType(DiseaseType.HEART_DISEASE);
        Assertions.assertThat(heartDisease).isNotNull();
    }
}
