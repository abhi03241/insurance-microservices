package com.java.insurance.app.repositories;

import com.java.insurance.app.models.Application;
import com.java.insurance.app.models.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    List<Application> findByUserId(int userId);

    List<Application> findByApplicationStatus(ApplicationStatus applicationStatus);

    List<Application> findByUnderwriterIdAndApplicationStatus(int underwriterId, ApplicationStatus status);

    List<Application> findByUnderwriterId(int underwriterId);
}
