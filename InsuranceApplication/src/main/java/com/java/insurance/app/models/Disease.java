package com.java.insurance.app.models;

import com.java.insurance.app.models.enums.DiseaseType;
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
import static com.java.insurance.app.constants.AppConstant.DISEASE;
import static com.java.insurance.app.constants.AppConstant.DISEASES;
import static com.java.insurance.app.constants.AppConstant.SPACE;

@Entity
@Table(name = DISEASES)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Enumerated(EnumType.STRING)
    @NotNull(message = DISEASE + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @Column(unique = true, nullable = false)
    DiseaseType diseaseType;
}
