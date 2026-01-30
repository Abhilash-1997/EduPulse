package com.sms.user.entity;

import com.sms.user.entity.enums.StaffStatus;
import com.sms.user.entity.enums.StaffWorkingAs;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "staff_profiles",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_staff_school_employee_code",
                        columnNames = {"school_id", "employee_code"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE staff_profiles SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class StaffProfile {

    /* ================= ID ================= */

    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    /* ================= FK IDS ================= */
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "school_id", nullable = false)
    private UUID schoolId;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    /* ================= EMPLOYEE ================= */

    @Column(name = "employee_code", nullable = false)
    private String employeeCode;

    @Column(nullable = true)
    private String department;

    @Column(nullable = true)
    private String designation;

    /* ================= WORK ================= */

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "working_as", nullable = false)
    private StaffWorkingAs workingAs = StaffWorkingAs.TEACHER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StaffStatus status = StaffStatus.PRE_BOARDING;

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
