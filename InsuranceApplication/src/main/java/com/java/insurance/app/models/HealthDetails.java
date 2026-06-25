package com.java.insurance.app.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import static com.java.insurance.app.constants.AppConstant.ALCOHOL_CONSUMPTION;
import static com.java.insurance.app.constants.AppConstant.CANNOT_BE_NULL_OR_BLANK;
import static com.java.insurance.app.constants.AppConstant.DISEASE_ID_COL_NAME;
import static com.java.insurance.app.constants.AppConstant.ID;
import static com.java.insurance.app.constants.AppConstant.ROOT_CANAL_TREATMENT;
import static com.java.insurance.app.constants.AppConstant.SMOKING_STATUS;
import static com.java.insurance.app.constants.AppConstant.SPACE;
import static com.java.insurance.app.constants.AppConstant.TOBACCO_CONSUMPTION;
import static com.java.insurance.app.constants.AppConstant.TOOTH_EXTRACTION;
import static com.java.insurance.app.constants.AppConstant.USER_DISEASES;
import static com.java.insurance.app.constants.AppConstant.USER_HEALTH_DETAILS;
import static com.java.insurance.app.constants.AppConstant.USER_ID;

@Entity
@Table(name = USER_HEALTH_DETAILS)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user"})
public class HealthDetails {

    @OneToOne
    @Id
    @JsonIgnore
    @JoinColumn(name = USER_ID, referencedColumnName = ID, nullable = false)
    private User user;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = USER_DISEASES, joinColumns = {@JoinColumn(name = USER_ID)}, inverseJoinColumns = {@JoinColumn(name = DISEASE_ID_COL_NAME)})
    private List<Disease> diseases;
    @NotNull(message = ALCOHOL_CONSUMPTION + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @Column(nullable = false)
    private Boolean hasAlcoholConsumption;
    @Column(nullable = false)
    @NotNull(message = TOBACCO_CONSUMPTION + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private Boolean hasTobaccoConsumption;
    @Column(nullable = false)
    @NotNull(message = SMOKING_STATUS + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private Boolean hasSmokingStatus;
    @Column(nullable = false)
    @NotNull(message = ROOT_CANAL_TREATMENT + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private Boolean hasRootCanalTreatment;
    @Column(nullable = false)
    @NotNull(message = TOOTH_EXTRACTION + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private Boolean hasToothExtraction;
}
