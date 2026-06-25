package com.java.insurance.app.repositories;

import com.java.insurance.app.models.Policy;
import com.java.insurance.app.constants.MysqlQueries;
import com.java.insurance.app.models.enums.PolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Integer> {
    @Query(value = MysqlQueries.POLICY_REPOSITORY_FIND_USER_BY_ID, nativeQuery = true)
    List<Policy> findByUserId(int userId);

    List<Policy> findByExpiryBeforeAndPolicyStatus(LocalDate expiry, PolicyStatus policyStatus);
}
