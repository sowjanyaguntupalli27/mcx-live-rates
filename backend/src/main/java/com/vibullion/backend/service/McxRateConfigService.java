package com.vibullion.backend.service;

import com.vibullion.backend.dto.ComparisonResponse;
import com.vibullion.backend.dto.ConfigDto;
import com.vibullion.backend.dto.McxRatesDto;
import com.vibullion.backend.dto.PurityRateDto;
import com.vibullion.backend.entity.LiveRateConfig;
import com.vibullion.backend.enums.Purity;
import com.vibullion.backend.enums.Symbol;
import com.vibullion.backend.properties.LiveRateApi;
import com.vibullion.backend.repository.LiveRateConfigRepository;
import com.vibullion.backend.repository.UserDetailsRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class McxRateConfigService {
    private final RestTemplate restTemplate;
    private final LiveRateApi liveRateApi;
    private final LiveRateConfigRepository liveRateConfigRepository;
    private final UserDetailsRepository userDetailsRepository;

    public McxRateConfigService(RestTemplate restTemplate, LiveRateApi liveRateApi, LiveRateConfigRepository liveRateConfigRepository, UserDetailsRepository userDetailsRepository) {
        this.restTemplate = restTemplate;
        this.liveRateApi = liveRateApi;
        this.liveRateConfigRepository = liveRateConfigRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    public List<McxRatesDto> fetchLiveRates() {
        String url = UriComponentsBuilder.fromUriString(this.liveRateApi.getUrl())
                .toUriString();

        var result = this.restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<List<McxRatesDto>>() {}
        );

        List<McxRatesDto> ratesList = result.getBody();
        List<McxRatesDto> separateResultList = new ArrayList<>();

        if (ratesList != null) {
            McxRatesDto goldRate = ratesList.stream()
                    .filter(rate -> "GOLD".equalsIgnoreCase(rate.getSymbol()))
                    .findFirst()
                    .orElse(null);
            McxRatesDto silverRate = ratesList.stream()
                    .filter(rate -> "SILVER".equalsIgnoreCase(rate.getSymbol()))
                    .findFirst()
                    .orElse(null);
            if (goldRate != null) {
                separateResultList.add(goldRate);
            }
            if (silverRate != null) {
                separateResultList.add(silverRate);
            }
        }

        return separateResultList;
    }

    public double getSpreadChargesFromDatabase() {
        return liveRateConfigRepository.findAll().stream()
                .findFirst()
                .map(LiveRateConfig::getSpreadCharges)
                .orElse(4800.0F);
    }   public List<PurityRateDto> calculatePurityGramRates(McxRatesDto rateDto, double spreadCharges) {
        List<PurityRateDto> calculationResults = new ArrayList<>();

        if (rateDto == null) {
            return calculationResults;
        }

        double totalBasePrice = rateDto.getAsk() + spreadCharges;
        double currentGramRate;

        Symbol symbol = Symbol.valueOf(rateDto.getSymbol().toUpperCase());

        if (symbol == Symbol.GOLD) {
            currentGramRate = totalBasePrice / 10.0;
        } else {
            currentGramRate = totalBasePrice;
        }
        double previousPurityValue = Purity.PURITY_999.getValue();
        for (Purity purity : Purity.values()) {
            if (purity != Purity.PURITY_999) {
                currentGramRate = (currentGramRate * purity.getValue()) / previousPurityValue;
                previousPurityValue = purity.getValue();
            }

            calculationResults.add(new PurityRateDto(purity, roundToTwoDecimals(currentGramRate)));
        }

        return calculationResults;
    }
    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
    public ConfigDto getConfig(Symbol symbol) {
        var config = this.liveRateConfigRepository.findBySymbol(symbol);
        if (config.isPresent()) {
            return ConfigDto.builder()
                    .spreadCharges(config.get().getSpreadCharges())
                    .ask(config.get().getAsk())
                    .symbol(symbol)
                    .otp(0).build();
        }
        return ConfigDto.builder().ask(0).otp(0).build();
    }

    public void setConfig(ConfigDto configDto, Symbol symbol) {
        var users = this.userDetailsRepository.findAll();
        if (users.isEmpty()) {
            throw new RuntimeException("No users found");
        }
        var user = users.getFirst();
        if (user.getOtp() != configDto.getOtp()) {
            throw new RuntimeException("OTP does not match");
        }
        var config = this.liveRateConfigRepository.findBySymbol(symbol);
        if (config.isPresent()) {
            config.get().setAsk(configDto.getAsk());
            config.get().setSpreadCharges(Math.abs(configDto.getSpreadCharges()));
            this.liveRateConfigRepository.save(config.get());
        } else {
            var newConfig = LiveRateConfig.builder().symbol(symbol)
                    .spreadCharges(Math.abs(configDto.getSpreadCharges()))
                    .ask(configDto.getAsk()).build();
            this.liveRateConfigRepository.save(newConfig);
        }
        user.setOtp(null);
        this.userDetailsRepository.save(user);
    }


    public ComparisonResponse preview(ConfigDto configDto) {
        var liveRatesCacheMap = this.fetchLiveRates();
        if (liveRatesCacheMap == null || liveRatesCacheMap.isEmpty()) {
            throw new RuntimeException("No liveRates found");
        }

        var goldConfig = covertToLiveRateConfig(configDto);
        var silverConfig = this.liveRateConfigRepository.findBySymbol(Symbol.SILVER);
        if (goldConfig == null || silverConfig.isEmpty()) {
            throw new RuntimeException("No configuration found");
        }

        McxRatesDto original = liveRatesCacheMap.get((configDto.getSymbol().equals(Symbol.GOLD) ? Symbol.GOLD : Symbol.SILVER).ordinal());
        if (original == null) {
            throw new RuntimeException("No GOLD_SPOT_INR rates found");
        }
        McxRatesDto afterChange = McxRatesDto.builder()
                .symbol(original.getSymbol())
                .Ask(original.getAsk())
                .Bid(original.getBid())
                .LTP(original.getLTP())
                .Low(original.getLow())
                .High(original.getHigh())
                .build();


        return ComparisonResponse.builder()
                .beforeSpotPriceGram24k(original.getAsk())
                .after(afterChange)
                .build();

    }
    private LiveRateConfig covertToLiveRateConfig(ConfigDto configDto) {
        return LiveRateConfig.builder()
                .ask(configDto.getAsk())
                .spreadCharges(configDto.getSpreadCharges())
                .symbol(configDto.getSymbol())
                .build();
    }
}