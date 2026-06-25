package com.java.insurance.app.models;

import com.java.insurance.app.models.enums.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.java.insurance.app.constants.AppConstant.CANNOT_BE_NULL_OR_BLANK;
import static com.java.insurance.app.constants.AppConstant.ROLE;
import static com.java.insurance.app.constants.AppConstant.ROLE_TYPE;
import static com.java.insurance.app.constants.AppConstant.SPACE;

@Entity
@Table(name = ROLE)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    @NotNull(message = ROLE_TYPE + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @Column(nullable = false, unique = true)
    private RoleType roleType;
}



