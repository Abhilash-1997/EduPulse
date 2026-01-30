package com.sms.user.service;

import com.sms.user.entity.User;
import com.sms.user.exception.ResourceNotFoundException;
import com.sms.user.repository.ParentRepository;
import com.sms.user.repository.StaffProfileRepository;
import com.sms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StaffProfileRepository staffProfileRepository;
    private final ParentRepository parentRepository;

    public Map<String, Object> getUserWithProfiles(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);

        staffProfileRepository.findByUserId(userId)
                .ifPresent(staff -> response.put("staffProfile", staff));

        parentRepository.findByUserId(userId)
                .ifPresent(parent -> response.put("parentProfile", parent));

        return response;
    }
}
