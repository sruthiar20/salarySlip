package com.appleUniform.au.bill.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class JobCardResponse {
    private String workerId;
    private String styleId;
    private String workerName;
    private String styleName;
    private String patternName;
    private String department;
    private int quantity;
    private double total;
    private LocalDate date;
    private String schoolName;
    private double rate;
    private String standard;
    private Double advance;
    private Double detectedAdvance;
    private Double advanceBalance;
    private Double finalPay;


}