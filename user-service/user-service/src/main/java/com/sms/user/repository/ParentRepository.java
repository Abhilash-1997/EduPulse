package com.sms.user.repository;

import com.sms.user.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParentRepository extends JpaRepository<Parent, UUID> {

    // Find parent profile by user ID
    Optional<Parent> findByUserId(UUID userId);
}

