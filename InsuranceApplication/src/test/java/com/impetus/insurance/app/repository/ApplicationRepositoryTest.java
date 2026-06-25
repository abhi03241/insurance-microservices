package com.java.insurance.app.repository;

import com.java.insurance.app.models.Address;
import com.java.insurance.app.models.Application;
import com.java.insurance.app.models.Disease;
import com.java.insurance.app.models.HealthDetails;
import com.java.insurance.app.models.Policy;
import com.java.insurance.app.models.PolicyRule;
import com.java.insurance.app.models.Premium;
import com.java.insurance.app.models.Role;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.enums.ApplicationStatus;
import com.java.insurance.app.models.enums.DiseaseType;
import com.java.insurance.app.models.enums.Gender;
import com.java.insurance.app.models.enums.PolicyStatus;
import com.java.insurance.app.models.enums.PolicyType;
import com.java.insurance.app.models.enums.PremiumType;
import com.java.insurance.app.models.enums.RoleType;
import com.java.insurance.app.repositories.ApplicationRepository;
import com.java.insurance.app.repositories.DiseaseRepository;
import com.java.insurance.app.repositories.PolicyRepository;
import com.java.insurance.app.repositories.RoleRepository;
import com.java.insurance.app.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
class ApplicationRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private DiseaseRepository diseaseRepository;
    @Autowired
    private PolicyRepository policyRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    private User user;
    private Policy policy;

    @BeforeEach
    public void setUp() {
        List<Role> roleList = List.of(Role.builder().roleType(RoleType.CUSTOMER).build(), Role.builder().roleType(RoleType.ADMIN).build(), Role.builder().roleType(RoleType.UNDERWRITER).build());
        roleRepository.saveAll(roleList);

        List<Disease> diseaseList = List.of(Disease.builder().diseaseType(DiseaseType.HEART_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.CANCER).build(), Disease.builder().diseaseType(DiseaseType.THYROID_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.KIDNEY_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.LIVER_DISEASE).build(), Disease.builder().diseaseType(DiseaseType.ALZHEIMER).build(), Disease.builder().diseaseType(DiseaseType.ASTHMA).build(), Disease.builder().diseaseType(DiseaseType.DENTAL_ABSCESS).build(), Disease.builder().diseaseType(DiseaseType.DENTAL_CAVITIES).build(), Disease.builder().diseaseType(DiseaseType.DIABETES).build(), Disease.builder().diseaseType(DiseaseType.TUBERCULOSIS).build(), Disease.builder().diseaseType(DiseaseType.ORAL_CANCER).build());
        diseaseRepository.saveAll(diseaseList);

        policy = Policy.builder().policyName("Policy 1").policyDesc("This is Policy").policyStatus(PolicyStatus.ACTIVE).benefits("Policy benefits").expiry(LocalDate.of(2030, 12, 31)).validityInMonths(20).policyRule(PolicyRule.builder().numberOfBeneficiaries(4).minAge(20).maxAge(80).minHealthScore(76).uncoveredDiseases(List.of(diseaseList.get(0), diseaseList.get(9))).build()).policyType(PolicyType.LIFE).premium(Premium.builder().premiumAmount(235d).premiumType(PremiumType.MONTHLY).build()).build();

        user = User.builder().name("User 1").email("user1@gmail.com").role(roleList.get(0)).address(Address.builder().city("Indore").state("MP").pinCode("123456").street("Ambikapuri").build()).applications(new ArrayList<>()).userPolicies(new ArrayList<>()).dob(LocalDate.of(2002, 8, 27)).aadharNo("123456789090").phoneNumber("7770811726").password("myPassword123").beneficiaries(new ArrayList<>()).gender(Gender.MALE).healthDetails(HealthDetails.builder().diseases(new ArrayList<>()).hasRootCanalTreatment(true).hasSmokingStatus(false).hasTobaccoConsumption(false).hasAlcoholConsumption(true).hasToothExtraction(false).build()).build();
        user.getHealthDetails().setUser(user);
    }

    @Test
    void ApplicationRepository_FindByUserId_ReturnsApplicationsList() {
        User savedUser = userRepository.save(user);
        policyRepository.save(policy);
        List<Policy> policyList = new ArrayList<>();
        policyList.add(policy);
        Application application = Application.builder().user(user).policies(policyList).applicationStatus(ApplicationStatus.NEW).creationDateTime(LocalDateTime.now()).totalMonthlyPremium(policy.getPremium().getPremiumAmount()).build();
        applicationRepository.save(application);
        List<Application> userApplications = applicationRepository.findByUserId(savedUser.getId());
        Assertions.assertThat(userApplications).isNotNull().isNotEmpty();
    }

}