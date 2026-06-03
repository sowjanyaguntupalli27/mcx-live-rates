package com.vibullion.backend.controller.controller;

import com.vibullion.backend.dto.McxRatesDto;
import com.vibullion.backend.dto.PurityRateDto;
import com.vibullion.backend.service.McxRateConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping(value = "/mcxRates/purities")
    public Map<String, List<PurityRateDto>> getBothPurityRates() {
        List<McxRatesDto> liveRates = mcxRateConfigService.fetchLiveRates();
        double goldCharges = mcxRateConfigService.getGoldSpreadCharges();
        double silverCharges = mcxRateConfigService.getSilverSpreadCharges();

        Map<String, List<PurityRateDto>> completeResponse = new HashMap<>();

        liveRates.stream()
                .filter(rate -> "GOLD".equalsIgnoreCase(rate.getSymbol()))
                .findFirst()
                .ifPresent(gold -> completeResponse.put("gold",
                        mcxRateConfigService.calculatePurityGramRates(gold, goldCharges)));

        liveRates.stream()
                .filter(rate -> "SILVER".equalsIgnoreCase(rate.getSymbol()))
                .findFirst()
                .ifPresent(silver -> completeResponse.put("silver",
                        mcxRateConfigService.calculatePurityGramRates(silver, silverCharges)));

        return completeResponse;
    }


}
