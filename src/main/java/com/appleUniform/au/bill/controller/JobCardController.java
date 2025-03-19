package com.appleUniform.au.bill.controller;

import com.appleUniform.au.bill.model.JobCard;
import com.appleUniform.au.bill.model.JobCardResponse;
import com.appleUniform.au.bill.model.Style;
import com.appleUniform.au.bill.model.Worker;
import com.appleUniform.au.bill.repository.StyleRepository;
import com.appleUniform.au.bill.repository.WorkerRepository;
import com.appleUniform.au.bill.service.JobCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("appleUniformm/job-cards")
public class JobCardController {

    @Autowired
    private JobCardService jobCardService;
    @PostMapping
    public List<JobCardResponse> addJobCards(@RequestBody List<JobCard> jobCards) {
        return jobCardService.addJobCards(jobCards);
    }
}