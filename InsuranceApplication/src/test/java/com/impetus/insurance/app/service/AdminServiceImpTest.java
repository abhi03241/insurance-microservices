package com.java.insurance.app.service;

import com.java.insurance.app.models.Role;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.enums.RoleType;
import com.java.insurance.app.repositories.UserRepository;
import com.java.insurance.app.services.RoleService;
import com.java.insurance.app.services.UserService;
import com.java.insurance.app.services.implementations.AdminServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminServiceImpTest {
    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCreateAdmin_Success() {
        // Given
        User admin = new User();
        admin.setName("admin");
        admin.setPassword("admin123");

        Role adminRole = new Role();
        adminRole.setRoleType(RoleType.ADMIN);

        when(roleService.getRoleByRoleType(RoleType.ADMIN)).thenReturn(adminRole);
        when(passwordEncoder.encode(admin.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(admin);

        // When
        User createdAdmin = adminService.createAdmin(admin);

        // Then
        assertNotNull(createdAdmin);
        assertEquals("admin", createdAdmin.getName());
        assertEquals(RoleType.ADMIN, createdAdmin.getRole().getRoleType());
        verify(passwordEncoder).encode("admin123");
        verify(userRepository).save(admin);
    }

    @Test
    void testCreateUnderwriter_Success() {
        // Given
        User underwriter = new User();
        underwriter.setName("underwriter");
        underwriter.setPassword("underwriter123");

        Role underwriterRole = new Role();
        underwriterRole.setRoleType(RoleType.UNDERWRITER);

        when(roleService.getRoleByRoleType(RoleType.UNDERWRITER)).thenReturn(underwriterRole);
        when(passwordEncoder.encode(underwriter.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(underwriter);

        // When
        User createdUnderwriter = adminService.createUnderwriter(underwriter);

        // Then
        assertNotNull(createdUnderwriter);
        assertEquals("underwriter", createdUnderwriter.getName());
        assertEquals(RoleType.UNDERWRITER, createdUnderwriter.getRole().getRoleType());
        verify(passwordEncoder).encode("underwriter123");
        verify(userRepository).save(underwriter);
    }

    @Test
    void testDeleteUnderwriter_Success() {
        // Given
        int underwriterId = 123;

        // When
        adminService.deleteUnderwriter(underwriterId);

        // Then
        verify(userService).deleteUser(underwriterId);
    }

    @Test
    void testDeleteAdmin_Success() {
        // Given
        int adminId = 456;

        // When
        adminService.deleteAdmin(adminId);

        // Then
        verify(userService).deleteUser(adminId);
    }
}
