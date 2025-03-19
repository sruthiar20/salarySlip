package com.appleUniform.au.bill.service;

import com.appleUniform.au.bill.model.JobCard;
import com.appleUniform.au.bill.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BillService {

    @Autowired
    private JobCardService jobCardService;

    @Autowired
    private WorkerService workerService;

    public Map<String, Object> generateBill(String workerId) {
        Worker worker = workerService.getWorkerById(workerId);
        if (worker == null) {
            throw new IllegalArgumentException("Worker not found!");
        }

        List<JobCard> JobCards = jobCardService.getJobCardsByWorker(workerId);
        double totalBill = JobCards.stream().mapToDouble(JobCard::getTotal).sum();

        Map<String, Object> bill = new HashMap<>();
        bill.put("worker", worker.getName());
        bill.put("jobCards", JobCards);
        bill.put("totalBill", totalBill);

        return bill;
    }
}