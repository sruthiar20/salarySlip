package com.appleUniform.au.bill.service;

import com.appleUniform.au.bill.model.Worker;
import com.appleUniform.au.bill.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    public Worker addWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker getWorkerById(String id) {
        return workerRepository.findById(id).orElse(null);
    }
}