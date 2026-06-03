package com.vibullion.backend.service;
import com.vibullion.backend.dto.UserDetailsDto;
import com.vibullion.backend.enums.SmsCode;
import com.vibullion.backend.model.SmsMessageRequest;
import com.vibullion.backend.model.SmsMessageResponse;
import com.vibullion.backend.properties.SmsProperties;
import com.vibullion.backend.repository.UserDetailsRepository;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class UserService {

    private final RestTemplate restTemplate;

    private final UserDetailsRepository userDetailsRepository;

    private final SmsProperties smsProperties;

    private final RandomDataGenerator randomDataGenerator = new RandomDataGenerator();

    public UserService(RestTemplate restTemplate, UserDetailsRepository userDetailsRepository, SmsProperties smsProperties) {
        this.restTemplate = restTemplate;
        this.userDetailsRepository = userDetailsRepository;
        this.smsProperties = smsProperties;
    }

    public UserDetailsDto getUser(){
        var userDetailsEntity = this.userDetailsRepository.findAll().getFirst();
        return UserDetailsDto.builder().phone(userDetailsEntity.getPhone()).build();
    }

    public void sendOtp() {
        var users = this.userDetailsRepository.findAll();
        if(users.isEmpty()) {
            throw new RuntimeException("No users found");
        }
        var _otp = randomDataGenerator.nextInt(100000, 999999);
        var user = users.getFirst();
        ParameterizedTypeReference<SmsMessageResponse> smsTypeRef =
                new ParameterizedTypeReference<>() {
                };
        var reqBody = SmsMessageRequest.builder().phoneNumber(user.getPhone())
                .otp(_otp).smsCode(SmsCode.OTP)
                .appName(this.smsProperties.getAppName())
                .build();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SmsMessageRequest> request =
                new HttpEntity<>(reqBody, headers);

        var result = this.restTemplate.exchange(this.smsProperties.getUrl(), HttpMethod.POST, request, smsTypeRef);
        if (!result.getStatusCode().equals(HttpStatus.OK)) {
            throw new RuntimeException("OTP failed");
        }
        //Saving OTP
        user.setOtp(_otp);
        this.userDetailsRepository.save(user);
    }


}
