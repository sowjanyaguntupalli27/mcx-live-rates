package com.vibullion.backend.dto;

import lombok.*;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class UserDetailsDto {

    private UUID id;

    private String phone;

    private Integer otp;
}
