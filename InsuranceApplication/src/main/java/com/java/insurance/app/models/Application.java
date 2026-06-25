package com.java.insurance.app.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.insurance.app.models.enums.ApplicationStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.java.insurance.app.constants.AppConstant.APPLICATION;
import static com.java.insurance.app.constants.AppConstant.UNDERWRITER_ID;

@Data
@Entity
@Builder
@Table(name = APPLICATION)
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user"})
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JsonIgnore
    private User user;

    @JoinColumn(name = UNDERWRITER_ID)
    @ManyToOne
    private User underwriter;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Policy> policies = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime creationDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus applicationStatus;
    @Column(nullable = false)
    private double totalMonthlyPremium;
}
