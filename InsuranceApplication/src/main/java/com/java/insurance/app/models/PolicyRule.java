package com.java.insurance.app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.java.insurance.app.constants.AppConstant.CANNOT_BE_NULL_OR_BLANK;
import static com.java.insurance.app.constants.AppConstant.DISEASE_ID_COL;
import static com.java.insurance.app.constants.AppConstant.HEALTH_SCORE;
import static com.java.insurance.app.constants.AppConstant.MAX_AGE;
import static com.java.insurance.app.constants.AppConstant.MIN_AGE;
import static com.java.insurance.app.constants.AppConstant.NO_OF_BENEFICIARIES;
import static com.java.insurance.app.constants.AppConstant.POLICY_RULE_DB;
import static com.java.insurance.app.constants.AppConstant.POLICY_RULE_DISEASES;
import static com.java.insurance.app.constants.AppConstant.POLICY_RULE_ID;
import static com.java.insurance.app.constants.AppConstant.SPACE;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = POLICY_RULE_DB)
public class PolicyRule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    @NotNull(message = MIN_AGE + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @Min(0)
    @Max(100)
    private int minAge;
    @Column(nullable = false)
    @Min(0)
    @Max(100)
    @NotNull(message = MAX_AGE + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private int maxAge;
    @NotNull(message = NO_OF_BENEFICIARIES + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @Column(nullable = false)
    @Min(0)
    @Max(5)
    private int numberOfBeneficiaries;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = POLICY_RULE_DISEASES, joinColumns = {@JoinColumn(name = POLICY_RULE_ID)}, inverseJoinColumns = {@JoinColumn(name = DISEASE_ID_COL)})
    private List<Disease> uncoveredDiseases;
    @Column(nullable = false)
    @Min(10)
    @NotNull(message = HEALTH_SCORE + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @Max(100)
    private int minHealthScore;

}
