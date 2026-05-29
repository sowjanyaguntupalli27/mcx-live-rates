package com.vibullion.backend.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class McxRatesDto {
    private String symbol;
    private String Name;
    private double Bid;
    private double Ask;
    private double High;
    private double Low;
    private double Open;
    private double Close;
    private double LTP;
    private String Difference;
    private String Time;
    private String V;
}