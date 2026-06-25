package com.java.insurance.app.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.insurance.app.constants.DiseaseUrls;
import com.java.insurance.app.controllers.DiseaseController;
import com.java.insurance.app.models.Disease;
import com.java.insurance.app.models.enums.DiseaseType;
import com.java.insurance.app.services.DiseaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DiseaseControlletTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private DiseaseService diseaseService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new DiseaseController(diseaseService)).build();
    }

    @Test
    void getDisease_Test_SUCCESS() throws Exception {
        // Create a sample disease object
        Disease disease = new Disease();
        disease.setId(1);
        disease.setDiseaseType(DiseaseType.THYROID_DISEASE);

        // Mock the behavior of the diseaseService to return the sample disease
        when(diseaseService.getDisease(1)).thenReturn(disease);

        // Perform GET request to /api/disease/1
        mockMvc.perform(MockMvcRequestBuilders.get(DiseaseUrls.GET_DISEASE,1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void saveDisease_Test_SUCCES() throws Exception {
        // Create a sample disease object
        Disease disease = new Disease();
        disease.setId(1);
        disease.setDiseaseType(DiseaseType.CANCER);

        // Mock the behavior of the diseaseService to return the sample disease
        when(diseaseService.saveDisease(any(Disease.class))).thenReturn(disease);

        // Convert the disease object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String diseaseJson = objectMapper.writeValueAsString(disease);

        // Perform POST request to /api/disease/create
        mockMvc.perform(MockMvcRequestBuilders.post(DiseaseUrls.CREATE_DISEASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(diseaseJson))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteDisease_Test_SUCCESS() throws Exception {
        int diseaseId = 1;
        doNothing().when(diseaseService).deleteDisease(diseaseId);
        mockMvc.perform(MockMvcRequestBuilders.delete(DiseaseUrls.DELETE_DISEASE, diseaseId))
                .andExpect(status().isNoContent());
    }

    @Test
    void saveAllDiseases_Test_SUCCESS() throws Exception {
        List<Disease> diseases = Arrays.asList(new Disease(), new Disease());
        when(diseaseService.saveAllDisease(diseases)).thenReturn(diseases);
        String jsonRequest = objectMapper.writeValueAsString(diseases);
        mockMvc.perform(MockMvcRequestBuilders.post(DiseaseUrls.SAVE_ALL_DISEASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

    }

    @Test
    void getAllDiseases_Test_SUCCESS() throws Exception {
        List<Disease> diseases = Arrays.asList(new Disease(), new Disease());
        when(diseaseService.getAllDisease()).thenReturn(diseases);
        mockMvc.perform(MockMvcRequestBuilders.get(DiseaseUrls.GET_ALL_DISEASE))
                .andExpect(status().isOk());
    }

    /* Failure Test */

    @Test
    public void getDisease_Test_FAILURE() throws Exception {
        int diseaseId = 1;
        when(diseaseService.getDisease(diseaseId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(DiseaseUrls.GET_DISEASE, diseaseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void saveDisease_Test_FAILURE() throws Exception {
        Disease invalidDisease = new Disease();
        mockMvc.perform(MockMvcRequestBuilders
                        .post(DiseaseUrls.CREATE_DISEASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDisease)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllDiseases_Test_FAILURE() throws Exception {
        when(diseaseService.getAllDisease()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders
                        .get(DiseaseUrls.GET_ALL_DISEASE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());     }




}
