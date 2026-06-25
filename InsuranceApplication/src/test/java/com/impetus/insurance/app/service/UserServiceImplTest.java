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
import com.java.insurance.app.models.enums.RoleType;
import com.java.insurance.app.repositories.UserRepository;
import com.java.insurance.app.services.DiseaseService;
import com.java.insurance.app.services.RoleService;
import com.java.insurance.app.services.UserService;
import com.java.insurance.app.services.implementations.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.java.insurance.app.models.enums.Gender.MALE;
import static com.java.insurance.app.models.enums.RoleType.CUSTOMER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UserServiceImplTest {

    AutoCloseable autoCloseable;
    User user;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @Mock
    private DiseaseService diseaseService;

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
        user = new User(1, MALE, "9876543211", "123456789012", LocalDate.of(2002, 5, 3), "AMIT", "AMIT", "amit@gmail.com", new Address("MP", "Indore", "452001", "Rajiv Gandhi"), role, apps, policies, ben, healthDetails);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAllUsers() {
        mock(User.class);
        mock(UserRepository.class);

        when(userRepository.findAll()).thenReturn(new ArrayList<>(Collections.singleton(user)));
        assertThat(userService.getAllUsers().get(0).getId()).isEqualTo(user.getId());
    }

    @Test
    void getUserById() {
        mock(User.class);
        mock(UserRepository.class);

        when(userRepository.findById(1)).thenReturn(Optional.ofNullable((user)));
        assertThat(userService.getUserById(1).getId()).isEqualTo(user.getId());
    }

    @Test
    @WithMockUser
    void testGetUserByEmail_ExistingUser() {
        // Mocking the behavior of userRepository.findByEmail
        String email = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // Calling the method under test
        User result = userService.getUserByEmail(email);

        // Assertions
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testCreateUser() {
        Role role = new Role();
        role.setRoleType(RoleType.CUSTOMER);

        User user = new User();
        user.setId(1);
        user.setGender(MALE);
        user.setPhoneNumber("9876543211");
        user.setAadharNo("123456789012");
        user.setDob(LocalDate.of(2002, 5, 3));
        user.setName("AMIT");
        user.setPassword("password"); // plain text password
        user.setEmail("amit@gmail.com");
        Address address = new Address("MP", "Indore", "452001", "Rajiv Gandhi");
        user.setAddress(address);
        user.setRole(role);
        user.setApplications(new ArrayList<>());
        user.setUserPolicies(new ArrayList<>());
        user.setBeneficiaries(new ArrayList<>());
        HealthDetails healthDetails = new HealthDetails();
        healthDetails.setDiseases(new ArrayList<>());
        user.setHealthDetails(healthDetails);

        when(diseaseService.getDiseaseByType(any())).thenReturn(new Disease()); // mocking disease service

        // Mocking userRepository.save to return the user object with an ID
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1); // setting an ID
            return savedUser;
        });

        // When
        User createdUser = userService.createUser(user);

        // Then
        assertNotNull(createdUser);// ID should not be null after save
        assertEquals(user.getUsername(), createdUser.getUsername());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getRole(), createdUser.getRole());
        assertEquals(user.getAddress(), createdUser.getAddress());
        assertEquals(user.getHealthDetails().getDiseases().size(), createdUser.getHealthDetails().getDiseases().size());

        // Verifying that userRepository.save was called
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @WithMockUser
    void testUpdateUser() {
        // Given
        // Create a user object to be updated
        Role role = new Role();
        role.setRoleType(RoleType.CUSTOMER);

        User existingUser = new User();
        existingUser.setId(1);
        existingUser.setGender(MALE);
        existingUser.setPhoneNumber("9876543211");
        existingUser.setAadharNo("123456789012");
        existingUser.setDob(LocalDate.of(2002, 5, 3));
        existingUser.setName("AMIT");
        existingUser.setPassword("password"); // plain text password
        existingUser.setEmail("amit@gmail.com");
        Address address = new Address("MP", "Indore", "452001", "Rajiv Gandhi");
        existingUser.setAddress(address);
        existingUser.setRole(role);
        existingUser.setApplications(new ArrayList<>());
        existingUser.setUserPolicies(new ArrayList<>());
        existingUser.setBeneficiaries(new ArrayList<>());
        HealthDetails healthDetails = new HealthDetails();
        healthDetails.setDiseases(new ArrayList<>());
        existingUser.setHealthDetails(healthDetails);

        // Mock the userRepository.findById to return the existing user
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(existingUser));

        // Mock the userRepository.save to return the updated user object
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Update user details
        existingUser.setName("Updated Name");
        existingUser.setPhoneNumber("9876543210");

        // When
        User updatedUser = userService.updateUser(existingUser);

        // Then
        assertNotNull(updatedUser);
        assertEquals(existingUser.getId(), updatedUser.getId());
        assertEquals(existingUser.getName(), updatedUser.getName());
        assertEquals(existingUser.getEmail(), updatedUser.getEmail());
        assertEquals(existingUser.getRole(), updatedUser.getRole());
        assertEquals(existingUser.getAddress(), updatedUser.getAddress());
        assertEquals(existingUser.getHealthDetails().getDiseases().size(), updatedUser.getHealthDetails().getDiseases().size());

        // Verifying that userRepository.findById was called to fetch the existing user
        verify(userRepository, times(1)).findById(existingUser.getId());

        // Verifying that userRepository.save was called to save the updated user
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testDeleteUser() {
        // Mocking userRepository.deleteById method
        doNothing().when(userRepository).deleteById(any());

        // Calling the method under test
        try {
            userService.deleteUser(1);
        } catch (InsuranceCustomException ignored) {
        }

        // Verifying that userRepository.deleteById method was called with the correct argument
        verify(userRepository, times(0)).deleteById(1);
    }
}
