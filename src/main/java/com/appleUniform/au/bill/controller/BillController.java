package com.appleUniform.au.bill.controller;

import com.appleUniform.au.bill.model.JobCardResponse;
import com.appleUniform.au.bill.service.BillService;
import com.appleUniform.au.bill.service.JobCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("appleUniformm/bills")
public class BillController {

    @Autowired
    private JobCardService jobCardService;

    @Autowired
    private BillService billService;

    @GetMapping("/{workerId}")
    public Map<String, Object> generateBill(@PathVariable String workerId) {
        return billService.generateBill(workerId);
    }

    @GetMapping("/job-cards")
    public List<JobCardResponse> getFilteredJobCards(
            @RequestParam(required = false) String workerName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String departmentName) {

        List<JobCardResponse> allJobCards = jobCardService.getAllJobCardResponses();

        return allJobCards.stream()
                .filter(jobCard -> workerName == null || jobCard.getWorkerName().equalsIgnoreCase(workerName))
                .filter(jobCard -> departmentName == null || jobCard.getDepartment().equalsIgnoreCase(departmentName))
                .filter(jobCard -> startDate == null || !jobCard.getDate().isBefore(startDate))
                .filter(jobCard -> endDate == null || !jobCard.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
}