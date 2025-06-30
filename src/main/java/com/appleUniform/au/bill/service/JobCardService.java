package com.appleUniform.au.bill.service;

import com.appleUniform.au.bill.model.*;
import com.appleUniform.au.bill.repository.JobCardRepository;
import com.appleUniform.au.bill.repository.SchoolRepository;
import com.appleUniform.au.bill.repository.StyleRepository;
import com.appleUniform.au.bill.repository.WorkerRepository;
import com.appleUniform.au.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JobCardService {

    @Autowired
    private JobCardRepository jobCardRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private StyleRepository styleRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    public List<JobCardResponse> addJobCards(List<JobCard> jobCards) {
        List<JobCardResponse> responseList = new ArrayList<>();
        boolean shiftSaved = false;
        Style style = null;
        Schools school = null;
        Double rate = null;

        for (JobCard jobCard : jobCards) {
            Worker worker = workerRepository.findById(jobCard.getWorkerId()).orElse(null);

            if (worker == null) {
                throw new IllegalArgumentException("Worker not found!");
            }

            boolean isShift = "Shift".equalsIgnoreCase(jobCard.getDepartment());

            if (isShift && shiftSaved) {
                continue;
            }

            if (!isShift) {
                style = styleRepository.findById(jobCard.getStyleId()).orElse(null);
                school = schoolRepository.findById(jobCard.getSchoolId()).orElse(null);

                exceptionChecker(jobCard, style, school);

                rate = style.getDepartmentRates() != null ? style.getDepartmentRates().get(worker.getDepartment()) : null;
                if (rate == null) {
                    throw new IllegalArgumentException("Rate not defined for this department!");
                }

                double adjustedRate = rate;
                int qty = jobCard.getQuantity();

                adjustedRate = getAdjustedRate(jobCard, qty, adjustedRate, rate);

                jobCard.setRate(adjustedRate);
                jobCard.setTotal(adjustedRate * qty);

            } else {
                jobCard.setTotal(jobCard.getRate());
                shiftSaved = true;
            }

            jobCard.setDate(LocalDate.now());
            jobCardRepository.save(jobCard);

            JobCardResponse response = getJobCardResponse(jobCard, worker, style, school);

            responseList.add(response);
        }

        return responseList;
    }

    private static void exceptionChecker(JobCard jobCard, Style style, Schools school) {
        if (style == null) {
            throw new IllegalArgumentException("Style not found!");
        }
        if (school == null) {
            throw new IllegalArgumentException("School not found!");
        }

        if (!Arrays.asList(Constants.STANDARDS).contains(jobCard.getStandard())) {
            throw new IllegalArgumentException("Invalid standard: " + jobCard.getStandard());
        }
    }

    private static double getAdjustedRate(JobCard jobCard, int qty, double adjustedRate, Double rate) {
        if ("Cutting".equalsIgnoreCase(jobCard.getDepartment())) {
            if (qty >= 1 && qty <= 15) {
                adjustedRate = rate * 2;
            } else if (qty >= 16 && qty <= 20) {
                adjustedRate = rate * 1.5;
            }
        }
        return adjustedRate;
    }


    public List<JobCard> getJobCardsByWorker(String workerId) {
        return jobCardRepository.findByWorkerId(workerId);
    }

    public List<JobCardResponse> getAllJobCardResponses() {
        List<JobCard> jobCards = jobCardRepository.findAll();
        List<JobCardResponse> responseList = new ArrayList<>();

        for (JobCard jobCard : jobCards) {
            Worker worker = workerRepository.findById(jobCard.getWorkerId()).orElse(null);

            if (worker == null) continue;

            boolean isShift = "Shift".equalsIgnoreCase(jobCard.getDepartment());
            Style style = !isShift ? styleRepository.findById(jobCard.getStyleId()).orElse(null) : null;
            Schools school = !isShift ? schoolRepository.findById(jobCard.getSchoolId()).orElse(null) : null;

            JobCardResponse response = getJobCardResponse(jobCard, worker, style, school);

            responseList.add(response);
        }

        return responseList;
    }

    private static JobCardResponse getJobCardResponse(JobCard jobCard, Worker worker, Style style, Schools school) {
        JobCardResponse response = new JobCardResponse(
                jobCard.getWorkerId(),
                jobCard.getStyleId(),
                worker.getName(),
                style != null ? style.getName() : "",
                style != null ? style.getPattern() : "",
                jobCard.getDepartment(),
                jobCard.getQuantity(),
                jobCard.getTotal(),
                jobCard.getDate(),
                school != null ? school.getName() : "",
                jobCard.getRate(),
                jobCard.getStandard(),
                jobCard.getAdvance(),
                jobCard.getDetectedAdvance(),
                jobCard.getAdvanceBalance(),
                jobCard.getFinalPay()
        );
        return response;
    }
}