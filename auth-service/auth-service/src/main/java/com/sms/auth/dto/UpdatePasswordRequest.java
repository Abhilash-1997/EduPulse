package com.sms.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UpdatePasswordRequest {
    private String currentPassword;   // required
    private String newPassword;       // required
}
