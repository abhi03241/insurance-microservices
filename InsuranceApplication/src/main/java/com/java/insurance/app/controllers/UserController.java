package com.java.insurance.app.controllers;


import com.java.insurance.app.constants.AccessConstants;
import com.java.insurance.app.constants.UserUrls;
import com.java.insurance.app.models.User;
import com.java.insurance.app.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.java.insurance.app.constants.AppConstant.USER_DELETED;

@RestController
@RequiredArgsConstructor
@EnableMethodSecurity
@Tag(name = "User APIs")
public class UserController {
    private final UserService userService;

    /**
     * Retrieves all users.
     *
     * @return List of all users.
     */
    @GetMapping(UserUrls.GET_ALL_USERS)
    @PreAuthorize(AccessConstants.HAS_ROLE_ADMIN)
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    /**
     * Creates a new user.
     *
     * @param user The user object to create.
     * @return The created user.
     */
    @PostMapping(UserUrls.CREATE_USER)
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    /**
     * Updates an existing user.
     *
     * @param user The user object to update.
     * @return The updated user.
     */
    @PutMapping(UserUrls.UPDATE_USER)
    public ResponseEntity<User> updateUser(@RequestBody @Valid User user) {
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.OK);
    }

    /**
     * Retrieves the authenticated user.
     *
     * @return ResponseEntity with the authenticated user.
     */
    @GetMapping(UserUrls.GET_USER)
    public ResponseEntity<User> getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userService.getUserById(user.getId()));
    }

    /**
     * Deletes the authenticated user.
     *
     * @return ResponseEntity with a success message.
     */
    @DeleteMapping(UserUrls.DELETE_USER)
    public ResponseEntity<String> deleteUser() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.deleteUser(user.getId());
        return ResponseEntity.ok(USER_DELETED);
    }


}
