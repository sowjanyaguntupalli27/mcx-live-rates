package com.vibullion.backend.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ComparisonResponse {
    private double beforeSpotPriceGram24k;
    private McxRatesDto after;
}
