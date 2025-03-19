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
import java.util.Collections;
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

        for (JobCard jobCard : jobCards) {
            Worker worker = workerRepository.findById(jobCard.getWorkerId()).orElse(null);
            Style style = styleRepository.findById(jobCard.getStyleId()).orElse(null);
            Schools schools = schoolRepository.findById(jobCard.getSchoolId()).orElse(null);

            if (worker == null || style == null) {
                throw new IllegalArgumentException("Worker or Style not found!");
            }
            if (schools == null) {
                throw new IllegalArgumentException("School not found!");
            }
            if (!Arrays.asList(Constants.STANDARDS).contains(jobCard.getStandard())) {
                throw new IllegalArgumentException("Invalid standard: " + jobCard.getStandard());
            }

            Double rate = style.getDepartmentRates() != null ? style.getDepartmentRates().get(worker.getDepartment()) : null;
            if (rate == null) {
                throw new IllegalArgumentException("Rate not defined for this department!");
            }

            jobCard.setRate(rate);
            jobCard.setTotal(rate * jobCard.getQuantity());
            jobCard.setDate(LocalDate.now());

            jobCardRepository.save(jobCard);

            JobCardResponse response = new JobCardResponse(
                    jobCard.getWorkerId(),
                    jobCard.getStyleId(),
                    worker.getName(),
                    style.getName(),
                    style.getPattern(),
                    jobCard.getDepartment(),
                    jobCard.getQuantity(),
                    jobCard.getTotal(),
                    jobCard.getDate(),
                    schools.getName(),
                    rate,
                    jobCard.getStandard()
            );

            responseList.add(response);
        }

        return responseList;
    }

    public List<JobCard> getJobCardsByWorker(String workerId) {
        return jobCardRepository.findByWorkerId(workerId);
    }
}