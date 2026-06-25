package com.java.insurance.app.models;


import com.java.insurance.app.models.enums.PremiumType;
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

import static com.java.insurance.app.constants.AppConstant.AMOUNT;
import static com.java.insurance.app.constants.AppConstant.CANNOT_BE_NULL_OR_BLANK;
import static com.java.insurance.app.constants.AppConstant.PREMIUM_DB;
import static com.java.insurance.app.constants.AppConstant.PREMIUM_TYPE;
import static com.java.insurance.app.constants.AppConstant.SPACE;

@Entity
@Data
@Table(name = PREMIUM_DB)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Premium {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = PREMIUM_TYPE + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private PremiumType premiumType;
    @Column(nullable = false)
    @NotNull(message = AMOUNT + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private double premiumAmount;
}
