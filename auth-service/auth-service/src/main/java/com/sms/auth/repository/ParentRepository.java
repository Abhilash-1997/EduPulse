package com.sms.auth.repository;

import com.sms.auth.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParentRepository extends JpaRepository<Parent, UUID> {
    Optional<Parent> findByUserId(UUID userId);
    List<Parent> findBySchoolId(UUID schoolId);
}
