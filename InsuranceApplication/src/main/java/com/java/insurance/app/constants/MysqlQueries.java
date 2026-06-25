package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MysqlQueries {
    public static final String POLICY_REPOSITORY_FIND_USER_BY_ID = "SELECT p1.* FROM policy p1 INNER JOIN user_policies up ON up.policy_id = p1.id WHERE up.user_id = :userId";
    public static final String ROLE_REPOSITORY_FIND_ROLE_BY_USER_ID = "SELECT r1.* FROM role r1 INNER JOIN user_role ur ON r1.id = ur.role_id WHERE ur.user_id = :userId";
}
