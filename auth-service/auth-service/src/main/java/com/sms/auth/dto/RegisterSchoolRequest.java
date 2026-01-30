package com.sms.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class RegisterSchoolRequest {
    private String schoolName;        // required
    private String schoolAddress;     // optional
    private String schoolBoard;       // optional

    private String adminEmail;        // required
    private String adminPhone;        // optional
    private String adminPassword;     // required
    private String adminName;
}
