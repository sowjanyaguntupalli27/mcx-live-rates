package com.vibullion.backend.controller;

import com.vibullion.backend.dto.McxRatesDto;
import com.vibullion.backend.service.McxRateConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class McxRatesController {

    private final McxRateConfigService mcxRateConfigService;
    public McxRatesController(McxRateConfigService mcxRateConfigService) {
        this.mcxRateConfigService = mcxRateConfigService;
    }

    @GetMapping(value = "/mcxRates")
    public List<McxRatesDto> getRates() {
        return mcxRateConfigService.fetchLiveRates();
    }

}
