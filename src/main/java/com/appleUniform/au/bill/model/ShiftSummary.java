package com.appleUniform.au.bill.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ShiftSummary {
    private double totalShifts;
    private double advance;          // Total advance given to worker
    private double detectedAdvance;  // Amount deducted from this payslip
    private double advanceBalance;   // Remaining balance (advance - detectedAdvance)
    private double finalPay;         // Total amount after advance deduction
    private double totalAmount;      // Total amount before advance deduction
    private List<DailyShift> dailyShifts;
} 