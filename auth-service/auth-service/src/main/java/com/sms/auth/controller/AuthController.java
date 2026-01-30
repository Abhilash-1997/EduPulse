package com.sms.auth.controller;

import com.sms.auth.dto.*;
import com.sms.auth.entity.User;
import com.sms.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-school")
    public ResponseEntity<?> registerSchool(
            @RequestBody RegisterSchoolRequest request
    ) {
        return authService.registerSchool(request);
    }

    @PostMapping("/register-staff")
    public ResponseEntity<?> registerStaff(
            @RequestBody RegisterStaffRequest request,
            @AuthenticationPrincipal User user
    ) {
        return authService.registerStaff(request, user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(
            @RequestBody UpdatePasswordRequest request,
            @AuthenticationPrincipal User user
    ) {
        return authService.updatePassword(request, user);
    }
}
