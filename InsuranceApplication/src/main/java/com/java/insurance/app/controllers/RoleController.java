package com.java.insurance.app.controllers;

import com.java.insurance.app.constants.AccessConstants;
import com.java.insurance.app.models.Role;
import com.java.insurance.app.services.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.java.insurance.app.constants.AppConstant.ROLE_ID;
import static com.java.insurance.app.constants.RoleUrls.CREATE_ROLE;
import static com.java.insurance.app.constants.RoleUrls.GET_ALL_ROLE;
import static com.java.insurance.app.constants.RoleUrls.GET_ROLE;

@RestController
@RequiredArgsConstructor
//@PreAuthorize(AccessConstants.HAS_ROLE_ADMIN)
@Tag(name = "Role APIs")
public class RoleController {
    private final RoleService roleService;

    /**
     * Creates a new role.
     *
     * @param role The role to create.
     * @return ResponseEntity with the created role.
     */
    @PostMapping(CREATE_ROLE)
    public ResponseEntity<Role> createRole(@RequestBody @Valid Role role) {
        return new ResponseEntity<>(roleService.createRole(role), HttpStatus.CREATED);
    }

    /**
     * Retrieves a role by ID.
     *
     * @param roleId The ID of the role to retrieve.
     * @return ResponseEntity with the retrieved role.
     */
    @GetMapping(GET_ROLE)
    public ResponseEntity<Role> getRole(@PathVariable(ROLE_ID) int roleId) {
        return ResponseEntity.ok(roleService.getRole(roleId));
    }

    /**
     * Retrieves all roles.
     *
     * @return ResponseEntity with a list of all roles.
     */
    @GetMapping(GET_ALL_ROLE)
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }
}
