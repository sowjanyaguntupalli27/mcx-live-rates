package com.vibullion.backend.controller.controller;

import com.vibullion.backend.dto.ConfigDto;
import com.vibullion.backend.enums.Symbol;
import com.vibullion.backend.service.McxRateConfigService;
import com.vibullion.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping("/api/config")
public class UserController {

    private final UserService userService;
    private final McxRateConfigService configService;
    public UserController(UserService userService, McxRateConfigService configService) {
        this.userService = userService;
        this.configService = configService;
    }
    @GetMapping("/{symbol}")
    public ResponseEntity<ConfigDto> getConfig(@PathVariable Symbol symbol) {
        return ResponseEntity.ok(configService.getConfig(symbol));
    }

    @GetMapping("/otp")
    public void sendOtp() {
        this.userService.sendOtp();
    }

    @PostMapping("/{symbol}")
    public ResponseEntity<Map<String, String>> updateConfig(@ModelAttribute ConfigDto configDto, @PathVariable Symbol symbol) {
        configService.setConfig(configDto, symbol);
        return ResponseEntity.ok(Map.of("message", "Configuration updated successfully!"));
    }


}
