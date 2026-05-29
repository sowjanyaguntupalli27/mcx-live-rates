package com.vibullion.backend.entity;
import com.vibullion.backend.enums.Symbol;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "LIVE_RATE_CONFIG")

public class LiveRateConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "SPREAD_CHARGES", nullable = false)
    private float spreadCharges;

    @Column(name = "ASK", nullable = false)
    private float ask;

    @Column(name = "BUY", nullable = false)
    private float buy;

    @Column(name = "SYMBOL", nullable = false)
    @Enumerated(EnumType.STRING)
    private Symbol symbol;
}
