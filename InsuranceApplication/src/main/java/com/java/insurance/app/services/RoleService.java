package com.java.insurance.app.services;

import com.java.insurance.app.models.Role;
import com.java.insurance.app.models.enums.RoleType;

import java.util.List;

public interface RoleService {
    Role createRole(Role role);

    List<Role> getAllRoles();

    Role getRole(int roleId);

    Role getRoleByRoleType(RoleType roleType);
}
