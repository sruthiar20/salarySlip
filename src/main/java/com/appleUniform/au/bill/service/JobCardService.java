package com.appleUniform.au.bill.service;

import com.appleUniform.au.bill.model.*;
import com.appleUniform.au.bill.repository.JobCardRepository;
import com.appleUniform.au.bill.repository.SchoolRepository;
import com.appleUniform.au.bill.repository.StyleRepository;
import com.appleUniform.au.bill.repository.WorkerRepository;
import com.appleUniform.au.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.round;

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

        List<JobCard> shiftEntries = new ArrayList<>();
        List<JobCard> nonShiftEntries = new ArrayList<>();

        for (JobCard jc : jobCards) {
            if ("Shift".equalsIgnoreCase(jc.getDepartment())) {
                shiftEntries.add(jc);
            } else {
                nonShiftEntries.add(jc);
            }
        }

        responseList.addAll(processNonShiftEntries(nonShiftEntries));

        responseList.addAll(addShiftEntries(shiftEntries));

        return responseList;
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


    public List<JobCardResponse> addShiftEntries(List<JobCard> shiftEntries) {
        List<JobCardResponse> responses = new ArrayList<>();
        double total = 0;
        double totalAdvance = 0;
        double totalDetectedAdvance = 0;
        double totalPay = 0;
        for (JobCard entry : shiftEntries) {
            Worker worker = workerRepository.findById(entry.getWorkerId()).orElseThrow(
                    () -> new IllegalArgumentException("Worker not found: " + entry.getWorkerId())
            );
            System.out.println("Fetched Worker: " + worker.getWorkerId() + ", Department: " + worker.getDepartment());

            if (!"Shift".equalsIgnoreCase(worker.getDepartment())) {
                throw new IllegalArgumentException("Worker is not in Shift department");
            }


            double rate = worker.getRate();
            double totalHours = computeHours(entry.getStartTime(), entry.getEndTime());
            double calculatedRate = round((totalHours / 8) * rate * 100.0) / 100.0;

            totalPay += calculatedRate;

            // Initialize advance to 0.0 if null
            double advance = entry.getAdvance() != null ? entry.getAdvance() : 0.0;
            totalAdvance += advance;
            
            double detectedAdvance = Math.min(advance, calculatedRate);
            totalDetectedAdvance += detectedAdvance;
            
            double advanceBalance = Math.max(0.0, advance - detectedAdvance);
            double finalPay = calculatedRate - detectedAdvance;

            entry.setDepartment("Shift");
            entry.setRate(rate);
            entry.setTotalHours(round(totalHours * 100.0) / 100.0);
            entry.setCalculatedRate(calculatedRate);
            entry.setTotal(calculatedRate);
            entry.setDate(entry.getDate() != null ? entry.getDate() : LocalDate.now());

            double ratio = calculatedRate / totalPay;
            double individualDetected = round(ratio * detectedAdvance * 100.0) / 100.0;
            double individualFinal = calculatedRate - individualDetected;

            entry.setAdvance(advance);
            entry.setDetectedAdvance(detectedAdvance);
            entry.setAdvanceBalance(advanceBalance);
            entry.setFinalPay(individualFinal);


            jobCardRepository.save(entry);

            JobCardResponse response = new JobCardResponse(
                    entry.getWorkerId(), null, worker.getName(), "", "", entry.getDepartment(),
                    0, calculatedRate, entry.getDate(), "", rate, "", 
                    advance, detectedAdvance, advanceBalance, individualFinal
            );

            responses.add(response);
        }

        System.out.println("Total calculated rate for worker: " + total);
        return responses;
    }

    private double computeHours(String startTime, String endTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime start = LocalTime.parse(startTime, formatter);
        LocalTime end = LocalTime.parse(endTime, formatter);

        double hours = Duration.between(start, end).toMinutes() / 60.0;
        return hours < 0 ? hours + 24 : hours;
    }

    private List<JobCardResponse> processNonShiftEntries(List<JobCard> jobCards) {
        List<JobCardResponse> responseList = new ArrayList<>();

        for (JobCard jobCard : jobCards) {
            Worker worker = workerRepository.findById(jobCard.getWorkerId()).orElseThrow(() ->
                    new IllegalArgumentException("Worker not found!"));

            Style style = styleRepository.findById(jobCard.getStyleId()).orElseThrow(() ->
                    new IllegalArgumentException("Style not found!"));

            Schools school = schoolRepository.findById(jobCard.getSchoolId()).orElseThrow(() ->
                    new IllegalArgumentException("School not found!"));

            if (!Arrays.asList(Constants.STANDARDS).contains(jobCard.getStandard())) {
                throw new IllegalArgumentException("Invalid standard: " + jobCard.getStandard());
            }

            Double rate = style.getDepartmentRates().get(worker.getDepartment());
            if (rate == null) throw new IllegalArgumentException("Rate not defined!");
            Result result = new Result(worker, style, school, rate);

            double adjustedRate = result.rate();
            adjustedRate = getAdjustedRate(jobCard, jobCard.getQuantity(), adjustedRate, result.rate());

            jobCard.setRate(round(adjustedRate));
            double total = round(adjustedRate * jobCard.getQuantity());
            jobCard.setTotal(total);
            jobCard.setDate(LocalDate.now());

            // Initialize advance to 0.0 if null
            double advance = jobCard.getAdvance() != null ? jobCard.getAdvance() : 0.0;
            double detectedAdvance = Math.min(advance, total);
            double advanceBalance = Math.max(0.0, advance - detectedAdvance);
            double finalPay = total - detectedAdvance;
            
            // Set the calculated advance values
            jobCard.setAdvance(advance);
            jobCard.setDetectedAdvance(detectedAdvance);
            jobCard.setAdvanceBalance(advanceBalance);
            jobCard.setFinalPay(finalPay);

            jobCardRepository.save(jobCard);

            JobCardResponse response = new JobCardResponse(
                    jobCard.getWorkerId(),
                    jobCard.getStyleId(),
                    result.worker().getName(),
                    result.style().getName(),
                    result.style().getPattern(),
                    jobCard.getDepartment(),
                    jobCard.getQuantity(),
                    jobCard.getTotal(),
                    jobCard.getDate(),
                    result.school().getName(),
                    jobCard.getRate(),
                    jobCard.getStandard(),
                    advance,
                    detectedAdvance,
                    advanceBalance,
                    finalPay
            );

            responseList.add(response);
        }

        return responseList;
    }

    private record Result(Worker worker, Style style, Schools school, Double rate) {
    }

    private static double getAdjustedRate(JobCard jobCard, int qty, double adjustedRate, Double rate) {
        if ("Cutting".equalsIgnoreCase(jobCard.getDepartment())) {
            if (qty >= 1 && qty <= 20) {
                adjustedRate = rate * 2;
            } else if (qty >= 21 && qty <= 35) {
                adjustedRate = rate * 1.5;
            }
        }
        return adjustedRate;
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