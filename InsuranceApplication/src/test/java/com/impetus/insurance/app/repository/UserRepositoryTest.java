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
class UserRepositoryTest {

    private final List<User> userList = new ArrayList<>();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DiseaseRepository diseaseRepository;
    private List<Role> roleList;

    @BeforeEach
    public void setUp() {
        roleList = List.of(Role.builder().roleType(RoleType.CUSTOMER).build(), Role.builder().roleType(RoleType.ADMIN).build(), Role.builder().roleType(RoleType.UNDERWRITER).build());
        roleRepository.saveAll(roleList);

        List<Disease> diseaseList = List.of(Disease.builder().diseaseType(DiseaseType.HEART_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.CANCER).build(), Disease.builder().diseaseType(DiseaseType.THYROID_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.KIDNEY_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.LIVER_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.ALZHEIMER).build(), Disease.builder().diseaseType(DiseaseType.ASTHMA).build(), Disease.builder().diseaseType(DiseaseType.DENTAL_ABSCESS).build(), Disease.builder().diseaseType(DiseaseType.DENTAL_CAVITIES).build(), Disease.builder().diseaseType(DiseaseType.DIABETES).build(), Disease.builder().diseaseType(DiseaseType.TUBERCULOSIS).build(), Disease.builder().diseaseType(DiseaseType.ORAL_CANCER).build());
        diseaseRepository.saveAll(diseaseList);

        User user1 = User.builder().name("User 1").email("user1@gmail.com").role(roleList.get(0)).address(Address.builder().city("Indore").state("MP").pinCode("123456").street("Ambikapuri").build()).applications(new ArrayList<>()).userPolicies(new ArrayList<>()).dob(LocalDate.of(2002, 8, 27)).aadharNo("123456789090").phoneNumber("7770811726").password("myPassword123").beneficiaries(new ArrayList<>()).gender(Gender.MALE).healthDetails(HealthDetails.builder().diseases(List.of(diseaseList.get(2), diseaseList.get(5))).hasRootCanalTreatment(true).hasSmokingStatus(false).hasTobaccoConsumption(false).hasAlcoholConsumption(true).hasToothExtraction(false).build()).build();
        user1.getHealthDetails().setUser(user1);

        User user2 = User.builder().name("User 2").email("user2@gmail.com").role(roleList.get(0)).address(Address.builder().city("Indore").state("MP").pinCode("123456").street("Ambikapuri").build()).applications(new ArrayList<>()).userPolicies(new ArrayList<>()).dob(LocalDate.of(2002, 8, 27)).aadharNo("123456789099").phoneNumber("7770811725").password("myPassword123").beneficiaries(new ArrayList<>()).gender(Gender.MALE).healthDetails(HealthDetails.builder().diseases(List.of(diseaseList.get(0), diseaseList.get(6))).hasRootCanalTreatment(true).hasSmokingStatus(false).hasTobaccoConsumption(false).hasAlcoholConsumption(true).hasToothExtraction(false).build()).build();
        user2.getHealthDetails().setUser(user2);

        User underwriter = User.builder().name("Underwriter 1").email("underwriter1@gmail.com").role(roleList.get(2)).address(Address.builder().city("Indore").state("MP").pinCode("123456").street("Ambikapuri").build()).applications(new ArrayList<>()).userPolicies(new ArrayList<>()).dob(LocalDate.of(2002, 8, 27)).aadharNo("123456789092").phoneNumber("7770811721").password("myPassword123").beneficiaries(new ArrayList<>()).gender(Gender.MALE).healthDetails(HealthDetails.builder().diseases(List.of(diseaseList.get(0), diseaseList.get(6))).hasRootCanalTreatment(true).hasSmokingStatus(false).hasTobaccoConsumption(false).hasAlcoholConsumption(true).hasToothExtraction(false).build()).build();
        underwriter.getHealthDetails().setUser(underwriter);

        User admin = User.builder().name("Admin 1").email("admin1@gmail.com").role(roleList.get(1)).address(Address.builder().city("Indore").state("MP").pinCode("123456").street("Ambikapuri").build()).applications(new ArrayList<>()).userPolicies(new ArrayList<>()).dob(LocalDate.of(2002, 8, 27)).aadharNo("123456789022").phoneNumber("7770811221").password("myPassword123").beneficiaries(new ArrayList<>()).gender(Gender.MALE).healthDetails(HealthDetails.builder().diseases(List.of(diseaseList.get(0), diseaseList.get(6))).hasRootCanalTreatment(true).hasSmokingStatus(false).hasTobaccoConsumption(false).hasAlcoholConsumption(true).hasToothExtraction(false).build()).build();
        admin.getHealthDetails().setUser(admin);

        userList.add(user1);
        userList.add(user2);
        userList.add(underwriter);
        userList.add(admin);
    }

    @Test
    void UserRepository_Save_ReturnsUser() {
        User user = userList.get(0);
        User savedUser = userRepository.save(user);
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isPositive();
        Assertions.assertThat(user).isEqualTo(savedUser);
    }

    @Test
    void UserRepository_FindAll_ReturnsUsersList() {
        User user1 = userList.get(0);
        User user2 = userList.get(1);

        userRepository.save(user2);
        userRepository.save(user1);

        List<User> userList = userRepository.findAll();
        Assertions.assertThat(userList).isNotNull().hasSize(2);
    }

    @Test
    void UserRepository_FindById_ReturnsUser() {
        User user = userList.get(0);
        int savedUserId = userRepository.save(user).getId();

        User retrievedUser = userRepository.findById(savedUserId).get();
        Assertions.assertThat(retrievedUser).isNotNull();
        Assertions.assertThat(user).isEqualTo(retrievedUser);
    }

    @Test
    void UserRepository_FindAllByRole_ReturnsUsersList() {
        userRepository.saveAll(userList);

        List<User> customers = userRepository.findAllByRole(roleList.get(0));

        customers.forEach(customer -> {
            Assertions.assertThat(customer).isNotNull();
            Assertions.assertThat(customer.getRole()).isEqualTo(roleList.get(0));
        });
    }

    @Test
    void UserRepository_FindByEmail_ReturnsUser() {
        User user = userList.get(0);
        User savedUser = userRepository.save(user);

        User retrievedUser = userRepository.findByEmail(savedUser.getEmail()).get();
        Assertions.assertThat(retrievedUser).isNotNull();
        Assertions.assertThat(user).isEqualTo(retrievedUser);
    }

}
