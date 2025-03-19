package com.appleUniform.au.bill.controller;

import com.appleUniform.au.bill.message.SmsService;
import com.appleUniform.au.bill.model.Style;
import com.appleUniform.au.bill.repository.StyleRepository;
import com.appleUniform.au.bill.service.StyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("appleUniformm/styles")
public class StyleController {

    @Autowired
    private StyleService styleService;

    @Autowired
    private StyleRepository styleRepository;

    @Autowired
    private SmsService smsService;


    @PostMapping
    public Style addStyle(@RequestBody Style style) {
        return styleService.addStyle(style);
    }

    @GetMapping
    public List<Style> getAllStyles() {
        return styleService.getAllStyles();
    }

    @PostMapping("/modify-rates")
    public ResponseEntity<String> updateRates(@RequestBody Map<String, Map<String, Double>> updatedRates) {
        updatedRates.forEach((styleId, departmentRates) -> {
            Style style = styleRepository.findById(styleId).orElse(null);
            if (style != null) {
                style.setDepartmentRates(departmentRates);
                styleRepository.save(style);
            }
        });
        return ResponseEntity.ok("Rates updated successfully.");
    }

    @PostMapping("/notify-directors")
    public ResponseEntity<String> notifyDirectors(@RequestBody Map<String, Map<String, Double>> modifiedRates) {
        StringBuilder message = new StringBuilder("Modified Rates:\n");
        modifiedRates.forEach((styleId, rates) -> {
            message.append("Style name: ").append(styleId).append("\n");
            rates.forEach((department, rate) -> {
                message.append("  ").append(department).append(": ").append(rate).append("\n");
            });
        });

        String directorsPhoneNumber = "+919600509364";

        smsService.sendSMS(directorsPhoneNumber, message.toString());

        return ResponseEntity.ok("Directors notified successfully.");
    }
}