package com.vibullion.backend.dto;

import com.vibullion.backend.enums.Purity;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PurityRateDto {
    private Purity purity;
    private int purityValue;
    private double ratePerGram;
    private double bidGramRate;


    public PurityRateDto(Purity purity, double ratePerGram, double bidGramRate) {
        this.purity = purity;
        this.purityValue = purity.getValue();
        this.ratePerGram = ratePerGram;
        this.bidGramRate = bidGramRate;
    }
}
