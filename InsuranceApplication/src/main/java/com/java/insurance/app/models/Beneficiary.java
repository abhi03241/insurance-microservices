package com.java.insurance.app.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.java.insurance.app.models.enums.Relation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.java.insurance.app.constants.AppConstant.BENEFICIARY;
import static com.java.insurance.app.constants.AppConstant.CANNOT_BE_NULL_OR_BLANK;
import static com.java.insurance.app.constants.AppConstant.DOB;
import static com.java.insurance.app.constants.AppConstant.NAME;
import static com.java.insurance.app.constants.AppConstant.RELATION;
import static com.java.insurance.app.constants.AppConstant.SPACE;

@Entity
@Table(name = BENEFICIARY)
@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class Beneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    @NotNull(message = DOB + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private LocalDate dob;
    @Column(nullable = false)
    @NotBlank(message = NAME + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private String beneficiaryName;
    @Column(nullable = false)
    @NotNull(message = RELATION + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @Enumerated(EnumType.STRING)
    private Relation relation;
    @ManyToOne
    @JsonBackReference
    private User user;
}
