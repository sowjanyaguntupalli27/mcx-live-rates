package com.vibullion.backend.controller;

import com.vibullion.backend.dto.ComparisonResponse;
import com.vibullion.backend.dto.ConfigDto;
import com.vibullion.backend.service.McxRateConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")

public class ConfigApiController {

    private final McxRateConfigService mcxRateConfigService;

    public ConfigApiController(McxRateConfigService mcxRateConfigService) {
        this.mcxRateConfigService = mcxRateConfigService;
    }

    @PostMapping("/preview")
    public ResponseEntity<ComparisonResponse> checkGoldPriceAfterConfig(@RequestBody ConfigDto configDto) {
        ComparisonResponse result = mcxRateConfigService.preview(configDto);
        return ResponseEntity.ok(result);
    }
}
