package com.java.insurance.app.services.implementations;

import com.java.insurance.app.constants.ErrorCode;
import com.java.insurance.app.exception.InsuranceCustomException;
import com.java.insurance.app.models.User;
import com.java.insurance.app.models.enums.RoleType;
import com.java.insurance.app.repositories.UserRepository;
import com.java.insurance.app.services.AdminService;
import com.java.insurance.app.services.RoleService;
import com.java.insurance.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.java.insurance.app.constants.AppConstant.ADMIN_CREATED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.ADMIN_DELETED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.ADMIN_NOT_FOUND_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.COLON;
import static com.java.insurance.app.constants.AppConstant.UNDERWRITER_CREATED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.UNDERWRITER_DELETED_WITH_ID;
import static com.java.insurance.app.constants.AppConstant.UNDERWRITER_NOT_FOUND_WITH_ID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final UserService userService;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new admin user in the system.
     *
     * @param admin The admin user to create.
     * @return The created admin user.
     */
    @Override
    public User createAdmin(User admin) {
        admin.setRole(roleService.getRoleByRoleType(RoleType.ADMIN));
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        User user = userRepository.save(admin);
        logger.info(ADMIN_CREATED_WITH_ID + COLON + user.getId());
        return user;
    }

    /**
     * Creates a new underwriter user in the system.
     *
     * @param underwriter The underwriter user to create.
     * @return The created underwriter user.
     */
    @Override
    public User createUnderwriter(User underwriter) {
        underwriter.setRole(roleService.getRoleByRoleType(RoleType.UNDERWRITER));
        underwriter.setPassword(passwordEncoder.encode(underwriter.getPassword()));
        User savedUnderwriter = userRepository.save(underwriter);
        logger.info(UNDERWRITER_CREATED_WITH_ID + COLON + savedUnderwriter.getId());
        return savedUnderwriter;
    }


    /**
     * Deletes an underwriter user from the system.
     *
     * @param underwriterId The ID of the underwriter user to delete.
     */
    @Override
    public void deleteUnderwriter(int underwriterId) {
        try {
            userService.getUserById(underwriterId);
        } catch (InsuranceCustomException e) {
            throw new InsuranceCustomException(UNDERWRITER_NOT_FOUND_WITH_ID + COLON + underwriterId, ErrorCode.UNDERWRITER_NOT_FOUND);
        }
        userService.deleteUser(underwriterId);
        logger.info(UNDERWRITER_DELETED_WITH_ID + COLON + underwriterId);
    }


    /**
     * Deletes an admin user from the system.
     *
     * @param adminId The ID of the admin user to delete.
     */

    @Override
    public void deleteAdmin(int adminId) {
        try {
            userService.getUserById(adminId);
        } catch (InsuranceCustomException ie) {
            throw new InsuranceCustomException(ADMIN_NOT_FOUND_WITH_ID + COLON + adminId, ErrorCode.ADMIN_NOT_FOUND);
        }
        userService.deleteUser(adminId);
        logger.info(ADMIN_DELETED_WITH_ID + COLON + adminId);
    }
}
