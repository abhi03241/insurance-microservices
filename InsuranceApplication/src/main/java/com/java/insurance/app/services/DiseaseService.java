package com.java.insurance.app.services;

import com.java.insurance.app.models.Disease;
import com.java.insurance.app.models.enums.DiseaseType;

import java.util.List;

public interface DiseaseService {
    Disease saveDisease(Disease disease);

    Disease getDisease(int diseaseId);

    Disease getDiseaseByType(DiseaseType diseaseType);

    void deleteDisease(int diseaseId);

    List<Disease> saveAllDisease(List<Disease> diseases);

    List<Disease> getAllDisease();
}
