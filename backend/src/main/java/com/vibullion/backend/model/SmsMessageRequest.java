package com.vibullion.backend.model;

import com.vibullion.backend.enums.SmsCode;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class SmsMessageRequest {

    private String phoneNumber;
    private SmsCode smsCode;
    private int otp;
    private String withdrawalAmount;
    private String appName;
}