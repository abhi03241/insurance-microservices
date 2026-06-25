package com.java.insurance.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.java.insurance.app.models.enums.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static com.java.insurance.app.constants.AppConstant.AADHAR_REGEX;
import static com.java.insurance.app.constants.AppConstant.ADDRESS;
import static com.java.insurance.app.constants.AppConstant.CANNOT_BE_NULL_OR_BLANK;
import static com.java.insurance.app.constants.AppConstant.DOB;
import static com.java.insurance.app.constants.AppConstant.EMAIL;
import static com.java.insurance.app.constants.AppConstant.GENDER;
import static com.java.insurance.app.constants.AppConstant.HEALTH_DETAILS;
import static com.java.insurance.app.constants.AppConstant.INVALID_AADHAR_NUMBER;
import static com.java.insurance.app.constants.AppConstant.INVALID_EMAIL;
import static com.java.insurance.app.constants.AppConstant.INVALID_PHONE_NUMBER;
import static com.java.insurance.app.constants.AppConstant.NAME;
import static com.java.insurance.app.constants.AppConstant.PASSWORD;
import static com.java.insurance.app.constants.AppConstant.PASSWORD_MESSAGE;
import static com.java.insurance.app.constants.AppConstant.PHONE_REGEX;
import static com.java.insurance.app.constants.AppConstant.ROLE_ID;
import static com.java.insurance.app.constants.AppConstant.SPACE;
import static com.java.insurance.app.constants.AppConstant.USER;
import static com.java.insurance.app.constants.AppConstant.USER_ID;
import static com.java.insurance.app.constants.AppConstant.USER_ROLES;


@Entity
@Table(name = USER)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"healthDetails"})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Enumerated(EnumType.STRING)
    @NotNull(message = GENDER + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private Gender gender;
    @Column(unique = true, nullable = false)
    @Pattern(regexp = PHONE_REGEX, message = INVALID_PHONE_NUMBER)
    private String phoneNumber;
    @Column(unique = true, nullable = false)
    @Pattern(regexp = AADHAR_REGEX, message = INVALID_AADHAR_NUMBER)
    private String aadharNo;
    @Column(nullable = false)
    @NotNull(message = DOB + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private LocalDate dob;
    @Column(nullable = false)
    @NotBlank(message = NAME + SPACE + CANNOT_BE_NULL_OR_BLANK)
    private String name;
    @Column(nullable = false)
    @Size(min = 8, message = PASSWORD_MESSAGE)
    @NotBlank(message = PASSWORD)
    private String password;
    @Column(nullable = false, unique = true)
    @NotBlank(message = EMAIL + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @Email(message = INVALID_EMAIL)
    private String email;

    @NotNull(message = ADDRESS + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @Embedded
    private Address address;

    @ManyToOne
    @JoinTable(name = USER_ROLES, joinColumns = {@JoinColumn(name = USER_ID)}, inverseJoinColumns = {@JoinColumn(name = ROLE_ID)})
    private Role role;


    @OneToMany(mappedBy = USER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Application> applications;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = USER, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserPolicy> userPolicies;

    @OneToMany(mappedBy = USER, fetch = FetchType.EAGER)
    @JsonIgnore
    @JsonManagedReference

    private List<Beneficiary> beneficiaries;

    @NotNull(message = HEALTH_DETAILS + SPACE + CANNOT_BE_NULL_OR_BLANK)
    @OneToOne(mappedBy = USER, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private HealthDetails healthDetails;

    public void setHealthDetails(HealthDetails healthDetails) {
        this.healthDetails = healthDetails;
        this.healthDetails.setUser(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.role.getRoleType().getGrantedAuthorities();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
