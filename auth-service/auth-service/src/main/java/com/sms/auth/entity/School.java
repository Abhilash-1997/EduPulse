package com.sms.auth.entity;

import com.sms.auth.entity.enums.SchoolStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "schools")
@Getter
@Setter
@SQLDelete(sql = "UPDATE schools SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class School {

    /* ================= PRIMARY KEY ================= */

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, updatable = false, nullable = false)
    private UUID id;

    /* ================= BASIC INFO ================= */

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(nullable = true)
    private String logo;

    /* ================= ACADEMIC INFO ================= */

    @Column(nullable = true)
    @Comment("e.g., CBSE, ICSE, State Board")
    private String board;

    @Column(name = "academic_year", nullable = true)
    @Comment("e.g., 2024-2025")
    private String academicYear;

    /* ================= STATUS ================= */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SchoolStatus status = SchoolStatus.PENDING;

    /* ================= TIMESTAMPS ================= */

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* ================= SOFT DELETE ================= */

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
