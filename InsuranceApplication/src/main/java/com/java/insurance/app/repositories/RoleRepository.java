package com.java.insurance.app.repositories;

import com.java.insurance.app.constants.MysqlQueries;
import com.java.insurance.app.models.Role;
import com.java.insurance.app.models.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleType(RoleType roleType);

    @Query(value = MysqlQueries.ROLE_REPOSITORY_FIND_ROLE_BY_USER_ID, nativeQuery = true)
    Role findRoleByUserId(int userId);
}
