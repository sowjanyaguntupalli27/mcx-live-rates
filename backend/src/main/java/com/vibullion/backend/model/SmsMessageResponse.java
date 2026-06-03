package com.vibullion.backend.model;

import lombok.*;

import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class SmsMessageResponse {

    private Integer errorCode;

    private String errorDescription;

    private List<SmsMessageResponseData> data;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class SmsMessageResponseData {
        private Integer messageErrorCode;
        private String messageErrorDescription;
        private String mobileNumber;
        private String messageId;
        private String custom;
    }
}