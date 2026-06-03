package com.vibullion.backend.config;

import com.vibullion.backend.dto.ConfigDto;
import com.vibullion.backend.dto.UserDetailsDto;
import com.vibullion.backend.enums.Symbol;
import com.vibullion.backend.service.McxRateConfigService;
import com.vibullion.backend.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class AppController {

    private final UserService userService;

    private final McxRateConfigService liveRateConfigService;

    public AppController(UserService userService, McxRateConfigService liveRateConfigService) {
        this.userService = userService;
        this.liveRateConfigService = liveRateConfigService;
    }

    @GetMapping("/user")
    public UserDetailsDto getUserDetails() {
      return this.userService.getUser();
    }

    @PostMapping("/appConfig")
    public void setupGoldConfig(@RequestBody ConfigDto configDto) {
       this.liveRateConfigService.setConfig(configDto, configDto.getSymbol());
    }

    @GetMapping("/config")
    public List<ConfigDto> setupGoldConfig() {
        var goldConfig = this.liveRateConfigService.getConfig(Symbol.GOLD);
        var silverConfig = this.liveRateConfigService.getConfig(Symbol.SILVER);
        return List.of(goldConfig, silverConfig);
    }

}
