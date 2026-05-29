package com.vibullion.backend.service;

import com.vibullion.backend.dto.McxRatesDto;
import com.vibullion.backend.properties.LiveRateApi;
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

    public McxRateConfigService(RestTemplate restTemplate, LiveRateApi liveRateApi) {
        this.restTemplate = restTemplate;
        this.liveRateApi = liveRateApi;
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
                double goldLtp = goldRate.getLTP();
                separateResultList.add(goldRate);
            }

            if (silverRate != null) {
                double silverLtp = silverRate.getLTP();
                separateResultList.add(silverRate);
            }
        }

        return separateResultList;
    }

    }