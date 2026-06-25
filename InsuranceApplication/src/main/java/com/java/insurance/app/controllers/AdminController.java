package com.java.insurance.app.controllers;

import com.java.insurance.app.constants.AccessConstants;
import com.java.insurance.app.constants.AdminUrls;
import com.java.insurance.app.models.User;
import com.java.insurance.app.services.AdminService;
import com.java.insurance.app.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.java.insurance.app.constants.AppConstant.ADMIN_DELETED;
import static com.java.insurance.app.constants.AppConstant.UNDERWRITER_DELETED;

@RestController
@RequestMapping
@RequiredArgsConstructor
@EnableMethodSecurity
@PreAuthorize(AccessConstants.HAS_ROLE_ADMIN)
@Tag(name = "Admin APIs")
public class AdminController {
    private final AdminService adminService;
    private final UserService userService;

    /**
     * Creates a new admin user.
     *
     * @param admin The admin user to create.
     * @return ResponseEntity containing the created admin user.
     */
    @PostMapping(AdminUrls.CREATE_ADMIN)
    public ResponseEntity<User> createAdmin(@RequestBody @Valid User admin) {
        User user = adminService.createAdmin(admin);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * Creates a new underwriter user.
     *
     * @param underwriter The underwriter user to create.
     * @return ResponseEntity containing the created underwriter user.
     */
    @PostMapping(AdminUrls.CREATE_UNDERWRITER)
    public ResponseEntity<User> createUnderwriter(@RequestBody @Valid User underwriter) {
        User user = adminService.createUnderwriter(underwriter);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * Deletes an admin user by ID.
     *
     * @param adminId The ID of the admin user to delete.
     * @return ResponseEntity with a message indicating successful deletion.
     */
    @DeleteMapping(AdminUrls.DELETE_ADMIN)
    public ResponseEntity<String> deleteAdmin(@PathVariable int adminId) {

        userService.deleteUser(adminId);
        return ResponseEntity.ok(ADMIN_DELETED);
    }

    /**
     * Deletes an underwriter user by ID.
     *
     * @param underwriterId The ID of the underwriter user to delete.
     * @return ResponseEntity with a message indicating successful deletion.
     */
    @DeleteMapping(AdminUrls.DELETE_UNDERWRITER)
    public ResponseEntity<String> deleteUnderwriter(@PathVariable int underwriterId) {
        userService.deleteUser(underwriterId);
        return ResponseEntity.ok(UNDERWRITER_DELETED);
    }


}
