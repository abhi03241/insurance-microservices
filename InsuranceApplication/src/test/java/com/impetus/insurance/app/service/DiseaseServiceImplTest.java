package com.java.insurance.app.service;

import com.java.insurance.app.exception.InsuranceCustomException;
import com.java.insurance.app.models.Address;
import com.java.insurance.app.models.Application;
import com.java.insurance.app.models.Beneficiary;
import com.java.insurance.app.models.Disease;
import com.java.insurance.app.models.HealthDetails;
import com.java.insurance.app.models.Role;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.UserPolicy;
import com.java.insurance.app.models.enums.DiseaseType;
import com.java.insurance.app.repositories.DiseaseRepository;
import com.java.insurance.app.repositories.RoleRepository;
import com.java.insurance.app.repositories.UserRepository;
import com.java.insurance.app.services.DiseaseService;
import com.java.insurance.app.services.RoleService;
import com.java.insurance.app.services.UserService;
import com.java.insurance.app.services.implementations.DiseaseServiceImpl;
import com.java.insurance.app.services.implementations.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.java.insurance.app.models.enums.Gender.MALE;
import static com.java.insurance.app.models.enums.RoleType.CUSTOMER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class DiseaseServiceImplTest {

    AutoCloseable autoCloseable;
    User user;
    @Mock
    private DiseaseService diseaseService;
    @Mock
    private DiseaseServiceImpl diseaseServiceImpl;
    @Mock
    private DiseaseRepository diseaseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        Role role = new Role();
        role.setRoleType(CUSTOMER);
        List<Application> apps = new ArrayList<>();
        List<UserPolicy> policies = new ArrayList<>();
        List<Beneficiary> ben = new ArrayList<>();
        HealthDetails healthDetails = new HealthDetails();
        healthDetails.setHasAlcoholConsumption(true);
        healthDetails.setHasSmokingStatus(true);
        healthDetails.setHasRootCanalTreatment(false);
        healthDetails.setHasToothExtraction(true);
        healthDetails.setHasTobaccoConsumption(false);
        userService = new UserServiceImpl(userRepository, roleService, diseaseService);
        diseaseService = new DiseaseServiceImpl(diseaseRepository);
        user = new User(1, MALE, "9876543211", "123456789012", LocalDate.of(2002, 5, 3), "AMIT", "AMIT", "amit@gmail.com", new Address("MP", "Indore", "452001", "Rajiv Gandhi"), role, apps, policies, ben, healthDetails);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }


    @Test
    void testDeleteDisease() {
        // Given
        Disease disease = new Disease(); // Create a dummy disease
        disease.setId(1);

        // Mock the behavior of diseaseRepository
        doNothing().when(diseaseRepository).deleteById(any());

        // When
        try {
            diseaseService.deleteDisease(1);
        }
        catch (InsuranceCustomException ie) {
            //Do nothing
        }

        // Then
        verify(diseaseRepository, times(0)).deleteById(1);
    }

    @Test
    void testGetAllDisease() {
        // Given
        Disease disease = new Disease();
        disease.setId(1);
        disease.setDiseaseType(DiseaseType.ASTHMA);

        List<Disease> diseases = Collections.singletonList(disease);

        // Mock the behavior of diseaseRepository
        when(diseaseRepository.findAll()).thenReturn(diseases);

        // When
        List<Disease> result = diseaseService.getAllDisease();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.get(0).getId()).isEqualTo(disease.getId());
        assertThat(result.get(0).getDiseaseType()).isEqualTo(DiseaseType.ASTHMA);
    }

    @Test
    void testSaveDisease() {
        // Given
        Disease disease = new Disease();
        disease.setId(1);
        disease.setDiseaseType(DiseaseType.ASTHMA);

        // Mock the behavior of diseaseRepository
        when(diseaseRepository.save(disease)).thenReturn(disease);

        // When
        Disease savedDisease = diseaseService.saveDisease(disease);

        // Then
        assertThat(savedDisease).isNotNull();
        assertThat(savedDisease.getId()).isEqualTo(1);
        assertThat(savedDisease.getDiseaseType()).isEqualTo(DiseaseType.ASTHMA);
    }

    @Test
    void testGetDisease() {
        int diseaseId = 1;
        Disease disease = new Disease();
        disease.setId(diseaseId);
        disease.setDiseaseType(DiseaseType.ASTHMA);

        when(diseaseRepository.findById(diseaseId)).thenReturn(Optional.of(disease));

        // When
        Disease retrievedDisease = diseaseService.getDisease(diseaseId);

        // Then
        assertThat(retrievedDisease).isNotNull();
        assertThat(retrievedDisease.getId()).isEqualTo(diseaseId);
        assertThat(retrievedDisease.getDiseaseType()).isEqualTo(DiseaseType.ASTHMA);
    }

    @Test
    void testGetDiseaseByType() {
        // Given
        DiseaseType diseaseType = DiseaseType.ASTHMA;
        Disease disease = new Disease();
        disease.setId(1);
        disease.setDiseaseType(diseaseType);

        when(diseaseRepository.findByDiseaseType(diseaseType)).thenReturn(disease);

        // When
        Disease retrievedDisease = diseaseService.getDiseaseByType(diseaseType);

        // Then
        assertThat(retrievedDisease).isNotNull();
        assertThat(retrievedDisease.getDiseaseType()).isEqualTo(diseaseType);
    }

    @Test
    void testSaveAllDisease() {
        List<Disease> diseases = new ArrayList<>();
        Disease disease1 = new Disease();
        disease1.setId(1);
        disease1.setDiseaseType(DiseaseType.ASTHMA);
        diseases.add(disease1);

        Disease disease2 = new Disease();
        disease2.setId(2);
        disease2.setDiseaseType(DiseaseType.CANCER);
        diseases.add(disease2);

        // Mock the behavior of diseaseRepository
        when(diseaseRepository.saveAll(diseases)).thenReturn(diseases);

        // When
        List<Disease> savedDiseases = diseaseService.saveAllDisease(diseases);

        // Then
        assertThat(savedDiseases).isNotNull();
        assertThat(savedDiseases.get(0).getId()).isEqualTo(1);
        assertThat(savedDiseases.get(0).getDiseaseType()).isEqualTo(DiseaseType.ASTHMA);
        assertThat(savedDiseases.get(1).getId()).isEqualTo(2);
        assertThat(savedDiseases.get(1).getDiseaseType()).isEqualTo(DiseaseType.CANCER);
    }
}
