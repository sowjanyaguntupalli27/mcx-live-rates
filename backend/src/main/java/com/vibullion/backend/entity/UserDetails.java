package com.vibullion.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "USER_DETAILS")

public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phone;

    @Column(name = "OTP")
    private Integer otp;
}
