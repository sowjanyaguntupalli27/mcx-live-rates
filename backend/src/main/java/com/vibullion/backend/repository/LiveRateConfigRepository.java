package com.vibullion.backend.repository;

import com.vibullion.backend.entity.LiveRateConfig;
import com.vibullion.backend.enums.Symbol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LiveRateConfigRepository extends JpaRepository<LiveRateConfig, UUID> {

    public Optional<LiveRateConfig> findBySymbol(Symbol symbol);
}
