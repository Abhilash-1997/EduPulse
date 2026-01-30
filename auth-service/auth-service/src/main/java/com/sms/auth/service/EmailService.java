package com.sms.auth.service;

public interface EmailService {
    void sendSchoolRegistrationEmail(
            String schoolName,
            String adminEmail,
            String adminPassword,
            String logoUrl
    );

    void sendStaffRegistrationEmail(
            String schoolName,
            String staffName,
            String staffEmail,
            String password,
            String designation,
            String department,
            String schoolLogoUrl
    );

}
