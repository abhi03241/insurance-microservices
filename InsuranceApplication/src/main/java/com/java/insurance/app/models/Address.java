package com.java.insurance.app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.java.insurance.app.constants.AppConstant.CANNOT_BE_NULL_OR_BLANK;
import static com.java.insurance.app.constants.AppConstant.CITY;
import static com.java.insurance.app.constants.AppConstant.PIN_CODE;
import static com.java.insurance.app.constants.AppConstant.SPACE;
import static com.java.insurance.app.constants.AppConstant.STATE;
import static com.java.insurance.app.constants.AppConstant.STREET;

@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {
    @Column(nullable = false)
    @NotBlank(message = STATE + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private String state;
    @Column(nullable = false)
    @NotBlank(message = CITY + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private String city;
    @NotBlank(message = PIN_CODE + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @Column(nullable = false)
    private String pinCode;
    @NotBlank(message = STREET + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @Column(nullable = false)
    private String street;
}
