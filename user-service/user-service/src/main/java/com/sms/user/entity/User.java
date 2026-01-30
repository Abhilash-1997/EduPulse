package com.sms.user.entity;

import com.sms.user.entity.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_school_email",
                        columnNames = {"school_id", "email"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE users SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class User {

    /* ================= ID ================= */

    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = true)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String phone;

    /* ================= SCHOOL ================= */

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "school_id", nullable = true)
    private UUID schoolId;

    /* ================= ROLE ================= */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    /* ================= STATUS ================= */

    @Column(name = "is_active")
    private Boolean isActive = true;

    /* ================= AUDIT ================= */

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /* ================= LIFECYCLE ================= */

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
