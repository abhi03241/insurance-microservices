package com.java.insurance.app.models.enums;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.java.insurance.app.constants.AppConstant.ROLE_WITH_UNDERSCORE;
import static com.java.insurance.app.models.enums.Permission.APPLICATION_CREATE;
import static com.java.insurance.app.models.enums.Permission.APPLICATION_READ;
import static com.java.insurance.app.models.enums.Permission.APPLICATION_WRITE;
import static com.java.insurance.app.models.enums.Permission.BENEFICIARY_READ;
import static com.java.insurance.app.models.enums.Permission.BENEFICIARY_WRITE;
import static com.java.insurance.app.models.enums.Permission.POLICY_READ;
import static com.java.insurance.app.models.enums.Permission.POLICY_WRITE;
import static com.java.insurance.app.models.enums.Permission.ROLE_READ;
import static com.java.insurance.app.models.enums.Permission.ROLE_WRITE;
import static com.java.insurance.app.models.enums.Permission.USER_READ;
import static com.java.insurance.app.models.enums.Permission.USER_WRITE;

public enum RoleType {
    CUSTOMER(Sets.newHashSet(USER_READ, USER_WRITE, APPLICATION_CREATE, APPLICATION_READ, POLICY_READ, BENEFICIARY_READ, BENEFICIARY_WRITE)), UNDERWRITER(Sets.newHashSet(APPLICATION_READ, APPLICATION_WRITE)), ADMIN(Sets.newHashSet(USER_READ, USER_WRITE, APPLICATION_READ, APPLICATION_WRITE, ROLE_READ, ROLE_WRITE, POLICY_READ, POLICY_WRITE));

    private final Set<Permission> permissions;

    RoleType(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermission() {
        return this.permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> grantedAuthorities = this.getPermission().stream().map(permission -> new SimpleGrantedAuthority(permission.name())).collect(Collectors.toSet());
        grantedAuthorities.add(new SimpleGrantedAuthority(ROLE_WITH_UNDERSCORE + this.name()));
        return grantedAuthorities;
    }
}
