package com.vibullion.backend.controller;

import com.vibullion.backend.dto.ConfigDto;
import com.vibullion.backend.enums.Symbol;
import com.vibullion.backend.service.McxRateConfigService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final McxRateConfigService mcxRateConfigService;
    private final String buildVersion;

    public HomeController(McxRateConfigService mcxRateConfigService, @Value("${com.vibullion.build}") String buildVersion) {
        this.mcxRateConfigService = mcxRateConfigService;
        this.buildVersion = buildVersion;
    }

    @GetMapping("/")
    public String getHome(Model model) {
        var liveRates = mcxRateConfigService.fetchLiveRates();
        model.addAttribute("liveRates", liveRates);
        model.addAttribute("version", this.buildVersion);
        return "index";
    }

    @GetMapping("/goldConfig")
    public ModelAndView goldConfigPage(@ModelAttribute("formData") ConfigDto configDto, BindingResult bindingResult, Model model) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("goldconfig");
        var config = this.mcxRateConfigService.getConfig(Symbol.GOLD);
        mv.addObject("formData", config);
        mv.addObject("version", this.buildVersion);
        return mv;
    }

    @GetMapping("/silverConfig")
    public ModelAndView silverConfigPage(@ModelAttribute("formData") ConfigDto configDto, BindingResult bindingResult, Model model) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("silverconfig");
        var config = this.mcxRateConfigService.getConfig(Symbol.SILVER);
        mv.addObject("formData", config);
        mv.addObject("version", this.buildVersion);
        return mv;
    }

    @PostMapping("/setupGoldPrice")
    public ModelAndView setupGoldConfig(@ModelAttribute("formData") ConfigDto configDto, BindingResult bindingResult, Model model) {
        ModelAndView mv = new ModelAndView();
        try {
            this.mcxRateConfigService.setConfig(configDto, Symbol.GOLD);
        } catch (Exception e) {
            mv.setViewName("error");
            mv.addObject("version", this.buildVersion);
            return mv;
        }
        mv.setViewName("index");
        var liveRates = mcxRateConfigService.fetchLiveRates();
        mv.addObject("liveRates", liveRates);
        mv.addObject("version", this.buildVersion);
        return mv;
    }

    @PostMapping("/setupSilverPrice")
    public ModelAndView setupSilverConfig(@ModelAttribute("formData") ConfigDto configDto, BindingResult bindingResult, Model model) {
        ModelAndView mv = new ModelAndView();
        try {
            this.mcxRateConfigService.setConfig(configDto, Symbol.SILVER);
        } catch (Exception e) {
            mv.setViewName("error");
            mv.addObject("version", this.buildVersion);
            return mv;
        }
        mv.setViewName("index");
        var liveRates = mcxRateConfigService.fetchLiveRates();
        mv.addObject("liveRates", liveRates);
        mv.addObject("version", this.buildVersion);
        return mv;
    }

    @GetMapping("/privacy")
    public ModelAndView privacy(@ModelAttribute("formData") ConfigDto configDto, BindingResult bindingResult, Model model) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("privacy");
        mv.addObject("version", this.buildVersion);
        return mv;
    }

    @GetMapping("/about-us")
    public ModelAndView aboutUs(@ModelAttribute("formData") ConfigDto configDto, BindingResult bindingResult, Model model) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("about-us");
        mv.addObject("version", this.buildVersion);
        return mv;
    }

    @GetMapping("/error")
    public ModelAndView errorPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        mv.addObject("version", this.buildVersion);
        return mv;
    }

}
