package com.sms.auth.service.impl;

import com.sms.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendSchoolRegistrationEmail(
            String schoolName,
            String adminEmail,
            String adminPassword,
            String logoUrl
    ) {
        String subject = "School Registration Successful";

        String body = """
                Welcome to School Management System!

                School Name: %s
                Admin Email: %s
                Temporary Password: %s

                Please login and change your password after first login.
                """.formatted(
                schoolName,
                adminEmail,
                adminPassword
        );

        sendSimpleMail(adminEmail, subject, body);
    }

    @Override
    public void sendStaffRegistrationEmail(
            String schoolName,
            String staffName,
            String staffEmail,
            String password,
            String designation,
            String department,
            String schoolLogoUrl
    ) {
        String subject = "Staff Registration Successful";

        String body = """
                Welcome to %s!

                Name: %s
                Email: %s
                Designation: %s
                Department: %s
                Temporary Password: %s

                Please login and change your password after first login.
                """.formatted(
                schoolName,
                staffName,
                staffEmail,
                designation,
                department,
                password
        );

        sendSimpleMail(staffEmail, subject, body);
    }

    /* ================= COMMON MAIL SENDER ================= */

    private void sendSimpleMail(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
