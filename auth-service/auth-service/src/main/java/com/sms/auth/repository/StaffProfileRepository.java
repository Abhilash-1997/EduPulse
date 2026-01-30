package com.sms.auth.repository;

import com.sms.auth.entity.StaffProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StaffProfileRepository extends JpaRepository<StaffProfile, UUID> {
    Optional<StaffProfile> findByUserId(UUID userId);
    Optional<StaffProfile> findBySchoolIdAndEmployeeCode(UUID schoolId, String employeeCode);
}
