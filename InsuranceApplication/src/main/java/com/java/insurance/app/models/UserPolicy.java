package com.java.insurance.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

import static com.java.insurance.app.constants.AppConstant.CANNOT_BE_NULL_OR_BLANK;
import static com.java.insurance.app.constants.AppConstant.ENDS_ON;
import static com.java.insurance.app.constants.AppConstant.ID;
import static com.java.insurance.app.constants.AppConstant.POLICY_ID_COL;
import static com.java.insurance.app.constants.AppConstant.SPACE;
import static com.java.insurance.app.constants.AppConstant.STARTED_ON;
import static com.java.insurance.app.constants.AppConstant.USER_ID;
import static com.java.insurance.app.constants.AppConstant.USER_POLICIES;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = USER_POLICIES)
@ToString(exclude = {"user"})
public class UserPolicy {
    @JsonIgnore
    @ManyToOne
    @Id
    @JoinColumn(name = USER_ID, referencedColumnName = ID)
    private User user;
    @JsonIgnore
    @ManyToOne
    @Id
    @JoinColumn(name = POLICY_ID_COL, referencedColumnName = ID)
    private Policy policy;
    @NotNull(message = STARTED_ON + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private LocalDate startedOn;
    @NotNull(message = ENDS_ON + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private LocalDate endsOn;
}
