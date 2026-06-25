package com.java.insurance.app.services.implementations;

import com.java.insurance.app.services.RoleService;
import com.java.insurance.app.constants.ErrorCode;
import com.java.insurance.app.exception.InsuranceCustomException;
import com.java.insurance.app.models.Role;
import com.java.insurance.app.models.enums.RoleType;
import com.java.insurance.app.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.java.insurance.app.constants.AppConstant.COLON;
import static com.java.insurance.app.constants.AppConstant.ROLE_NOT_FOUND_WITH_ID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {
        Role savedRole = roleRepository.save(role);
        logger.info(ROLE_NOT_FOUND_WITH_ID + COLON + savedRole.getId());
        return savedRole;
    }

    /**
     * Retrieves all roles.
     *
     * @return A list of all roles.
     */
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Retrieves a role entity by its ID.
     *
     * @param roleId The ID of the role to retrieve.
     * @return The role entity with the specified ID.
     * @throws InsuranceCustomException If the role with the specified ID is not found.
     */
    @Override
    public Role getRole(int roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new InsuranceCustomException(ROLE_NOT_FOUND_WITH_ID + COLON + roleId, ErrorCode.ROLE_NOT_FOUND));
    }

    /**
     * Retrieves a role entity by its type.
     *
     * @param roleType The type of the role.
     * @return The role entity with the specified type.
     */
    @Override
    public Role getRoleByRoleType(RoleType roleType) {
        return roleRepository.findByRoleType(roleType);
    }
}
