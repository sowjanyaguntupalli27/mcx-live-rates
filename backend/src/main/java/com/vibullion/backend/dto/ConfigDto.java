package com.vibullion.backend.dto;


import com.vibullion.backend.enums.Symbol;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConfigDto {

    private float spreadCharges;

    private float ask;

    private int otp;

    private Symbol symbol;

}
