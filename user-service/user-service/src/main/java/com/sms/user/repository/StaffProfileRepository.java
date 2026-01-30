package com.sms.user.repository;

import com.sms.user.entity.StaffProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StaffProfileRepository extends JpaRepository<StaffProfile, UUID> {

    // Find staff profile by user ID
    Optional<StaffProfile> findByUserId(UUID userId);

    // Find staff profile by school ID and employee code
    Optional<StaffProfile> findBySchoolIdAndEmployeeCode(UUID schoolId, String employeeCode);
}
