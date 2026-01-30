package com.sms.auth.service;

import com.sms.auth.dto.LoginRequest;
import com.sms.auth.dto.RegisterSchoolRequest;
import com.sms.auth.dto.RegisterStaffRequest;
import com.sms.auth.dto.UpdatePasswordRequest;
import com.sms.auth.entity.School;
import com.sms.auth.entity.StaffProfile;
import com.sms.auth.entity.User;
import com.sms.auth.entity.enums.StaffWorkingAs;
import com.sms.auth.entity.enums.UserRole;
import com.sms.auth.repository.*;
import com.sms.auth.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final StaffProfileRepository staffProfileRepository;
    private final ParentRepository parentRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
//    private final EmailService emailService;

    /* ================= REGISTER SCHOOL ================= */

    @Transactional
    public ResponseEntity<?> registerSchool(RegisterSchoolRequest req) {

        if (req.getSchoolName() == null ||
                req.getAdminEmail() == null ||
                req.getAdminPassword() == null) {

            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Missing required fields"
            ));
        }

        School school = new School();
        school.setName(req.getSchoolName());
        school.setAddress(req.getSchoolAddress());
        school.setBoard(req.getSchoolBoard());

        schoolRepository.save(school);

        User admin = new User();
        admin.setName(req.getAdminName());
        admin.setEmail(req.getAdminEmail());
        admin.setPhone(req.getAdminPhone());
        admin.setPasswordHash(passwordEncoder.encode(req.getAdminPassword()));
        admin.setRole(UserRole.SCHOOL_ADMIN);
        admin.setIsActive(true);
        admin.setSchool(school);

        userRepository.save(admin);

//        emailService.sendSchoolRegistrationEmail(
//                school.getName(),
//                admin.getEmail(),
//                req.getAdminPassword(),
//                null
//        );

        String token = jwtUtil.generateToken(admin.getId());

        return ResponseEntity.status(201).body(Map.of(
                "success", true,
                "message", "School registered successfully",
                "token", token,
                "data", Map.of(
                        "school", school,
                        "user", admin
                )
        ));
    }
    /* ================= REGISTER STAFF ================= */

    @Transactional
    public ResponseEntity<?> registerStaff(RegisterStaffRequest req, User loggedInUser) {

        UUID schoolId;
        if (loggedInUser.getRole() == UserRole.SCHOOL_ADMIN) {
            schoolId = loggedInUser.getSchool().getId();
        } else if (loggedInUser.getRole() == UserRole.SUPER_ADMIN) {
            if (req.getSchoolId() == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "School ID is required for Super Admin"
                ));
            }
            schoolId = req.getSchoolId();
        } else {
            return ResponseEntity.status(403).body(Map.of(
                    "success", false,
                    "message", "You do not have permission to perform this action"
            ));
        }

        if (req.getEmail() == null ||
                req.getPassword() == null ||
                req.getName() == null ||
                req.getWorkingAs() == null) {

            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Missing required fields"
            ));
        }

        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("School not found"));

        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email already registered"
            ));
        }

        UserRole role =
                req.getWorkingAs().equals("TEACHER")
                        ? UserRole.TEACHER
                        : UserRole.STAFF;

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole(role);
        user.setIsActive(true);
        user.setSchool(school);

        userRepository.save(user);

        StaffProfile staff = new StaffProfile();
        staff.setUser(user);
        staff.setSchool(school);
        staff.setEmployeeCode("EMP-" + System.currentTimeMillis() % 1_000_000);
        staff.setDesignation(req.getDesignation());
        staff.setDepartment(req.getDepartment());
        staff.setJoiningDate(LocalDate.now());
        staff.setWorkingAs(
                StaffWorkingAs.valueOf(
                        req.getWorkingAs().equals("TEACHER")
                                ? "TEACHER"
                                : "STAFF"
                )
        );

        staffProfileRepository.save(staff);

//        emailService.sendStaffRegistrationEmail(
//                school.getName(),
//                user.getName(),
//                user.getEmail(),
//                req.getPassword(),
//                staff.getDesignation(),
//                staff.getDepartment(),
//                school.getLogo()
//        );

        String token = jwtUtil.generateToken(user.getId());

        return ResponseEntity.status(201).body(Map.of(
                "success", true,
                "message", "Staff registered successfully",
                "token", token,
                "data", Map.of(
                        "user", user,
                        "staffProfile", staff
                )
        ));
    }
    /* ================= LOGIN ================= */
    public ResponseEntity<?> login(LoginRequest req) {

        if (req.getEmail() == null || req.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Please provide email and password"
            ));
        }

        Optional<User> userOpt = userRepository.findByEmail(req.getEmail());

        if (userOpt.isEmpty() ||
                !passwordEncoder.matches(req.getPassword(), userOpt.get().getPasswordHash())) {

            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Incorrect email or password"
            ));
        }

        User user = userOpt.get();
        user.setPasswordHash(null);

        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("user", user);

        // Attach role-specific profile
        if (user.getRole() == UserRole.PARENT) {
            parentRepository.findByUserId(user.getId())
                    .ifPresent(parent ->
                            userResponse.put("parent", parent)
                    );
        } else {
            staffProfileRepository.findByUserId(user.getId())
                    .ifPresent(staff ->
                            userResponse.put("staffProfile", staff)
                    );
        }

        String token = jwtUtil.generateToken(user.getId());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Login successful",
                "token", token,
                "data", Map.of("user", user)
        ));
    }


    @Transactional
    public ResponseEntity<?> updatePassword(UpdatePasswordRequest req, User user) {

        if (req.getCurrentPassword() == null || req.getNewPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Please provide both current and new password"
            ));
        }

        if (req.getNewPassword().length() < 8) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "New password must be at least 8 characters long"
            ));
        }

        if (req.getCurrentPassword().equals(req.getNewPassword())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "New password must be different from current password"
            ));
        }

        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body(Map.of(
                    "success", false,
                    "message", "Current password is incorrect"
            ));
        }

        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Password updated successfully"
        ));
    }
}
