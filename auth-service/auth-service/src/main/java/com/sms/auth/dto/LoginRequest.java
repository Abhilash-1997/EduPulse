package com.sms.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
    private String email;      // required
    private String password;   // required
}
