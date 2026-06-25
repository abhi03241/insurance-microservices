package com.java.insurance.app.services.implementations;

import com.java.insurance.app.constants.ErrorCode;
import com.java.insurance.app.exception.InsuranceCustomException;
import com.java.insurance.app.models.Disease;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.enums.RoleType;
import com.java.insurance.app.repositories.UserRepository;
import com.java.insurance.app.services.DiseaseService;
import com.java.insurance.app.services.RoleService;
import com.java.insurance.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

import static com.java.insurance.app.constants.AppConstant.COLON;
import static com.java.insurance.app.constants.AppConstant.USER_CREATED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.USER_DELETED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.USER_NOT_FOUND_WITH_EMAIL;
import static com.java.insurance.app.constants.AppConstant.USER_NOT_FOUND_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.USER_UPDATED_WITH_ID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final DiseaseService diseaseService;

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user with the specified ID.
     * @throws InsuranceCustomException If the user is not found with the given ID.
     */
    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new InsuranceCustomException(USER_NOT_FOUND_WITH_ID + COLON + userId, ErrorCode.USER_NOT_FOUND));
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user to retrieve.
     * @return The user with the specified email address.
     * @throws InsuranceCustomException If the user is not found with the given email address.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new InsuranceCustomException(USER_NOT_FOUND_WITH_EMAIL + COLON + email, ErrorCode.USER_NOT_FOUND));
    }

    /**
     * Creates a new user.
     *
     * @param user The user object to be created.
     * @return The created user.
     */
    @Override
    public User createUser(@Validated User user) {
        user.setRole(roleService.getRoleByRoleType(RoleType.CUSTOMER));
        user.getHealthDetails().setDiseases(user.getHealthDetails().getDiseases().stream().map(disease -> diseaseService.getDiseaseByType(disease.getDiseaseType())).toList());
        user.setPassword(user.getPassword());
        user.setApplications(new ArrayList<>());
        user.setUserPolicies(new ArrayList<>());
        User savedUser = userRepository.save(user);
        logger.info(USER_CREATED_WITH_ID + COLON + savedUser.getId());
        return savedUser;
    }

    /**
     * Updates an existing user.
     *
     * @param user The user object with updated information.
     * @return The updated user.
     */
    @Override
    public User updateUser(@Validated User user) {
        User user1 = userRepository.findById(user.getId()).orElseThrow(() -> new InsuranceCustomException(USER_NOT_FOUND_WITH_ID + COLON + user.getId(), ErrorCode.USER_NOT_FOUND));
        user1.setName(user.getName());
        user1.setAddress(user.getAddress());
        user1.setDob(user.getDob());
        user1.setEmail(user.getEmail());
        user1.setAddress(user.getAddress());
        user1.setGender(user.getGender());
        user1.setUserPolicies(user.getUserPolicies());
        List<Disease> diseases = user.getHealthDetails().getDiseases().stream().map(disease -> diseaseService.getDiseaseByType(disease.getDiseaseType())).toList();
        user1.setPhoneNumber(user.getPhoneNumber());
        user.getHealthDetails().setDiseases(diseases);
        user1.setHealthDetails(user.getHealthDetails());
        User updatedUser = userRepository.save(user1);
        logger.info(USER_UPDATED_WITH_ID + COLON + updatedUser.getId());
        return updatedUser;
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete.
     */
    @Override
    public void deleteUser(int userId) {
        this.getUserById(userId);
        userRepository.deleteById(userId);
        logger.info(USER_DELETED_WITH_ID + COLON + userId);
    }

    /**
     * Loads a user by their username (email address).
     *
     * @param username The username (email address) of the user to load.
     * @return The UserDetails object representing the user.
     * @throws UsernameNotFoundException If the user is not found with the given username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.getUserByEmail(username);
    }
}



