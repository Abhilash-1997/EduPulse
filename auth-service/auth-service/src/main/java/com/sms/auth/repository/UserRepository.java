package com.sms.auth.repository;

import com.sms.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailAndSchoolId(UUID schoolId, String email);
    Optional<User> findByEmail(String email);
}

