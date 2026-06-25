package com.java.insurance.app.repository;

import com.java.insurance.app.models.Address;
import com.java.insurance.app.models.Disease;
import com.java.insurance.app.models.HealthDetails;
import com.java.insurance.app.models.Role;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.enums.DiseaseType;
import com.java.insurance.app.models.enums.Gender;
import com.java.insurance.app.models.enums.RoleType;
import com.java.insurance.app.repositories.DiseaseRepository;
import com.java.insurance.app.repositories.RoleRepository;
import com.java.insurance.app.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class RoleRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DiseaseRepository diseaseRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        List<Role> roleList = List.of(Role.builder().roleType(RoleType.CUSTOMER).build(), Role.builder().roleType(RoleType.ADMIN).build(), Role.builder().roleType(RoleType.UNDERWRITER).build());
        roleRepository.saveAll(roleList);

        List<Disease> diseaseList = List.of(Disease.builder().diseaseType(DiseaseType.HEART_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.CANCER).build(), Disease.builder().diseaseType(DiseaseType.THYROID_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.KIDNEY_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.LIVER_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.ALZHEIMER).build(), Disease.builder().diseaseType(DiseaseType.ASTHMA).build(), Disease.builder().diseaseType(DiseaseType.DENTAL_ABSCESS).build(), Disease.builder().diseaseType(DiseaseType.DENTAL_CAVITIES).build(), Disease.builder().diseaseType(DiseaseType.DIABETES).build(), Disease.builder().diseaseType(DiseaseType.TUBERCULOSIS).build(), Disease.builder().diseaseType(DiseaseType.ORAL_CANCER).build());
        diseaseRepository.saveAll(diseaseList);

        user = User.builder().name("User 1").email("user1@gmail.com").role(roleList.get(0)).address(Address.builder().city("Indore").state("MP").pinCode("123456").street("Ambikapuri").build()).applications(new ArrayList<>()).userPolicies(new ArrayList<>()).dob(LocalDate.of(2002, 8, 27)).aadharNo("123456789090").phoneNumber("7770811726").password("myPassword123").beneficiaries(new ArrayList<>()).gender(Gender.MALE).healthDetails(HealthDetails.builder().diseases(List.of(diseaseList.get(2), diseaseList.get(5))).hasRootCanalTreatment(true).hasSmokingStatus(false).hasTobaccoConsumption(false).hasAlcoholConsumption(true).hasToothExtraction(false).build()).build();
        user.getHealthDetails().setUser(user);
    }

    @Test
    public void RoleRepository_FindByRoleType_ReturnsRole() {
        RoleType customerRole = RoleType.CUSTOMER;
        Role retreivedRole = roleRepository.findByRoleType(customerRole);
        Assertions.assertThat(retreivedRole).isNotNull();
        Assertions.assertThat(retreivedRole.getRoleType()).isEqualTo(RoleType.CUSTOMER);
    }

    @Test
    public void RoleRepository_FindRoleByUserId_ReturnsRole() {
        User user1 = user;
        User savedUser = userRepository.save(user1);
        Role retreivedRole = roleRepository.findRoleByUserId(savedUser.getId());
        Assertions.assertThat(retreivedRole).isNotNull();
    }
}
