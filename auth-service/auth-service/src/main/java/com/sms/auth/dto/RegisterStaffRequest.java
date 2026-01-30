package com.sms.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class RegisterStaffRequest {
    private UUID schoolId;        // required ONLY for SUPER_ADMIN
    private String name;          // required
    private String email;         // required
    private String phone;         // optional
    private String password;      // required
    private String workingAs;     // required (TEACHER / STAFF)
    private String designation;   // optional
    private String department;
}
