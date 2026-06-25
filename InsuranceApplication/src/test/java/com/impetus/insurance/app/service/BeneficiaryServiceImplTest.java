package com.java.insurance.app.service;

import com.java.insurance.app.exception.InsuranceCustomException;
import com.java.insurance.app.models.Address;
import com.java.insurance.app.models.Beneficiary;
import com.java.insurance.app.models.Role;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.enums.Gender;
import com.java.insurance.app.models.enums.Relation;
import com.java.insurance.app.models.enums.RoleType;
import com.java.insurance.app.repositories.BeneficiaryRepository;
import com.java.insurance.app.repositories.UserRepository;
import com.java.insurance.app.services.implementations.BeneficiaryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ContextConfiguration(classes = {BeneficiaryServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class BeneficiaryServiceImplTest {
    @MockBean
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private BeneficiaryServiceImpl beneficiaryServiceImp;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link BeneficiaryServiceImpl#getAllBeneficiaries()}
     */
    @Test
    void testGetAllBeneficiaries() {
        // Arrange
        ArrayList<Beneficiary> beneficiaryList = new ArrayList<>();
        when(beneficiaryRepository.findAll()).thenReturn(beneficiaryList);

        // Act
        List<Beneficiary> actualAllBeneficiaries = beneficiaryServiceImp.getAllBeneficiaries();

        // Assert
        verify(beneficiaryRepository).findAll();
        assertTrue(actualAllBeneficiaries.isEmpty());
        assertSame(beneficiaryList, actualAllBeneficiaries);
    }

    /**
     * Method under test: {@link BeneficiaryServiceImpl#getAllBeneficiaries()}
     */
    @Test
    void testGetAllBeneficiaries2() {
        // Arrange
        when(beneficiaryRepository.findAll()).thenThrow(new InsuranceCustomException("An error occurred", "An error occurred"));

        // Act and Assert
        assertThrows(InsuranceCustomException.class, () -> beneficiaryServiceImp.getAllBeneficiaries());
        verify(beneficiaryRepository).findAll();
    }

    /**
     * Method under test: {@link BeneficiaryServiceImpl#getBeneficiaryById(Integer)}
     */
    @Test
    void testGetBeneficiaryById() {
        // Arrange
        Address address = new Address();
        address.setCity("Oxford");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        Role role = new Role();
        role.setId(1);
        role.setRoleType(RoleType.CUSTOMER);

        User user = new User();
        user.setAadharNo("Aadhar No");
        user.setAddress(address);
        user.setApplications(new ArrayList<>());
        user.setBeneficiaries(new ArrayList<>());
        user.setDob(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setGender(Gender.MALE);
        user.setId(1);
        user.setName("Bella");
        user.setPassword("iloveyou");
        user.setPhoneNumber("6625550144");
        user.setRole(role);
        user.setUserPolicies(new ArrayList<>());

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryName("Bella");
        beneficiary.setDob(LocalDate.of(1970, 1, 1));
        beneficiary.setId(1);
        beneficiary.setRelation(Relation.PARENTS);
        beneficiary.setUser(user);
        Optional<Beneficiary> ofResult = Optional.of(beneficiary);
        when(beneficiaryRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act
        Beneficiary actualBeneficiaryById = beneficiaryServiceImp.getBeneficiaryById(1);

        // Assert
        verify(beneficiaryRepository).findById(eq(1));
        assertSame(beneficiary, actualBeneficiaryById);
    }

    /**
     * Method under test: {@link BeneficiaryServiceImpl#getBeneficiaryById(Integer)}
     */
    @Test
    void testGetBeneficiaryById2() {
        // Arrange
        Optional<Beneficiary> emptyResult = Optional.empty();
        when(beneficiaryRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(InsuranceCustomException.class, () -> beneficiaryServiceImp.getBeneficiaryById(1));
        verify(beneficiaryRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link BeneficiaryServiceImpl#getBeneficiaryById(Integer)}
     */
    @Test
    void testGetBeneficiaryById3() {
        // Arrange
        when(beneficiaryRepository.findById(Mockito.<Integer>any())).thenThrow(new InsuranceCustomException("An error occurred", "An error occurred"));

        // Act and Assert
        assertThrows(InsuranceCustomException.class, () -> beneficiaryServiceImp.getBeneficiaryById(1));
        verify(beneficiaryRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link BeneficiaryServiceImpl#findByUser(int)}
     */
    @Test
    void testFindByUser() {
        // Arrange
        ArrayList<Beneficiary> beneficiaryList = new ArrayList<>();
        when(beneficiaryRepository.findByUserId(anyInt())).thenReturn(beneficiaryList);

        // Act
        List<Beneficiary> actualFindByUserResult = beneficiaryServiceImp.findByUser(1);

        // Assert
        verify(beneficiaryRepository).findByUserId(eq(1));
        assertTrue(actualFindByUserResult.isEmpty());
        assertSame(beneficiaryList, actualFindByUserResult);
    }

    /**
     * Method under test: {@link BeneficiaryServiceImpl#findByUser(int)}
     */
    @Test
    void testFindByUser2() {
        // Arrange
        when(beneficiaryRepository.findByUserId(anyInt())).thenThrow(new InsuranceCustomException("An error occurred", "An error occurred"));

        // Act and Assert
        assertThrows(InsuranceCustomException.class, () -> beneficiaryServiceImp.findByUser(1));
        verify(beneficiaryRepository).findByUserId(eq(1));
    }

    /**
     * Method under test:
     * {@link BeneficiaryServiceImpl#createBeneficiary(Beneficiary, int)}
     */
    @Test
    void testCreateBeneficiary() {
        // Arrange
        Address address = new Address();
        address.setCity("Oxford");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        Role role = new Role();
        role.setId(1);
        role.setRoleType(RoleType.CUSTOMER);

        User user = new User();
        user.setAadharNo("Aadhar No");
        user.setAddress(address);
        user.setApplications(new ArrayList<>());
        user.setBeneficiaries(new ArrayList<>());
        user.setDob(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setGender(Gender.MALE);
        user.setId(1);
        user.setName("Bella");
        user.setPassword("iloveyou");
        user.setPhoneNumber("6625550144");
        user.setRole(role);
        user.setUserPolicies(new ArrayList<>());

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryName("Bella");
        beneficiary.setDob(LocalDate.of(1970, 1, 1));
        beneficiary.setId(1);
        beneficiary.setRelation(Relation.PARENTS);
        beneficiary.setUser(user);
        when(beneficiaryRepository.save(Mockito.any())).thenReturn(beneficiary);

        Address address2 = new Address();
        address2.setCity("Oxford");
        address2.setPinCode("Pin Code");
        address2.setState("MD");
        address2.setStreet("Street");

        Role role2 = new Role();
        role2.setId(1);
        role2.setRoleType(RoleType.CUSTOMER);

        User user2 = new User();
        user2.setAadharNo("Aadhar No");
        user2.setAddress(address2);
        user2.setApplications(new ArrayList<>());
        user2.setBeneficiaries(new ArrayList<>());
        user2.setDob(LocalDate.of(1970, 1, 1));
        user2.setEmail("jane.doe@example.org");
        user2.setGender(Gender.MALE);
        user2.setId(1);
        user2.setName("Bella");
        user2.setPassword("iloveyou");
        user2.setPhoneNumber("6625550144");
        user2.setRole(role2);
        user2.setUserPolicies(new ArrayList<>());
        Optional<User> ofResult = Optional.of(user2);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        Address address3 = new Address();
        address3.setCity("Oxford");
        address3.setPinCode("Pin Code");
        address3.setState("MD");
        address3.setStreet("Street");

        Role role3 = new Role();
        role3.setId(1);
        role3.setRoleType(RoleType.CUSTOMER);

        User user3 = new User();
        user3.setAadharNo("Aadhar No");
        user3.setAddress(address3);
        user3.setApplications(new ArrayList<>());
        user3.setBeneficiaries(new ArrayList<>());
        user3.setDob(LocalDate.of(1970, 1, 1));
        user3.setEmail("jane.doe@example.org");
        user3.setGender(Gender.MALE);
        user3.setId(1);
        user3.setName("Bella");
        user3.setPassword("iloveyou");
        user3.setPhoneNumber("6625550144");
        user3.setRole(role3);
        user3.setUserPolicies(new ArrayList<>());

        Beneficiary beneficiary2 = new Beneficiary();
        beneficiary2.setBeneficiaryName("Bella");
        beneficiary2.setDob(LocalDate.of(1970, 1, 1));
        beneficiary2.setId(1);
        beneficiary2.setRelation(Relation.PARENTS);
        beneficiary2.setUser(user3);

        // Act
        Beneficiary actualCreateBeneficiaryResult = beneficiaryServiceImp.createBeneficiary(beneficiary2, 1);

        // Assert
        verify(userRepository).findById(eq(1));
        verify(beneficiaryRepository).save(isA(Beneficiary.class));
        assertTrue(beneficiary2.getUser().isAccountNonExpired());
        assertSame(beneficiary, actualCreateBeneficiaryResult);
    }

    /**
     * Method under test:
     * {@link BeneficiaryServiceImpl#createBeneficiary(Beneficiary, int)}
     */
    @Test
    void testCreateBeneficiary2() {
        // Arrange
        when(beneficiaryRepository.save(Mockito.any())).thenThrow(new InsuranceCustomException("An error occurred", "An error occurred"));

        Address address = new Address();
        address.setCity("Oxford");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        Role role = new Role();
        role.setId(1);
        role.setRoleType(RoleType.CUSTOMER);

        User user = new User();
        user.setAadharNo("Aadhar No");
        user.setAddress(address);
        user.setApplications(new ArrayList<>());
        user.setBeneficiaries(new ArrayList<>());
        user.setDob(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setGender(Gender.MALE);
        user.setId(1);
        user.setName("Bella");
        user.setPassword("iloveyou");
        user.setPhoneNumber("6625550144");
        user.setRole(role);
        user.setUserPolicies(new ArrayList<>());
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        Address address2 = new Address();
        address2.setCity("Oxford");
        address2.setPinCode("Pin Code");
        address2.setState("MD");
        address2.setStreet("Street");

        Role role2 = new Role();
        role2.setId(1);
        role2.setRoleType(RoleType.CUSTOMER);

        User user2 = new User();
        user2.setAadharNo("Aadhar No");
        user2.setAddress(address2);
        user2.setApplications(new ArrayList<>());
        user2.setBeneficiaries(new ArrayList<>());
        user2.setDob(LocalDate.of(1970, 1, 1));
        user2.setEmail("jane.doe@example.org");
        user2.setGender(Gender.MALE);
        user2.setId(1);
        user2.setName("Bella");
        user2.setPassword("iloveyou");
        user2.setPhoneNumber("6625550144");
        user2.setRole(role2);
        user2.setUserPolicies(new ArrayList<>());

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryName("Bella");
        beneficiary.setDob(LocalDate.of(1970, 1, 1));
        beneficiary.setId(1);
        beneficiary.setRelation(Relation.PARENTS);
        beneficiary.setUser(user2);

        // Act and Assert
        assertThrows(InsuranceCustomException.class, () -> beneficiaryServiceImp.createBeneficiary(beneficiary, 1));
        verify(userRepository).findById(eq(1));
        verify(beneficiaryRepository).save(isA(Beneficiary.class));
    }

    /**
     * Method under test:
     * {@link BeneficiaryServiceImpl#updateBeneficiary(Beneficiary)}
     */
    @Test
    void testUpdateBeneficiary() {
        // Arrange
        Address address = new Address();
        address.setCity("Oxford");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        Role role = new Role();
        role.setId(1);
        role.setRoleType(RoleType.CUSTOMER);

        User user = new User();
        user.setAadharNo("Aadhar No");
        user.setAddress(address);
        user.setApplications(new ArrayList<>());
        user.setBeneficiaries(new ArrayList<>());
        user.setDob(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setGender(Gender.MALE);
        user.setId(1);
        user.setName("Bella");
        user.setPassword("iloveyou");
        user.setPhoneNumber("6625550144");
        user.setRole(role);
        user.setUserPolicies(new ArrayList<>());

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryName("Bella");
        beneficiary.setDob(LocalDate.of(1970, 1, 1));
        beneficiary.setId(1);
        beneficiary.setRelation(Relation.PARENTS);
        beneficiary.setUser(user);
        Optional<Beneficiary> ofResult = Optional.of(beneficiary);

        Address address2 = new Address();
        address2.setCity("Oxford");
        address2.setPinCode("Pin Code");
        address2.setState("MD");
        address2.setStreet("Street");

        Role role2 = new Role();
        role2.setId(1);
        role2.setRoleType(RoleType.CUSTOMER);

        User user2 = new User();
        user2.setAadharNo("Aadhar No");
        user2.setAddress(address2);
        user2.setApplications(new ArrayList<>());
        user2.setBeneficiaries(new ArrayList<>());
        user2.setDob(LocalDate.of(1970, 1, 1));
        user2.setEmail("jane.doe@example.org");
        user2.setGender(Gender.MALE);
        user2.setId(1);
        user2.setName("Bella");
        user2.setPassword("iloveyou");
        user2.setPhoneNumber("6625550144");
        user2.setRole(role2);
        user2.setUserPolicies(new ArrayList<>());

        Beneficiary beneficiary2 = new Beneficiary();
        beneficiary2.setBeneficiaryName("Bella");
        beneficiary2.setDob(LocalDate.of(1970, 1, 1));
        beneficiary2.setId(1);
        beneficiary2.setRelation(Relation.PARENTS);
        beneficiary2.setUser(user2);
        when(beneficiaryRepository.save(Mockito.any())).thenReturn(beneficiary2);
        when(beneficiaryRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        Address address3 = new Address();
        address3.setCity("Oxford");
        address3.setPinCode("Pin Code");
        address3.setState("MD");
        address3.setStreet("Street");

        Role role3 = new Role();
        role3.setId(1);
        role3.setRoleType(RoleType.CUSTOMER);

        User user3 = new User();
        user3.setAadharNo("Aadhar No");
        user3.setAddress(address3);
        user3.setApplications(new ArrayList<>());
        user3.setBeneficiaries(new ArrayList<>());
        user3.setDob(LocalDate.of(1970, 1, 1));
        user3.setEmail("jane.doe@example.org");
        user3.setGender(Gender.MALE);
        user3.setId(1);
        user3.setName("Bella");
        user3.setPassword("iloveyou");
        user3.setPhoneNumber("6625550144");
        user3.setRole(role3);
        user3.setUserPolicies(new ArrayList<>());

        Beneficiary beneficiary3 = new Beneficiary();
        beneficiary3.setBeneficiaryName("Bella");
        beneficiary3.setDob(LocalDate.of(1970, 1, 1));
        beneficiary3.setId(1);
        beneficiary3.setRelation(Relation.PARENTS);
        beneficiary3.setUser(user3);

        // Act
        Beneficiary actualUpdateBeneficiaryResult = beneficiaryServiceImp.updateBeneficiary(beneficiary3);

        // Assert
        verify(beneficiaryRepository).findById(eq(1));
        verify(beneficiaryRepository).save(isA(Beneficiary.class));
        assertEquals("1970-01-01", actualUpdateBeneficiaryResult.getDob().toString());
        assertEquals("Bella", actualUpdateBeneficiaryResult.getBeneficiaryName());
        assertEquals(Relation.PARENTS, actualUpdateBeneficiaryResult.getRelation());
        assertSame(beneficiary, actualUpdateBeneficiaryResult);
    }

    /**
     * Method under test:
     * {@link BeneficiaryServiceImpl#updateBeneficiary(Beneficiary)}
     */
    @Test
    void testUpdateBeneficiary2() {
        // Arrange
        Address address = new Address();
        address.setCity("Oxford");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        Role role = new Role();
        role.setId(1);
        role.setRoleType(RoleType.CUSTOMER);

        User user = new User();
        user.setAadharNo("Aadhar No");
        user.setAddress(address);
        user.setApplications(new ArrayList<>());
        user.setBeneficiaries(new ArrayList<>());
        user.setDob(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setGender(Gender.MALE);
        user.setId(1);
        user.setName("Bella");
        user.setPassword("iloveyou");
        user.setPhoneNumber("6625550144");
        user.setRole(role);
        user.setUserPolicies(new ArrayList<>());

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryName("Bella");
        beneficiary.setDob(LocalDate.of(1970, 1, 1));
        beneficiary.setId(1);
        beneficiary.setRelation(Relation.PARENTS);
        beneficiary.setUser(user);
        Optional<Beneficiary> ofResult = Optional.of(beneficiary);
        when(beneficiaryRepository.save(Mockito.any())).thenThrow(new InsuranceCustomException("An error occurred", "An error occurred"));
        when(beneficiaryRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        Address address2 = new Address();
        address2.setCity("Oxford");
        address2.setPinCode("Pin Code");
        address2.setState("MD");
        address2.setStreet("Street");

        Role role2 = new Role();
        role2.setId(1);
        role2.setRoleType(RoleType.CUSTOMER);

        User user2 = new User();
        user2.setAadharNo("Aadhar No");
        user2.setAddress(address2);
        user2.setApplications(new ArrayList<>());
        user2.setBeneficiaries(new ArrayList<>());
        user2.setDob(LocalDate.of(1970, 1, 1));
        user2.setEmail("jane.doe@example.org");
        user2.setGender(Gender.MALE);
        user2.setId(1);
        user2.setName("Bella");
        user2.setPassword("iloveyou");
        user2.setPhoneNumber("6625550144");
        user2.setRole(role2);
        user2.setUserPolicies(new ArrayList<>());

        Beneficiary beneficiary2 = new Beneficiary();
        beneficiary2.setBeneficiaryName("Bella");
        beneficiary2.setDob(LocalDate.of(1970, 1, 1));
        beneficiary2.setId(1);
        beneficiary2.setRelation(Relation.PARENTS);
        beneficiary2.setUser(user2);

        // Act and Assert
        assertThrows(InsuranceCustomException.class, () -> beneficiaryServiceImp.updateBeneficiary(beneficiary2));
        verify(beneficiaryRepository).findById(eq(1));
        verify(beneficiaryRepository).save(isA(Beneficiary.class));
    }

    /**
     * Method under test: {@link BeneficiaryServiceImpl#deleteBeneficiary(int)}
     */
    @Test
    void testDeleteBeneficiary() {
        // Arrange
        Address address = new Address();
        address.setCity("Oxford");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        Role role = new Role();
        role.setId(1);
        role.setRoleType(RoleType.CUSTOMER);

        User user = new User();
        user.setAadharNo("Aadhar No");
        user.setAddress(address);
        user.setApplications(new ArrayList<>());
        user.setBeneficiaries(new ArrayList<>());
        user.setDob(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setGender(Gender.MALE);
        user.setId(1);
        user.setName("Bella");
        user.setPassword("iloveyou");
        user.setPhoneNumber("6625550144");
        user.setRole(role);
        user.setUserPolicies(new ArrayList<>());

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryName("Bella");
        beneficiary.setDob(LocalDate.of(1970, 1, 1));
        beneficiary.setId(1);
        beneficiary.setRelation(Relation.PARENTS);
        beneficiary.setUser(user);
        Optional<Beneficiary> ofResult = Optional.of(beneficiary);
        doNothing().when(beneficiaryRepository).deleteById(Mockito.<Integer>any());
        when(beneficiaryRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act
        beneficiaryServiceImp.deleteBeneficiary(1);

        // Assert that nothing has changed
        verify(beneficiaryRepository).deleteById(eq(1));
        verify(beneficiaryRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link BeneficiaryServiceImpl#deleteBeneficiary(int)}
     */
    @Test
    void testDeleteBeneficiary2() {
        // Arrange
        Address address = new Address();
        address.setCity("Oxford");
        address.setPinCode("Pin Code");
        address.setState("MD");
        address.setStreet("Street");

        Role role = new Role();
        role.setId(1);
        role.setRoleType(RoleType.CUSTOMER);

        User user = new User();
        user.setAadharNo("Aadhar No");
        user.setAddress(address);
        user.setApplications(new ArrayList<>());
        user.setBeneficiaries(new ArrayList<>());
        user.setDob(LocalDate.of(1970, 1, 1));
        user.setEmail("jane.doe@example.org");
        user.setGender(Gender.MALE);
        user.setId(1);
        user.setName("Bella");
        user.setPassword("iloveyou");
        user.setPhoneNumber("6625550144");
        user.setRole(role);
        user.setUserPolicies(new ArrayList<>());

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryName("Bella");
        beneficiary.setDob(LocalDate.of(1970, 1, 1));
        beneficiary.setId(1);
        beneficiary.setRelation(Relation.PARENTS);
        beneficiary.setUser(user);
        Optional<Beneficiary> ofResult = Optional.of(beneficiary);
        doThrow(new InsuranceCustomException("An error occurred", "An error occurred")).when(beneficiaryRepository).deleteById(Mockito.<Integer>any());
        when(beneficiaryRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(InsuranceCustomException.class, () -> beneficiaryServiceImp.deleteBeneficiary(1));
        verify(beneficiaryRepository).deleteById(eq(1));
        verify(beneficiaryRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link BeneficiaryServiceImpl#deleteBeneficiary(int)}
     */
    @Test
    void testDeleteBeneficiary3() {
        // Arrange
        Optional<Beneficiary> emptyResult = Optional.empty();
        when(beneficiaryRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(InsuranceCustomException.class, () -> beneficiaryServiceImp.deleteBeneficiary(1));
        verify(beneficiaryRepository).findById(eq(1));
    }
}
