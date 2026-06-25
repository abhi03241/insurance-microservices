package com.java.insurance.app.models;

import com.java.insurance.app.models.enums.PolicyStatus;
import com.java.insurance.app.models.enums.PolicyType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.java.insurance.app.constants.AppConstant.BENEFITS;
import static com.java.insurance.app.constants.AppConstant.CANNOT_BE_NULL_OR_BLANK;
import static com.java.insurance.app.constants.AppConstant.DESCRIPTION;
import static com.java.insurance.app.constants.AppConstant.EXPIRY;
import static com.java.insurance.app.constants.AppConstant.POLICY;
import static com.java.insurance.app.constants.AppConstant.POLICY_NAME;
import static com.java.insurance.app.constants.AppConstant.POLICY_PREMIUM_ID;
import static com.java.insurance.app.constants.AppConstant.POLICY_RULE;
import static com.java.insurance.app.constants.AppConstant.POLICY_RULE_ID;
import static com.java.insurance.app.constants.AppConstant.POLICY_TYPE;
import static com.java.insurance.app.constants.AppConstant.PREMIUM;
import static com.java.insurance.app.constants.AppConstant.SPACE;
import static com.java.insurance.app.constants.AppConstant.VALIDITY_IN_MONTHS;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = POLICY)
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = POLICY_TYPE + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @Column(nullable = false)
    private PolicyType policyType;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = POLICY_RULE + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @JoinColumn(name = POLICY_RULE_ID, nullable = false)
    private PolicyRule policyRule;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = PREMIUM + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @JoinColumn(name = POLICY_PREMIUM_ID, nullable = false)
    private Premium premium;

    @NotNull(message = EXPIRY + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private LocalDate expiry;
    @Column(nullable = false)
    @NotNull(message = VALIDITY_IN_MONTHS + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private int validityInMonths;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyStatus policyStatus;
    @Column(nullable = false)
    @NotBlank(message = BENEFITS + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private String benefits;
    @Column(nullable = false)
    @NotBlank(message = DESCRIPTION + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private String policyDesc;
    @Column(nullable = false)
    @NotBlank(message = POLICY_NAME + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private String policyName;
}
