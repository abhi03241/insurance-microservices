package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.java.insurance.app.constants.BaseUrl.BASE_URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiseaseUrls {
    public static final String DISEASE_URL = BASE_URL + "/diseases";
    public static final String GET_DISEASE = DISEASE_URL + "/{diseaseId}";
    public static final String GET_ALL_DISEASE = DISEASE_URL + "/all";
    public static final String SAVE_ALL_DISEASE = DISEASE_URL + "/create/all";
    public static final String CREATE_DISEASE = DISEASE_URL + "/create";
    public static final String DELETE_DISEASE = DISEASE_URL + "/delete/{diseaseId}";
}
